package com.github.mbmll.concept.web;

/**
 * 用于标记web的响应数据, 有此标记的类, 会被自动包装换成响应数据
 * <code>
 * <br>
 * public class ResponseData&lt;T&gt; {
 * <br>
 * // 实现此接口的数据将被放到这里
 * <br>
 * private T data;
 * <br>
 * }
 * </code>
 */
public interface ResponseData {

}
