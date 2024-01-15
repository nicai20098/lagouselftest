package com.jiabb.webflux.demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.tck.SubscriberBlackboxVerification;
import org.reactivestreams.tck.TestEnvironment;

import java.util.concurrent.Executors;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/16 0:07
 * @since: 1.0
 */
public class SubscriberBlackboxTest extends SubscriberBlackboxVerification<Integer> {


    protected SubscriberBlackboxTest() {
        super(new TestEnvironment());
    }

    @Override
    public Subscriber<Integer> createSubscriber() {
        return new AsyncSubscriber<Integer>(Executors.newFixedThreadPool(2)) {
            @Override
            protected boolean whenNext(Integer element) {
                System.out.println("接收到的流元素" + element);
                return true;
            }
        };
    }

    @Override
    public Integer createElement(int i) {
        return i;
    }
}