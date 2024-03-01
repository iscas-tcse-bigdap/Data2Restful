package com.iscas.apiservice.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author wbq
 * @version 1.0
 * @title LoginService
 * @description 针对类似如下情况：
 * 该类继承了一个第三方框架，在执行的过程中，它是被框架内部创建实例然后去调用的，这就导致了可能在内部new过这个对象了，所以就导致了@Component对这个类根本不起作用。
 * 导致@Autowire注解无法自动注入
 * 此时需要通过类的class从容器中手动获取对象
 * @create 2023/10/7 15:29
 */
@Component
public final class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext app) throws BeansException {
        if (applicationContext == null) {
            applicationContext = app;
        }
    }

    /**
     * 通过类的class从容器中手动获取对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
