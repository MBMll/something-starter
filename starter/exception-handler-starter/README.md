1. [继承 **BasicExceptionController**](./src/main/java/org/github/mbmll/starters/exception/CustomErrorController.java)
2. **@RestController** + **@ExceptionHandler**

```java
@RestController
public class TestController {

    @GetMapping("test9")
    public FundInfo test9() throws Exception {
        throw new Exception("test9 error");
    }

    @GetMapping("test10")
    public FundInfo test10() throws Exception {
        throw new IOException("test10 error");
    }

    @ExceptionHandler(Exception.class)
    public ApiResult exceptionHandler(Exception e) {
        return ApiUtil.custom(500, e.getMessage());
    }
}

```

3. [**@RestControllerAdvice** + **
   @ExceptionHandler**](src/main/java/org/github/mbmll/starters/exception/handler/ControllerExceptionHandlers.java)
4. **SimpleMappingExceptionResolver**

```java
@Configuration
public class GlobalExceptionConfig {

   @Bean
   public SimpleMappingExceptionResolver getSimpleMappingExceptionResolver(){
       SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
       /**
        * 参数一：异常的类型，这里必须要异常类型的全名
        * 参数二：要跳转的视图名称
        */
       Properties mappings = new Properties();
       mappings.put("java.lang.ArithmeticException", "error1");
       mappings.put("java.lang.NullPointerException", "error1");
       mappings.put("java.lang.Exception", "error1");
       mappings.put("java.io.IOException", "error1");
       // 设置异常与视图的映射信息
       resolver.setExceptionMappings(mappings);
       return resolver;
   }
}


```