package yy.springframework.aop.framework;

import org.springframework.util.ReflectionUtils;
import yy.springframework.aopalliance.intercept.MethodInterceptor;
import yy.springframework.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/21 11:51 下午 <br>
 * @see yy.springframework.aop.framework <br>
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    private Object target;

    private Method targetMethod;

    private Object[] args;

    private List<MethodInterceptor> chain;

    private int currentInterceptorIndex = -1;

    public ReflectiveMethodInvocation(Object target, Method targetMethod, Object[] args, List<MethodInterceptor> chain) {
        this.target = target;
        this.targetMethod = targetMethod;
        this.args = args;
        this.chain = chain;
    }

    @Override
    public Object proceed() throws Throwable {

        if (currentInterceptorIndex == chain.size() - 1) {
            ReflectionUtils.makeAccessible(targetMethod);
            return targetMethod.invoke(target, args);
        }

        MethodInterceptor currentAdvice = chain.get(++this.currentInterceptorIndex);

        return currentAdvice.invoke(this);
    }

    @Override
    public Object[] getArguments() {
        return this.args;
    }

    @Override
    public Method getMethod() {
        return this.targetMethod;
    }
}
