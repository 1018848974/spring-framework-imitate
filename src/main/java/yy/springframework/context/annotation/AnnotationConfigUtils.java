package yy.springframework.context.annotation;

import yy.springframework.beans.factory.RootBeanDefinition;
import yy.springframework.beans.support.BeanDefinitionRegistry;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/14 1:07 下午 <br>
 * @see yy.springframework.context.annotation <br>
 */
public class AnnotationConfigUtils {

    public static final String CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME = "yy.springframework.context.annotation.configurationAnnotationProcessor";

    public static void registerAnnotationConfigurationProcessor(BeanDefinitionRegistry registry) {
        //注册内部的配置解析处理器
        if (!registry.containsBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            RootBeanDefinition bd = new RootBeanDefinition(ConfigurationPostProcessor.class);
            registry.registerBeanDefinition(CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME, bd);
        }

    }

}
