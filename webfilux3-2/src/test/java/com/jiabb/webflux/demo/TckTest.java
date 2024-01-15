package com.jiabb.webflux.demo;

import org.reactivestreams.Publisher;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/16 0:00
 * @since: 1.0
 */
public class TckTest extends PublisherVerification<String> {

    public  TckTest () {
        super(new TestEnvironment());
    }

    @Override
    public Publisher<String> createPublisher(long l) {
        Set<String> elements = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            elements.add("hello -" + i);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        return new AsyncIterablePublisher<>(elements, executorService);
    }

    @Override
    public Publisher<String> createFailedPublisher() {
        Set elements = new HashSet<>();
        elements.add(new RuntimeException("手动异常"));
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        return new AsyncIterablePublisher<>(elements, executorService);
    }

    @Override
    public long maxElementsFromPublisher() {
//        return super.maxElementsFromPublisher();
        return 10;
    }
}