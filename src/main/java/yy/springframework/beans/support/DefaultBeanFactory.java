package yy.springframework.beans.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import yy.springframework.beans.factory.config.BeanDefinition;
import yy.springframework.beans.factory.*;
import yy.springframework.beans.factory.config.BeanPostProcessor;
import yy.springframework.core.io.type.StandardAnnotationMetadata;
import yy.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/11 8:28 下午 <br>
 * @see yy.springframework.beans.support <br>
 */
public class DefaultBeanFactory extends AbstractBeanFactory implements ListableBeanFactory, BeanDefinitionRegistry {

    protected final Log logger = LogFactory.getLog(getClass());

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private final Set<String> beanDefinitionNames = new HashSet<>(256);

    private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap<>();

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws NoSuchBeanDefinitionException {
        this.beanDefinitionMap.put(beanName, beanDefinition);
        this.beanDefinitionNames.add(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionNames.contains(beanName);
    }

    @Override
    public Set<String> getBeanNames() {
        return this.beanDefinitionNames;
    }

    @Override
    public <A> List<? extends A> getBeansByType(Class<A> clazz) {
        List<Object> result = new ArrayList<>();
        String[] beanNameByType = getBeanNameByType(clazz);
        for (String beanName : beanNameByType) {
            Object bean = getBean(beanName);
            if (bean != null) {
                result.add(bean);
            }
        }
        return (List<A>) result;
    }

    @Override
    public String[] getBeanNameByType(Class<?> clazz) {
        String[] beanNames = allBeanNamesByType.get(clazz);
        if (beanNames != null) {
            return beanNames;
        }
        return doGetBeanNameByType(clazz);
    }

    @Override
    public void initializeAllSingletonBean() {
        for (String beanName : getBeanNames()) {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            StandardAnnotationMetadata metadata = new StandardAnnotationMetadata(beanDefinition.getBeanClass());
            if (beanDefinition.isSingleton() && !metadata.isConcrete()) {
                getBean(beanName);
            }
        }
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public void adBeanPostProcessor(List<BeanPostProcessor> pps) {
        this.beanPostProcessors.addAll(pps);
    }

    private String[] doGetBeanNameByType(Class<?> clazz) {
        List<String> result = new ArrayList<>();

        for (String beanName : getBeanNames()) {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            Class<?> beanClass = null;
            beanClass = beanDefinition.getBeanClass();
            if (beanClass != null && !beanClass.isInterface() && (clazz.isAssignableFrom(beanClass) || beanClass == clazz)) {
                result.add(beanName);
            }
        }

        return StringUtils.toStringArray(result);
    }
}
