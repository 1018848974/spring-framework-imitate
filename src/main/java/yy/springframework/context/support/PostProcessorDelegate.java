package yy.springframework.context.support;

import yy.springframework.beans.factory.AbstractBeanFactory;
import yy.springframework.beans.factory.BeanDefinitionRegistryPostProcessor;
import yy.springframework.beans.factory.ListableBeanFactory;
import yy.springframework.beans.factory.config.BeanPostProcessor;
import yy.springframework.beans.support.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/14 1:55 下午 <br>
 * @see yy.springframework.context.support <br>
 */
public class PostProcessorDelegate {

    public static void invokeBeanDefinitionPostProcessors(ListableBeanFactory beanFactory) {
        List<? extends BeanDefinitionRegistryPostProcessor> postProcessors = beanFactory.getBeansByType(BeanDefinitionRegistryPostProcessor.class);
        for (BeanDefinitionRegistryPostProcessor processor : postProcessors) {
            processor.postProcessBeanDefinitionRegistry((BeanDefinitionRegistry) beanFactory);
        }
    }

    public static void registerBeanPostProcessor(ListableBeanFactory beanFactory) {
        String[] bpNames = beanFactory.getBeanNameByType(BeanPostProcessor.class);
        List<BeanPostProcessor> bpBeans = Arrays.stream(bpNames).map(n -> beanFactory.getBean(n, BeanPostProcessor.class)).collect(Collectors.toList());
        beanFactory.adBeanPostProcessor(bpBeans);
    }

}
