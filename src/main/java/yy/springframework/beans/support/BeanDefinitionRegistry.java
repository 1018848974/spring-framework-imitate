package yy.springframework.beans.support;

import yy.springframework.beans.factory.config.BeanDefinition;
import yy.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.Set;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws NoSuchBeanDefinitionException;

    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    boolean containsBeanDefinition(String beanName);

    Set<String> getBeanNames();

}
