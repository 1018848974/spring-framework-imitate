package yy.springframework.beans.factory;

import yy.springframework.beans.support.BeanDefinitionRegistry;

public interface BeanDefinitionRegistryPostProcessor {

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;

}
