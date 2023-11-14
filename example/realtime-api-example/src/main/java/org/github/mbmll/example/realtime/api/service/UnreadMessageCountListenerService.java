package org.github.mbmll.example.realtime.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.support.TaskUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UnreadMessageCountListenerService {

    private final ApplicationEventPublisher eventPublisher;

    public UnreadMessageCountListenerService(ApplicationEventPublisher publisher) {
        eventPublisher = publisher;
    }

    @Bean
    ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        eventMulticaster.setErrorHandler(TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER);
        return eventMulticaster;
    }

    public void start(String userId) {
        eventPublisher.publishEvent(new UnreadMessageCountChangeEvent(this));
    }
}
