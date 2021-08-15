package yy.springframework.beans.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import yy.springframework.beans.BeanCreationException;
import yy.springframework.beans.BeanInstantiationException;
import yy.springframework.beans.config.BeanDefinition;
import yy.springframework.beans.config.BeanWrapper;
import yy.springframework.beans.factory.annotation.Autowired;
import yy.springframework.beans.factory.annotation.InjectedMetadata;
import yy.springframework.util.BeanUtils;
import yy.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/14 6:36 下午 <br>
 * @see yy.springframework.beans.factory <br>
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    private static final Log logger = LogFactory.getLog(AbstractBeanFactory.class);

    /**
     * 一级缓存
     **/
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * 二级缓存
     **/
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    /**
     * 三级缓存
     **/
    private final Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>(16);

    @Override
    public Object getBean(String beanName) throws BeansException {

        Object instance = getSingleton(beanName);

        if (instance != null) {
            return instance;
        }

        BeanDefinition bd = getBeanDefinition(beanName);

        if (bd == null) {
            return null;
        }

        if (bd.isSingleton()) {

            instance = getSingleton(beanName, () -> {
                return doCreateBean(beanName, bd);
            });

        } else {
            instance = BeanUtils.instantiateClass(bd.getBeanClass());
        }

        return instance;
    }

    private Object getSingleton(String beanName, ObjectFactory<?> objectFactory) {
        synchronized (this.singletonObjects) {
            Object singletonObject = this.singletonObjects.get(beanName);
            if (singletonObject == null) {
                singletonObject = objectFactory.getObject();
                this.singletonObjects.put(beanName, singletonObject);
                this.earlySingletonObjects.remove(beanName);
                this.singletonFactories.remove(beanName);
            }
            return singletonObject;
        }
    }

    private Object doCreateBean(String beanName, BeanDefinition bd) {
        BeanWrapper beanWrapper = getBeanInstanceWrapper(beanName, bd);

        Object instance = beanWrapper.getInstance();

        singletonFactories.put(beanName, () -> instance);

        //2。属性注入
        populateBean(instance);

        //3 后置处理器增强 AOP

        return instance;
    }

    private BeanWrapper getBeanInstanceWrapper(String beanName, BeanDefinition bd) {
        //1. 实例化对象
        try {
            Object instance = BeanUtils.instantiateClass(bd.getBeanClass());
            return new BeanWrapper(bd.getBeanClass(), instance);
        } catch (Exception e) {
            logger.error("instantiateClass failed beanName : " + beanName, e);
            //TODO
            throw new BeansException("instantiateClass failed beanName : " + beanName);
        }
    }

    private void populateBean(Object instance) {
        InjectedMetadata metadata = findAutowiredInjectedMetadata(instance.getClass());
        try {
            metadata.injected(instance);
        } catch (Throwable throwable) {
            throw new BeanCreationException("injection bean field failed ");
        }
    }

    private final Map<String, InjectedMetadata> injectionMetadataCache = new ConcurrentHashMap<>();

    private InjectedMetadata findAutowiredInjectedMetadata(Class<?> clazz) {
        InjectedMetadata metadata = injectionMetadataCache.get(clazz.getName());

        if (metadata != null) {
            return metadata;
        }

        metadata = doCreateInjectionMetadata(clazz);
        injectionMetadataCache.put(clazz.getName(), metadata);

        return metadata;
    }

    private InjectedMetadata doCreateInjectionMetadata(Class<?> clazz) {
        ArrayList<InjectedMetadata.InjectedElement> elements = new ArrayList<>();

        ReflectionUtils.doWithFields(clazz, field -> {
            Annotation[] annotations = field.getDeclaredAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == Autowired.class) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        logger.info("Autowired not support static field : " + field.getName());
                        return;
                    }

                    Autowired ann = (Autowired) annotation;
                    boolean required = ann.required();

                    elements.add(new InjectedMetadata.AutowiredInjectedElement(field, required, this));
                }
            }
        });

        return new InjectedMetadata(clazz, elements);
    }

    private Object getSingleton(String beanName) {
        Object instance = singletonObjects.get(beanName);
        if (instance == null) {
            instance = earlySingletonObjects.get(beanName);
            if ((instance == null)) {
                synchronized (singletonObjects) {
                    instance = singletonObjects.get(beanName);
                    if (instance == null) {
                        instance = earlySingletonObjects.get(beanName);
                    }
                    if (instance == null) {
                        ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
                        if (objectFactory != null) {
                            instance = objectFactory.getObject();
                            earlySingletonObjects.put(beanName, instance);
                            singletonFactories.remove(beanName);
                        }
                    }
                }
            }
        }
        return instance;
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        return (T)getBean(name);
    }

    public abstract BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
}
