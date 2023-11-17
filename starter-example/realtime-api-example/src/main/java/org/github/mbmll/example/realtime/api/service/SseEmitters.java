package org.github.mbmll.example.realtime.api.service;

import com.github.mbmll.concept.ExceptionConsumer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
public class SseEmitters {
    private static final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public static SseEmitter subscribe(String key) {
        SseEmitter emitter = new SseEmitter();

        emitter.onCompletion(() -> {
            log.info("Emitter completed: {}", emitter);
            emitters.remove(key);
        });
        emitter.onTimeout(() -> {
            log.info("Emitter timed out: {}", emitter);
            emitter.complete();
            emitters.remove(key);
        });
        return emitter;
    }

    public static void send(String key, ExceptionConsumer<SseEmitter, Exception> consumer) {
        SseEmitter emitter = emitters.get(key);
        if (emitter == null) {
            subscribe(key);
        }
        emitter = emitters.get(key);
        if (emitter != null) {
            try {
                consumer.accept(emitter);
            } catch (Exception e) {
                emitter.completeWithError(e);
                emitters.remove(key);
                log.error("SseEmitter failed!", e);
            }
        }
    }
}
