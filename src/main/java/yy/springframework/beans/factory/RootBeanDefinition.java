package yy.springframework.beans.factory;

import yy.springframework.beans.support.AbstractBeanDefinition;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/14 1:21 下午 <br>
 * @see yy.springframework.beans.factory <br>
 */
public class RootBeanDefinition extends AbstractBeanDefinition {

    public RootBeanDefinition(Class<?> clazz) {
        setBeanClass(clazz);
    }
}
