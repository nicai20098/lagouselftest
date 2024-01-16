package com.jiabb;


import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/16 22:47
 * @since: 1.0
 */
public class Main {

    public static void main(String[] args) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                for (int i = 0; i < 10; i++) {
                    emitter.onNext("rx3 -- " + i);
                }
                emitter.onComplete();
            }
        }).subscribe(
                item -> System.out.println("下一个元素是:" + item),
                ex -> System.err.println("异常信息:" + ex.getMessage()),
                () -> System.out.println("响应式流结束"));


        Observable.just("1", "2", "3", "4", " 5").subscribe(
                item -> System.out.println("下一个元素是:" + item),
                ex -> System.err.println("异常信息:" + ex.getMessage()),
                () -> System.out.println("响应式流结束"));
    }

}