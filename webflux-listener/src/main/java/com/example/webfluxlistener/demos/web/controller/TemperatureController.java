package com.example.webfluxlistener.demos.web.controller;

import com.example.webfluxlistener.demos.web.entity.Temperature;
import org.json.JSONObject;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/16 20:56
 * @since: 1.0
 */
@RestController
public class TemperatureController {

    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @RequestMapping("/temperature-stream")
    public SseEmitter events(HttpServletRequest request) {
        // 设置超时时间
//        SseEmitter emitter = new SseEmitter(10000L);
        SseEmitter emitter = new SseEmitter();

        clients.add(emitter);

        emitter.onTimeout(() -> clients.remove(emitter));

        emitter.onCompletion(() -> clients.remove(emitter));

        return emitter;

    }

    @Async
    @EventListener
    public void handleMessage(Temperature temperature) {
        System.out.println("监听到web的调度事件 -- " + temperature.getValue());
        List<SseEmitter> deadEmitters = new ArrayList<>();
        clients.forEach(emitter -> {
            try {
                final JSONObject jsonObject = new JSONObject(temperature);
                final String s1 = jsonObject.toString();
                emitter.send(s1);

            } catch (Exception ignore) {
                deadEmitters.add(emitter);
            }
        });
        clients.removeAll(deadEmitters);

    }

}