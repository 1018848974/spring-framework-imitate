package yy.springframework.beans.support;

import yy.springframework.beans.factory.config.BeanDefinition;
import yy.springframework.beans.factory.NoSuchBeanDefinitionException;
import yy.springframework.util.ClassUtils;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/11 11:21 下午 <br>
 * @see yy.springframework.beans.support <br>
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    private String id;
    private String beanClassName;
    private Class<?> beanClass;
    private boolean singleton = true;
    private boolean prototype = false;
    private String scope = SCOPE_DEFAULT;

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    @Override
    public boolean isPrototype() {
        return prototype;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public boolean hasConstructorArgumentValues() {
        return false;
    }

    @Override
    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String beanClassName = this.getBeanClassName();
        if (beanClassName == null) {
            return null;
        }
        Class<?> resolvedBeanClass = ClassUtils.forName(beanClassName, classLoader);
        this.beanClass = resolvedBeanClass;
        return resolvedBeanClass;
    }

    @Override
    public Class<?> getBeanClass() {
        Class<?> beanClass = this.beanClass;
        if (beanClass == null) {
            try {
                beanClass = resolveBeanClass(ClassUtils.getDefaultClassLoader());
            } catch (ClassNotFoundException e) {
                throw new NoSuchBeanDefinitionException(this.getBeanClassName());
            }
        }
        return this.beanClass;
    }

    @Override
    public boolean hasBeanClass() {
        return this.beanClass != null;
    }

    @Override
    public Class<?> setBeanClass(Class<?> clazz) {
        return this.beanClass = clazz;
    }
}
