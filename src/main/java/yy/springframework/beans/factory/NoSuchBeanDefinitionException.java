package yy.springframework.beans.factory;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/10 9:41 下午 <br>
 * @see yy.springframework.beans.factory <br>
 */
public class NoSuchBeanDefinitionException extends BeansException {

    private final String beanName;

    public NoSuchBeanDefinitionException(String name) {
        super("No bean named '" + name + "' available");
        this.beanName = name;
    }

    public NoSuchBeanDefinitionException(String name, Throwable cause) {
        super("No bean named '" + name + "' available", cause);
        this.beanName = name;
    }

    public String getBeanName() {
        return this.beanName;
    }
}
