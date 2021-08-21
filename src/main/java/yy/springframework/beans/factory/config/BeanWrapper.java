package yy.springframework.beans.factory.config;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/15 2:34 下午 <br>
 * @see yy.springframework.beans.factory.config <br>
 */
public class BeanWrapper {

    private final Class<?> instanceClass;

    private final Object instance;

    private final String beanName;

    public BeanWrapper(Class<?> instanceClass, Object instance, String beanName) {
        this.instanceClass = instanceClass;
        this.instance = instance;
        this.beanName = beanName;
    }

    public Class<?> getInstanceClass() {
        return instanceClass;
    }

    public Object getInstance() {
        return instance;
    }

    public String getBeanName() {
        return this.beanName;
    }
}
