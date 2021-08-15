package yy.springframework.context;

import yy.springframework.beans.config.BeanDefinition;
import yy.springframework.beans.factory.BeanFactory;
import yy.springframework.beans.factory.ListableBeanFactory;
import yy.springframework.beans.factory.NoSuchBeanDefinitionException;
import yy.springframework.beans.support.BeanDefinitionRegistry;
import yy.springframework.beans.support.DefaultBeanFactory;
import yy.springframework.context.annotation.AnnotationConfigRegistry;
import yy.springframework.context.support.PostProcessorDelegate;

import java.util.Set;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/10 10:07 下午 <br>
 * @see yy.springframework.context <br>
 */
public abstract class AbstractApplicationContext implements BeanFactory, BeanDefinitionRegistry, AnnotationConfigRegistry {

    private final DefaultBeanFactory beanFactory;

    public AbstractApplicationContext() {
        this.beanFactory = new DefaultBeanFactory();
    }

    public void refresh(){

        ListableBeanFactory beanFactory = this.beanFactory;

        //工厂后置增强 解析配置类扫描所有的BeanDefinition
        PostProcessorDelegate.invokeBeanDefinitionPostProcessors(beanFactory);

        //注册所有单实例Bean
        FinishBeanFactoryInit(beanFactory);

    }

    private void FinishBeanFactoryInit(ListableBeanFactory beanFactory) {
        beanFactory.initializeAllSingletonBean();
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws NoSuchBeanDefinitionException {
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return this.beanFactory.getBeanDefinition(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return false;
    }

    @Override
    public Object getBean(String beanName) {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return this.beanFactory.getBean(name, clazz);
    }

    @Override
    public Set<String> getBeanNames() {
        return beanFactory.getBeanNames();
    }
}
