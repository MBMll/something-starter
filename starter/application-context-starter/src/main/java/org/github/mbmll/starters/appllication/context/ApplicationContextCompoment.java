package org.github.mbmll.starters.appllication.context;

import java.util.Objects;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

/**
 * @Author xlc
 * @Description
 * @Date 2023/5/28 13:57
 */

public final class ApplicationContextCompoment implements ApplicationContextAware {
    /**
     *
     */
    @Nullable
    private static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> clazz) {
        return Objects.requireNonNull(applicationContext).getBean(clazz);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextCompoment.applicationContext = applicationContext;
    }
}
