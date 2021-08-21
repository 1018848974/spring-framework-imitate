package yy.springframework.beans.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import yy.springframework.beans.BeanCreationException;
import yy.springframework.beans.factory.config.BeanDefinition;
import yy.springframework.beans.factory.config.BeanPostProcessor;
import yy.springframework.beans.factory.config.BeanWrapper;
import yy.springframework.beans.factory.annotation.Autowired;
import yy.springframework.beans.factory.annotation.InjectedMetadata;
import yy.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import yy.springframework.util.BeanUtils;
import yy.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/14 6:36 下午 <br>
 * @see yy.springframework.beans.factory <br>
 */
public abstract class AbstractBeanFactory implements BeanFactory, ListableBeanFactory {

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

        Object instance = getSingleton(beanName, true);

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

        singletonFactories.put(beanName, () -> getEarlyBeanReference(beanWrapper));

        //2。属性注入
        populateBean(instance);

        //3 后置处理器增强 AOP
        instance = initializeBean(instance, beanName);

        //检查早期引用有没有它 不一样的话就是冲突了
        Object earlyReference = getSingleton(beanName, false);

        if (earlyReference != null && instance != earlyReference) {
            throw new BeanCreationException("bean conflict " + beanName);
        }

        return instance;
    }

    private Object getEarlyBeanReference(BeanWrapper beanWrapper) {
        Object exposedBean = beanWrapper.getInstance();

        for (SmartInstantiationAwareBeanPostProcessor processor : getBeanPostProcessors().stream()
                .filter(pp -> pp instanceof SmartInstantiationAwareBeanPostProcessor)
                .map(pp -> (SmartInstantiationAwareBeanPostProcessor) pp).collect(Collectors.toList())) {
            Object earlyBeanReference = processor.getEarlyBeanReference(beanWrapper.getInstance(), beanWrapper.getBeanName());
            if (earlyBeanReference != null) {
                exposedBean = earlyBeanReference;
            }
        }

        return exposedBean;
    }

    private Object initializeBean(Object instance, String beanName) {
        //初始化bean  可以执行初始化前置处理 aware bean装配 等等
        Object bean = instance;

        invokeAwareMethod(instance);

        bean = applyBeanPostProcessorBeforeInitialization(bean, beanName);

        //执行初始化后置处理
        bean = applyBeanPostProcessorAfterInitialization(bean, beanName);

        return bean;
    }

    private void invokeAwareMethod(Object instance) {
        if (instance instanceof BeanFactoryAware) {
            ((BeanFactoryAware) instance).setBeanFactory(this);
        }
    }

    private Object applyBeanPostProcessorAfterInitialization(Object bean, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            if (current != null) {
                bean = current;
            }
        }
        return bean;
    }

    private Object applyBeanPostProcessorBeforeInitialization(Object bean, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            if (current != null) {
                bean = current;
            }
        }
        return bean;
    }

    private BeanWrapper getBeanInstanceWrapper(String beanName, BeanDefinition bd) {
        //1. 实例化对象
        try {
            Object instance = BeanUtils.instantiateClass(bd.getBeanClass());
            return new BeanWrapper(bd.getBeanClass(), instance, beanName);
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

    private Object getSingleton(String beanName, boolean allowEarlyReference) {
        Object instance = singletonObjects.get(beanName);
        if (instance == null) {
            instance = earlySingletonObjects.get(beanName);
            if (instance == null && allowEarlyReference) {
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
        return (T) getBean(name);
    }

    @Override
    public Class<?> getType(String beanName) {
        Object singleton = getSingleton(beanName, false);
        if (singleton != null) {
            return singleton.getClass();
        }
        BeanDefinition definition = getBeanDefinition(beanName);
        if (definition != null) {
            return definition.getBeanClass();
        }
        return null;
    }

    public abstract BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
}
