package yy.springframework.aop.aspectj;

import yy.springframework.aopalliance.intercept.MethodInvocation;
import yy.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/21 4:06 下午 <br>
 * @see yy.springframework.aop.aspectj <br>
 */
public class AspectJBeforeAdvice extends AbstractAspectJAdvice {

    public AspectJBeforeAdvice(String aspectName, BeanFactory beanFactory, Method adviceMethod) {
        super(aspectName, beanFactory, adviceMethod);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        invokeAdviceMethod(null);
        return invocation.proceed();
    }
}
