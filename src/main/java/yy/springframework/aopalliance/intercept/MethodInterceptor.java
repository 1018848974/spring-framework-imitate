package yy.springframework.aopalliance.intercept;

@FunctionalInterface
public interface MethodInterceptor extends Interceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;

}
