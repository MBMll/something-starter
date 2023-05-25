package org.github.mbmll.starters.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/25 22:28
 */

public class PackingResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return Res.builder().data(body).build();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Class<?> bodyType = returnType.getMethod().getReturnType();
        if (bodyType.isAssignableFrom(ResponseEntity.class) ||
                bodyType.isAssignableFrom(Res.class) ||
                converterType.isInstance(new StringHttpMessageConverter())) {
            return false;
        }
        return true;
    }
}
