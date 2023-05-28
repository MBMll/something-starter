package org.github.mbmll.starters.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
 * 这里必须使用 @Import 导入 {@link I18nUtil},否则 {@link I18nUtil#setMessageSource(MessageSource)} 不会执行.
 *
 * @Author xlc
 * @Description
 * @Date 2023/5/28 14:44
 */
@Configuration
@Import(I18nUtil.class)
public class I18nConfiguration {
    /**
     *
     */
    @Autowired
    I18nProperties properties;

    /**
     * 这里使用 {@link ConfigurationProperties} 减少一个导入设定.
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "i18n")
    public I18nProperties i18nProperties() {
        return new I18nProperties();
    }

    /**
     * {@link ResourceBundleMessageSource#setParentMessageSource(MessageSource)} 在 @Bean 模式下不需要,
     * 但是如果直接调用(没有放到 applicationContext 中), 必须要指定.
     * <p>
     * {@link ResourceBundleMessageSource#setBasename(String)} 传入的参数的 "messages" 指 文件名前缀
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        var messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(properties.getUseCodeAsDefaultMessage());
        messageSource.setDefaultEncoding(properties.getDefaultEncoding());
//        messageSource.setParentMessageSource();
        //设置缓存时间1个小时 1*60*60*1000毫秒
        //可以设置成-1表示永久缓存，设置成0表示每次都从文件中读取
        messageSource.setCacheMillis(properties.getCacheMillis());
        return messageSource;
    }
}
