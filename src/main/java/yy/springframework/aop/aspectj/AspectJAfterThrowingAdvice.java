package yy.springframework.aop.aspectj;

import yy.springframework.aopalliance.intercept.MethodInvocation;
import yy.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/21 6:19 下午 <br>
 * @see yy.springframework.aop.aspectj <br>
 */
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice {

    public AspectJAfterThrowingAdvice(String aspectName, BeanFactory beanFactory, Method adviceMethod) {
        super(aspectName, beanFactory, adviceMethod);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable e) {
            invokeAdviceMethod(new Object[]{e});
            throw e;
        }

    }

}
