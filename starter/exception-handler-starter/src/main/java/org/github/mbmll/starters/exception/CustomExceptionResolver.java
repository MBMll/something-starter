package org.github.mbmll.starters.exception;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/31 23:07
 */

public class CustomExceptionResolver extends AbstractHandlerExceptionResolver {
    /**
     * 异常解析器的顺序， 数值越小，表示优先级越高
     *
     * @return
     */
    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                              Exception ex) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            response.getWriter().write(ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
