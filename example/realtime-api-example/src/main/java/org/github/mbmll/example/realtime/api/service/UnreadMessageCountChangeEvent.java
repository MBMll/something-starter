package org.github.mbmll.example.realtime.api.service;

import org.springframework.context.ApplicationEvent;

/**
 * @Author xlc
 * @Description
 * @Date 2023/11/14 22:29:47
 */

public class UnreadMessageCountChangeEvent extends ApplicationEvent {

    public UnreadMessageCountChangeEvent(Object source) {
        super(source);
    }
}
