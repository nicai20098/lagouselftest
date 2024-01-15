package com.jiabb.webflux.demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description: 基于Executor的异步运行的订阅者实现, 一次请求一个元素, 然后对每个元素调用用户定义的方法进行处理
 * 注意: 该类中使用了很多try-catch 用于说明什么时候可以抛异常, 什么时候不可以抛异常
 * @author: jia_b
 * @date: 2024/1/15 20:36
 * @since: 1.0
 */
public abstract class AsyncSubscriber<T> implements Subscriber<T>, Runnable {

    // Signal表示发布者和订阅者之间的异步协议
    private static interface Signal {

    }

    // 表示数据流发送完成, 完成信号
    private enum OnComplete implements Signal {
        Instance;
    }

    // 表示发布者给订阅者的异常信号
    private static class OnError implements Signal {
        public final Throwable error;

        public OnError(final Throwable error) {
            this.error = error;
        }

    }

    private static class OnNext<T> implements Signal {
        public final T next;
        public OnNext(T t) {
            this.next = t;
        }

    }

    private static class OnSubscribe implements Signal {
        public final Subscription subscription;
        public OnSubscribe(Subscription t) {
            this.subscription = t;
        }
    }

    // 订阅单据
    private Subscription subscription;
    // 用于表示当前的订阅是否处理完成
    private boolean done;
    // 使用该线程异步处理各个信号
    private final Executor executor;

    /**
     * 仅有一个构造器 只能被子类使用
     */
    protected AsyncSubscriber (Executor executor) {
        if (executor == null) throw null;
        this.executor = executor;
    }

    /**
     * 幂等地标记当前订阅者已完成处理,不在处理更多的元素
     * 因此 需要取消订阅票据subscription
     */
    private final void done() {
        // 在此处 可以添加done 对订阅者的完成状态进行设置
        // 当whenNext方法抛异常, 认为订阅者已经完成处理(不再接收更多元素)
        done = true;
        if (subscription != null) {
            try {
                subscription.cancel();
            } catch (final Throwable t) {
                (new IllegalArgumentException(subscription + "violated the Reactive Streams rule 3.15 by throwing an exception from cancel", t)).printStackTrace(System.err);
            }
        }

    }

    protected abstract boolean whenNext(final T element);

    protected void whenComplete() {
    }

    protected void whenError(Throwable error) {
    }

    private final void handleOnSubscribe(final Subscription s) {
        if (s == null) {

        } else if (subscription != null) {
            try {
                s.cancel();
            } catch (final Throwable t) {
                (new IllegalArgumentException(s + "violated the Reactive Streams rule 3.15 by throwing an exception from cancel", t)).printStackTrace(System.err);
            }
        } else {
            subscription = s;
            try {
                s.request(1);
            } catch (final Throwable t) {
                (new IllegalArgumentException(s + "violated the Reactive Streams rule 3.15 by throwing an exception from cancel", t)).printStackTrace(System.err);
            }
        }
    }

    private final void handleOnNext(final T element) {
        if (!done) {
            if (subscription == null) {
                (new IllegalArgumentException("Someone violated the Reactive Streams rule 1.09 and 2.1 by signaling onNext before `Subscription.request`.")).printStackTrace(System.err);
            } else {
                try {
                    if (whenNext(element)) {
                        try {
                            subscription.request(1);
                        } catch (final Throwable t) {
                            (new IllegalArgumentException( subscription + " violated the Reactive Streams rule 3.16 by throwing an exception from request`.")).printStackTrace(System.err);
                        }
                    } else {
                        done();
                    }
                } catch (final Throwable t) {
                    done();
                    try {
                        onError(t);
                    } catch (final Throwable t2) {
                        (new IllegalArgumentException( this + " violated the Reactive Streams rule 2.13 by throwing an exception from error`.")).printStackTrace(System.err);
                    }
                }
            }
        }
    }

    private void handleOncomplete() {
        if (subscription == null) {
            (new IllegalArgumentException("Publisher violated the Reactive Streams rule 1.09 signaling onComplete prior to onSubscribe.")).printStackTrace(System.err);
        } else {
            done = true;
            whenComplete();
        }
    }

    private void handleOnError(final Throwable error) {
        if (subscription == null) {
            (new IllegalArgumentException("Publisher violated the Reactive Streams rule 1.09 signaling onError prior to onSubscribe.")).printStackTrace(System.err);
        } else {
            done = true;
            whenError(error);
        }
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (s == null) throw null;
        signal(new OnSubscribe(s));
    }

    @Override
    public void onNext(T t) {
        if (t == null) throw null;
        signal(new OnNext<T>(t));
    }

    @Override
    public void onError(Throwable t) {
        if (t == null) throw null;
        signal(new OnError(t));
    }


    @Override
    public void onComplete() {
        signal(OnComplete.Instance);
    }

    private final ConcurrentLinkedDeque<Signal> inboundSignals = new ConcurrentLinkedDeque<Signal>();


    private final AtomicBoolean on = new AtomicBoolean(false);

    @Override
    public final void run() {
        // 跟上次线程执行建立happens-before关系, 防止多个线程并发执行
        if (on.get()) {
            try {
                // 从入站队列取出信号
                final Signal s = inboundSignals.poll();
                if (!done) {
                    if (s instanceof OnNext<?>) {
                        handleOnNext(((OnNext<T>)s ).next);
                    } else if (s instanceof OnSubscribe) {
                        handleOnSubscribe(((OnSubscribe)s).subscription);
                    } else if (s instanceof OnError) {
                        handleOnError(((OnError)s).error);
                    } else if (s == OnComplete.Instance) {
                        handleOncomplete();
                    }
                }
            } finally {
                // 保持happens-before 关系 然后开始下一个线程调度执行
                on.set(false);
                // 如果入站信号不是空的, 调度线程处理入站信号
                if (!inboundSignals.isEmpty()) {
                    // 调度当前线程进行处理
                    tryScheduleToExecute();
                }
            }
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

    // 该方法确保订阅票据同一个时间在同一个线程运行
    private final void tryScheduleToExecute() {
        // CAS原子性地设置on的值为true,表示已经有一个线程正在处理了
        if (on.compareAndSet(false, true)) {
            try {
                // 向线程提交任务运行
                executor.execute(this);
            } catch (Throwable t) {
                if (!done) {
                    try {
                        // 首先,错误不可恢复, 先取消订阅
                        done();
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

}