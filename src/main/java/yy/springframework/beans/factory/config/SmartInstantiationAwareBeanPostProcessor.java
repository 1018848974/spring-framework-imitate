package yy.springframework.beans.factory.config;

import yy.springframework.beans.factory.BeansException;

public interface SmartInstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
