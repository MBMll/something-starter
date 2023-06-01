package org.github.mbmll.starters.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/30 0:58
 */

@Data
public class I18nException extends RuntimeException {

    /**
     * args
     */
    private Map<String, Object> args = new LinkedHashMap<>();

    /**
     * init.
     *
     * @param message unchecked
     */
    public I18nException(String message) throws IllegalArgumentException {
        super(message);
        validateMessage(message);
    }

    /**
     * init.
     *
     * @param message unchecked
     * @param args    args
     * @throws IllegalArgumentException
     */
    public I18nException(String message, String... args) throws IllegalArgumentException {
        super(message);
        validateMessage(message);
        params(args);
    }

    /**
     * init.
     *
     * @param message unchecked
     * @param cause   trace
     * @throws IllegalArgumentException
     */
    public I18nException(String message, Throwable cause) throws IllegalArgumentException {
        super(message, cause);
        validateMessage(message);
    }

    /**
     * init.
     *
     * @param message            unchecked
     * @param cause              trace
     * @param enableSuppression  unhandled
     * @param writableStackTrace unhandled
     * @throws IllegalArgumentException
     */
    public I18nException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) throws IllegalArgumentException {
        super(message, cause, enableSuppression, writableStackTrace);
        validateMessage(message);
    }

    /**
     * check message.
     *
     * @param message unchecked
     * @return checked
     * @throws IllegalArgumentException message
     */
    private static String validateMessage(String message) throws IllegalArgumentException {
        Assert.hasText(message, "unkonwn");
        return message;
    }

    /**
     * build params.
     *
     * @param args args
     * @return this
     */
    private I18nException params(String... args) {
        if (!ArrayUtils.isEmpty(args)) {
            Map<String, Object> builder = new LinkedHashMap<>();
            for (int i = 0; i < args.length; i++) {
                builder.put(String.valueOf(i), args[i]);
            }
            this.args = builder;
        }
        return this;
    }

    /**
     * build params.
     *
     * @param key   key
     * @param value value
     * @return this
     */
    public I18nException param(String key, Object value) {
        args.put(key, value);
        return this;
    }
}
