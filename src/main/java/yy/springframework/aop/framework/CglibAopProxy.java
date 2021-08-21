package yy.springframework.aop.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.*;
import yy.springframework.aop.Advisor;
import yy.springframework.aop.Pointcut;
import yy.springframework.aop.PointcutAdvisor;
import yy.springframework.util.ClassUtils;
import yy.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/21 4:49 下午 <br>
 * @see yy.springframework.aop.framework <br>
 */
public class CglibAopProxy implements AopProxy {

    protected static final Log logger = LogFactory.getLog(CglibAopProxy.class);

    private final AdvisedSupport advised;

    public CglibAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        ClassLoader classLoader = Optional.ofNullable(Thread.currentThread().getContextClassLoader()).orElse(ClassUtils.getDefaultClassLoader());

        Class<?> rootClass = this.advised.getTargetClass();

        Enhancer enhancer = new Enhancer();

        enhancer.setClassLoader(classLoader);

        enhancer.setSuperclass(rootClass);
        // enhancer.setInterfaces(rootClass.getInterfaces());
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        //enhancer.setStrategy(new ClassLoaderAwareGeneratorStrategy(classLoader));

        Callback[] callbacks = getCallbacks(rootClass);
        Class<?>[] types = new Class<?>[callbacks.length];
        for (int x = 0; x < types.length; x++) {
            types[x] = callbacks[x].getClass();
        }
        enhancer.setCallbackTypes(types);
        enhancer.setCallbackFilter(new ProxyCallbackFilter(this.advised));
        enhancer.setInterceptDuringConstruction(false);
        enhancer.setCallbacks(callbacks);

        return enhancer.create();
    }

    private Callback[] getCallbacks(Class<?> rootClass) {
        Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advised);
        return new Callback[]{aopInterceptor};
    }

    private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {

        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            logger.info("===================> 执行了小老弟  " + method.getName());
            System.out.println("执行了小老弟");

            Object target = advised.getTargetSource().getTarget();

            List<yy.springframework.aopalliance.intercept.MethodInterceptor> chain = this.getInterceptors(method);

            if (chain.size() == 0) {
                ReflectionUtils.makeAccessible(method);
                return method.invoke(target, args);
            }

            return new ReflectiveMethodInvocation(target, method, args, chain).proceed();
        }

        private List<yy.springframework.aopalliance.intercept.MethodInterceptor> getInterceptors(Method method) {
            List<yy.springframework.aopalliance.intercept.MethodInterceptor> eligibleAdvisors = new ArrayList<>();
            List<Advisor> advisors = this.advised.getAdvisors();

            for (Advisor advisor : advisors) {
                if (advisor instanceof PointcutAdvisor) {
                    Pointcut pointcut = ((PointcutAdvisor) advisor).getPointcut();
                    if (pointcut.getMethodMatcher().matches(method)) {
                        eligibleAdvisors.add(((yy.springframework.aopalliance.intercept.MethodInterceptor) advisor.getAdvice()));
                    }
                }
            }

            return eligibleAdvisors;
        }
    }

    public static final int AOP_PROXY = 0;

    /**
     * CallbackFilter to assign Callbacks to methods.
     */
    private static class ProxyCallbackFilter implements CallbackFilter {

        private final AdvisedSupport config;

        public ProxyCallbackFilter(AdvisedSupport advised) {
            this.config = advised;

        }

        public int accept(Method method) {
            return AOP_PROXY;
        }

    }
}
