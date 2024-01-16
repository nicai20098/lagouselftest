package com.example.webfluxlistener.demos.web.service;

import com.example.webfluxlistener.demos.web.entity.Temperature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/16 20:50
 * @since: 1.0
 */
@Component
public class temperatureService {

    private final ApplicationEventPublisher publisher;

    private final Random random = new Random();

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public temperatureService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    public void startProcessing() {
        this.service.schedule(this::probe, 1, TimeUnit.SECONDS);
    }

    public void probe() {
        double temperature = 16 + random.nextGaussian() * 10;
        System.out.println("发送事件...");
        //通过ApplicationEventPublisher 发布Temperature事件
        publisher.publishEvent(new Temperature(temperature));
        service.schedule(this::probe, random.nextInt(5000), TimeUnit.MILLISECONDS);

    }

}
