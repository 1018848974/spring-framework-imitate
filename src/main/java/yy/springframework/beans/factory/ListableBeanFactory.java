package yy.springframework.beans.factory;

import java.util.List;
import java.util.Set;

public interface ListableBeanFactory extends BeanFactory{

    Set<String> getBeanNames();

   <A> List<? extends A> getBeansByType(Class<A> beanDefinitionRegistryPostProcessorClass);

   String[] getBeanNameByType(Class<?> clazz);

    void initializeAllSingletonBean();
}
