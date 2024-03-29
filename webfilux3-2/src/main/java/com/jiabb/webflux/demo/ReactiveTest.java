package com.jiabb.webflux.demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: 测试类
 * @author: jia_b
 * @date: 2024/1/15 23:33
 * @since: 1.0
 */
public class ReactiveTest {

    public static void main(String[] args) {

        Set<Integer> elements = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            elements.add(i);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        AsyncIterablePublisher<Integer> publisher = new AsyncIterablePublisher<>(elements, executorService);
        AsyncSubscriber<Integer> subscriber = new AsyncSubscriber<Integer>(Executors.newFixedThreadPool(2)) {
            @Override
            protected boolean whenNext(Integer element) {
                System.out.println("接收到的流元素" + element);
                return true;
            }
        };
        publisher.subscribe(subscriber);


    }

}