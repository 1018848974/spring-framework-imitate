package yy.springframework.beans.factory.config;

import yy.springframework.beans.factory.BeansException;

public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
