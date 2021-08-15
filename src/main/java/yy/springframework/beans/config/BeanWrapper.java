package yy.springframework.beans.config;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/15 2:34 下午 <br>
 * @see yy.springframework.beans.config <br>
 */
public class BeanWrapper {

    private final Class<?> instanceClass;

    private final Object instance;

    public BeanWrapper(Class<?> instanceClass, Object instance) {
        this.instanceClass = instanceClass;
        this.instance = instance;
    }

    public Class<?> getInstanceClass() {
        return instanceClass;
    }

    public Object getInstance() {
        return instance;
    }
}
