package org.github.mbmll.starters.i18n;

import java.nio.charset.Charset;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * 原本打算写一个 {@link LocaleResolver}, 然后 通过继承 {@link WebMvcConfigurer} 添加 一个 {@link LocaleChangeInterceptor},
 * 但是实际尝试后发现 spring 已经默认添加了 一个 {@link AcceptHeaderLocaleResolver}, 所以这块去掉了.
 * <p>
 * {@link I18nUtil} 没有使用 @Component 注解, 这里必须使用 @Import 导入 {@link I18nUtil},否则 {@link I18nUtil#setMessageSource(MessageSource)} 不会执行.
 *
 * @Author xlc
 * @Description
 * @Date 2023/5/28 14:44
 */
@Configuration
@Import(I18nUtil.class)
public class I18nConfiguration {

    /**
     * {@link ResourceBundleMessageSource#setParentMessageSource(MessageSource)} 在 @Bean 模式下不需要,
     * 但是如果直接调用(没有放到 applicationContext 中), 必须要指定.
     * <p>
     * {@link ResourceBundleMessageSource#setBasename(String)} 传入的参数的 "messages" 指 文件名前缀
     * <p>
     * 如果在配置文件定义了 spring.messages 的配置, 就会创建一个 messageSource, 这里作为默认的使用.
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MessageSource.class)
    public MessageSource messageSource() {
        var messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding(Charset.defaultCharset().toString());
//        messageSource.setParentMessageSource();
        //设置缓存时间1个小时 1*60*60*1000毫秒
        //可以设置成-1表示永久缓存，设置成0表示每次都从文件中读取
        messageSource.setCacheMillis(1 * 60 * 60 * 1000);
        return messageSource;
    }
}
