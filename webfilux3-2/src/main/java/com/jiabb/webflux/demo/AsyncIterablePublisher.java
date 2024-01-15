package com.jiabb.webflux.demo;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description: Publisher 的实现, 使用指定的Executor异步执行, 并为指定的Iterable生成元素
 * 以unicast的形式为Subscriber只指定的Iterable 生成元素
 * 注意: 该类中使用了很多try-catch 用于说明什么时候可以抛异常, 什么时候不可以抛异常
 * @author: jia_b
 * @date: 2024/1/15 20:33
 * @since: 1.0
 */
public class AsyncIterablePublisher<T> implements Publisher<T> {

    // 默认的批次大小
    private final static int DEFAULT_BATCH_SIZE = 1024;

    // 用于生成数据的数据源或生成器
    private final Iterable<T> elements;

    // 线程池 Publisher 使用线程池为它的订阅者异步执行
    private final Executor executor;

    // 既然使用了线程池,就不要在一个线程中执行太多的任务
    // 此处使用批次大小调节单个线程的执行时长
    private final int batchSize;

    /**
     * @param elements 元素生成器
     * @param executor 线程池
     */
    public AsyncIterablePublisher(final Iterable<T> elements, final Executor executor) {
        // 调用重载的构造器,使用默认的批次大小, 指定的数据源和指定的线程池
        this(elements, DEFAULT_BATCH_SIZE, executor);
    }

    /**
     * 全参构造器 构造Publisher实例
     */
    public AsyncIterablePublisher(final Iterable<T> elements, final int batchSize, final Executor executor) {
        // 如果不指定元素发生器则抛出异常
        if (Objects.isNull(elements))
            throw new NullPointerException("elements is null");
        // 如果不指定线程池 则抛出异常
        if (Objects.isNull(executor))
            throw new NullPointerException("executor is null");
        if (batchSize < 1)
            throw new IllegalArgumentException("batchSize must be greater than zero");
        // 赋值批次大小
        this.batchSize = batchSize;
        // 赋值元素发生器
        this.elements = elements;
        // 赋值线程池
        this.executor = executor;
    }

    /**
     * 订阅者方法
     * 规范2.13指出, 该方法必须正常返回, 不能抛出异常等
     * 在当前实现中, 我们使用unicast的方式支持多个订阅者
     *
     * @param s 订阅者
     */
    @Override
    public void subscribe(final Subscriber<? super T> s) {
        new SubscriptionImpl(s).init();
    }

    // These represent the protocol of the `AsyncIterablePublishers` SubscriptionImpl
    // 静态接口: 信号
    static interface Signal {

    }

    // 取消订阅的信号
    enum Cancel implements Signal {Instance;}

    // 订阅的信号
    enum Subscribe implements Signal {Instance;}

    // 发送的信号
    enum Send implements Signal {Instance;}

    // 静态类, 表示请求信号
    static final class Request implements Signal {
        final long n;

        Request(final long n) {
            this.n = n;
        }

    }

    // 订阅票据,实现Subscription和Runnable
    final class SubscriptionImpl implements Subscription, Runnable {

        // 需要保有Subscriber的引用, 以用于通信
        final Subscriber<? super T> subscriber;

        // 该订阅票据是否失效的标志
        private boolean cancelled = false;

        // 跟踪当前请求
        // 记录订阅着的请求, 这些请求还没有对订阅者回复
        private long demand = 0;
        // 需要发送给订阅者(Subscriber) 的数据流指针
        private Iterator<T> iterator;

        SubscriptionImpl(final Subscriber<? super T> subscriber) {
            // 依据规范, 如果Subscriber 为null  需要抛出空指针异常 此处抛出null
            if (Objects.isNull(subscriber))
                throw new NullPointerException("subscriber is null");
            this.subscriber = subscriber;
        }

        // 该队列记录发送给票据的信号(入站信号) 如:request, cancel 等
        // 通过该Queue, 可以在Publisher端使用多线程异步处理
        private final ConcurrentLinkedDeque<Signal> inboundSignals = new ConcurrentLinkedDeque<Signal>();

        // 确保当前票据不会并发的标志
        // 防止在调用订阅者的onXXX 方法的时候并发调用, 规范1.3规定的不能并发
        private final AtomicBoolean on = new AtomicBoolean(false);

        // 注册订阅者发送来的请求
        private void doRequest(final long n) {
            // 规范规定, 如果请求的元素个数小于1, 则抛异常
            // 并在异常信息中指明错误的原因: n必须为正整数
            if (n < 1) {
                terminateDueTo(new IllegalArgumentException(subscriber + "violated the Reactive Streams rule 3.9 by requesting" +
                        " a non-positive number of elements."));
                // demand + n < 1 表示long型数字越界, 表示订阅者请求的元素数量大于Long.MAX_VALUE
            } else if (demand + n < 1) {
                // 根据规范3.17 当请求的元素大于Long.MAX_VALUE的时候, 将请求数只为Long.MAX_VALUE 即可
                demand = Long.MAX_VALUE;
                // 此时数据流认为是无界流
                doSend();
            } else {
                //记录下游请求 的元素个数
                demand += n;
                // 此时数据流认为是无界流
                doSend();
            }
        }

        // 规范3.5指明, Subscription.cancel方法必须及时的返回, 保持调用者的响应性, 还必须似乎幂等的, 必须是线程安全的
        // 因此该方法不能执行密集的计算
        private void doCancel() {
            cancelled = true;
        }

        // 不是在 `Publisher.subscribe`方法中同步抛出 `Subscriber.onSubscribe`方法 而是异步地执行subscriber.onSubscribe方法
        // 这样可以避免在调用线程执行用户的代码. 因为在订阅者的onSubscribe方法中要执行Iterable.iterator方法
        // 异步处理也无形中遵循了规范1.9
        private void doSubscribe() {
            try {
                // 获取数据源的迭代器
                iterator = elements.iterator();
                if (Objects.isNull(iterator)) {
                    // 如果iterator 为空, 就重置为空集合的迭代器, 我们假设永不是null
                    iterator = Collections.emptyIterator();
                }
            } catch (final Throwable t) {
                // Publisher 发生了异常,此时需要通知订阅者的OnError信号
                // 但是规范1.9指定了在通知订阅者其他信号之前,必须先通知订阅者onSubscribe信号
                // 因此,此处通知订阅中onSubscribe信号, 发送空的订阅票据
                subscriber.onSubscribe(new Subscription() {

                    @Override
                    public void request(long l) {
                        // 空
                    }

                    @Override
                    public void cancel() {
                        // 空
                    }
                });
                // 规范1.9 通知订阅者的onError 信号
                terminateDueTo(t);
            }
            if (!cancelled) {
                // 为订阅者设置订阅器票据
                try {
                    // 此处的this就是Subscription的实现类SubscriptionImpl的对象
                    subscriber.onSubscribe(this);
                } catch (final Throwable t) {
                    // Publisher 发生了异常,此时需要通知订阅者的OnError信号
                    // 但是规范2.3 指定了在通知订阅者onError信号之前,必须先取消该订阅者的订阅票据
                    // publisher 记录下异常信息
                    terminateDueTo(new IllegalArgumentException(subscriber + "violated the Reactive Streams rule 2.13 by throwing" +
                            " an exception from onSubscribe.", t));
                }
                // 立即处理已经完成的迭代器
                boolean hasElements = false;
                try {
                    // 判断是否还有为发送的数据, 如果没有, 则向订阅者发送onComplete信号
                    hasElements = iterator.hasNext();
                } catch (final Throwable t) {
                    // 规范的1.4 规定
                    // 如果hasNext发生异常, 必须向订阅者发送onError信号,发送信号之前先取消订阅
                    // 规范1.2 规定, Publisher通过向订阅者通知onError或者onComplete信号
                    // 发送少于订阅者请求的onNext信号
                    terminateDueTo(t);
                }

                // 如果没有数据发送了,表示已经完成,直接发送onComplete信息终止订阅票据
                if (!hasElements) {
                    try {
                        doCancel();
                        subscriber.onComplete();
                    } catch (final Throwable t) {
                        new IllegalArgumentException(subscriber + "violated the Reactive Streams rule 2.13 by throwing" +
                                " an exception from onComplete.", t).printStackTrace(System.err);
                    }
                }

            }


        }

        // 向下游发送元素的方法
        private void doSend() {
            try {
                // 为了充分利用Executor, 我们最多发送batchSize给元素,然后放弃当前线程,重新调度,通知订阅者onNext信号
                int leftInBatch = batchSize;
                do {
                    T next;
                    boolean hasNext;
                    try {
                        // 在订阅的时候已经调用过hasNext方法了,直接获取元素
                        next = iterator.next();
                        // 检查还有没有数据, 如果没有, 表示流结束了
                        hasNext = iterator.hasNext();
                    } catch (final Throwable t) {
                        // 如果next方法或hasNext方法抛出异常(用户提供的, 认为流抛异常了发送OnError信号)
                        terminateDueTo(t);
                        return;
                    }
                    // 向下游的订阅者发送onNext信号
                    subscriber.onNext(next);
                    // 如果已经到达流的结尾
                    if (!hasNext) {
                        // 首先考虑是票据取消了订阅
                        doCancel();
                        // 发送完成信号给订阅者
                        subscriber.onComplete();
                    }
                } while (!cancelled // 如果没有取消订阅
                        && --leftInBatch > 0 // 如果还有剩余批次的元素
                        && --demand > 0);  // 如果还有订阅者的请求
                // 如果订阅票据没有取消, 还有请求,通知自己发送更多的数据
                if (!cancelled && demand > 0) {
                    signal(Send.Instance);
                }
            } catch (final Throwable t) {
                // 确保已取消 因为是Subscriber的问题
                doCancel();
                (new IllegalArgumentException(subscriber + "violated the Reactive Streams rule 2.13 by throwing an exception from onNext or onComplete", t)).printStackTrace(System.err);
            }
        }

        /**
         * 当发送onError信号之前先取消订阅
         */
        private void terminateDueTo(final Throwable t) {
            cancelled = true;
            try {
                subscriber.onError(t);
            } catch (final Throwable t2) {
                (new IllegalArgumentException(subscriber + "violated the Reactive Streams rule 2.13 by throwing an exception from onNext or onError", t)).printStackTrace(System.err);
            }
        }


        // 该方法异步地给订阅票据发送指定信号
        private void signal(final Signal signal) {
            // 入站信号的队列,不需要检查是否是null 因为已经实例化过ConcurrentLinkedQueue
            // 将信号添加到入站信号队列中
            if (inboundSignals.offer(signal)) {
                // 信号入站成功, 调度线程处理
                tryScheduleToExecute();
            }
        }

        // 主事件循环
        @Override
        public void run() {
            // 与上次线程执行建立happens-before 关系防止并发执行
            // 如果on.get()为false 则不执行, 线程退出
            // 如果on.get()为true 则表示没有线程在执行, 当前线程可以执行
            if (on.get()) {
                try {
                    // 队列取出一个入站信号
                    final Signal s = inboundSignals.poll();
                    // 如果订阅票据没有取消
                    if (!cancelled) {
                        if (s instanceof Request) {
                            // 请求
                            doRequest(((Request) s).n);
                        } else if (s == Send.Instance) {
                            // 发送
                            doSend();
                        } else if (s == Cancel.Instance) {
                            // 取消
                            doCancel();
                        } else if (s == Subscribe.Instance) {
                            // 订阅
                            doSubscribe();
                        }
                    }
                } finally {
                    // 保证与下一个线程调度的happens-before 关系
                    on.set(false);
                    // 如果还有信号要处理
                    if (!inboundSignals.isEmpty()) {
                        // 调度当前线程进行处理
                        tryScheduleToExecute();
                    }
                }


            }
        }

        // 该方法确保订阅票据同一个时间在同一个线程运行
        private final void tryScheduleToExecute() {
            // CAS原子性地设置on的值为true,表示已经有一个线程正在处理了
            if (on.compareAndSet(false, true)) {
                try {
                    // 向线程提交任务运行
                    executor.execute(this);
                } catch (Throwable t) {
                    if (!cancelled) {
                    // 首先,错误不可恢复, 先取消订阅
                        doCancel();
                        try {
                            // 停止
                            terminateDueTo(new IllegalArgumentException("Publisher terminated due to unavailable executor", t));
                        } finally {
                            // 后续的入站信号不需要处理了,清空信号
                            inboundSignals.clear();
                            // 取消当前订阅票据, 但是让该票据处于可调度状态,以防止清空入站信号之后又有入站信号加入
                            on.set(false);
                        }

                    }
                }
            }

        }

        /**
         * Subscription.request的实现, 接收订阅者的请求给Subscription 等待处理
         */
        @Override
        public void request(final long l) {
            signal(new Request(l));
        }

        /**
         * 订阅者取消订阅
         */
        public void cancel() {
            signal(Cancel.Instance);
        }

        // init方法的设置 用于确保SubscriptionImpl 实例在暴露给线程池之前已经构造完成
        // 因此,在构造器一完成,就调用该方法, 仅调用一次
        // 先发个信号试一下
        void init() {
            signal(Subscribe.Instance);
        }

    }

}