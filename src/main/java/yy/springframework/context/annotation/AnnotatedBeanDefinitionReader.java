package yy.springframework.context.annotation;

import yy.springframework.beans.factory.RootBeanDefinition;
import yy.springframework.beans.support.BeanDefinitionRegistry;
import yy.springframework.util.ClassUtils;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/10 9:49 下午 <br>
 * @see yy.springframework.context.annotation <br>
 */
public class AnnotatedBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
        AnnotationConfigUtils.registerAnnotationConfigurationProcessor(registry);
    }

    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            doRegisterBean(componentClass);
        }
    }

    private void doRegisterBean(Class<?> componentClass) {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(componentClass);
        this.registry.registerBeanDefinition(ClassUtils.getShortName(componentClass), rootBeanDefinition);
    }
}
