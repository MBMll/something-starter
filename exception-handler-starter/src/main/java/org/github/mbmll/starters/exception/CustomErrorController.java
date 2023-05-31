package org.github.mbmll.starters.exception;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 早期代码的实现方式.
 *
 * @Author xlc
 * @Description
 * @Date 2023/5/31 22:48
 */
@Slf4j
@RestController
public class CustomErrorController extends BasicErrorController {

    public CustomErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Override
    @RequestMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        // 获取错误信息
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        String code = body.get("status").toString();
        String message = body.get("message").toString();
        return new ResponseEntity(message, HttpStatus.OK);
    }
}
