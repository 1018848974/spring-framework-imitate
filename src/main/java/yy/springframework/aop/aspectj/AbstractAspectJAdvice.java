package yy.springframework.aop.aspectj;

import org.springframework.util.ReflectionUtils;
import yy.springframework.aopalliance.aop.Advice;
import yy.springframework.aopalliance.intercept.MethodInterceptor;
import yy.springframework.beans.factory.BeanFactory;
import yy.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.lang.reflect.Method;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/21 4:09 下午 <br>
 * @see yy.springframework.aop.aspectj <br>
 */
public abstract class AbstractAspectJAdvice implements Advice, MethodInterceptor {

    private final String aspectName;

    private final BeanFactory beanFactory;

    private final Method adviceMethod;

    private AspectJExpressionPointcut pointcut;

    public AbstractAspectJAdvice(String aspectName, BeanFactory beanFactory, Method adviceMethod) {
        this.aspectName = aspectName;
        this.beanFactory = beanFactory;
        this.adviceMethod = adviceMethod;
    }

    protected void invokeAdviceMethod(Object[] args) throws Throwable {
        Object bean = beanFactory.getBean(aspectName);
        if (bean == null) {
            throw new NoSuchBeanDefinitionException(aspectName);
        }

        ReflectionUtils.makeAccessible(this.adviceMethod);
        if (args != null) {
            adviceMethod.invoke(bean, args);
        } else {
            adviceMethod.invoke(bean);
        }

    }

    public AspectJExpressionPointcut getPointcut() {
        return pointcut;
    }

    public void setPointcut(AspectJExpressionPointcut pointcut) {
        this.pointcut = pointcut;
    }
}
