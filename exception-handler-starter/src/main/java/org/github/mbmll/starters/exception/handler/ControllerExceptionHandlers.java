package org.github.mbmll.starters.exception.handler;

import org.github.mbmll.starters.exception.I18nException;
import org.github.mbmll.starters.response.Res;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author xlc
 * @Description encapsulate the return value of api.
 * @Date 2023/5/25 22:28
 */
@RestControllerAdvice
public class ControllerExceptionHandlers {

    @ExceptionHandler(I18nException.class)
    public Res<Object> i18n(I18nException e) {
        return Res.builder().data(e.getMessage()).build();
    }
}
