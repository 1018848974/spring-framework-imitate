package yy.springframework.beans.factory;

import yy.springframework.beans.factory.config.BeanPostProcessor;

import java.util.List;
import java.util.Set;

public interface ListableBeanFactory extends BeanFactory {

    Set<String> getBeanNames();

    <A> List<? extends A> getBeansByType(Class<A> beanDefinitionRegistryPostProcessorClass);

    String[] getBeanNameByType(Class<?> clazz);

    void initializeAllSingletonBean();

    public void adBeanPostProcessor(List<BeanPostProcessor> pps);

    List<BeanPostProcessor> getBeanPostProcessors();

    Class<?> getType(String beanName);

}
