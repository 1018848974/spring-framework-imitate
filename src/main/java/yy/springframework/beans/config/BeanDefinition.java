package yy.springframework.beans.config;

import java.util.List;

public interface BeanDefinition {

    public static final String SCOPE_SINGLETON = "singleton";
    public static final String SCOPE_PROTOTYPE = "prototype";
    public static final String SCOPE_DEFAULT = "";


    public boolean isSingleton();
    public boolean isPrototype();
    String getScope();
    void setScope(String scope);

    public void setBeanClassName(String beanClassName);

    public String getBeanClassName();

    public boolean hasConstructorArgumentValues();

    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException;
    public Class<?> getBeanClass();
    public Class<?> setBeanClass(Class<?> clazz);
    public boolean hasBeanClass();
}
