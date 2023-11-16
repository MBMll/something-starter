package org.github.mbmll.example.realtime.api.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author xlc
 * @Description
 * @Date 2023/11/17 00:49:29
 */
@RestController
public class SseEmitterController {

    @GetMapping(value = "test/{clientId}", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter test(@PathVariable String clientId) {
        SseEmitter emitter = SseEmitters.subscribe(clientId);
        return emitter;
    }
}
