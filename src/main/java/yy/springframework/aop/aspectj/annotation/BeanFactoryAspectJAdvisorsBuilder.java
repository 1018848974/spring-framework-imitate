package yy.springframework.aop.aspectj.annotation;

import org.aspectj.lang.annotation.*;

import org.springframework.core.annotation.AnnotationUtils;
import yy.springframework.aop.Advisor;
import yy.springframework.aop.MethodMatcher;
import yy.springframework.aop.PointcutAdvisor;
import yy.springframework.aop.aspectj.*;
import yy.springframework.aopalliance.aop.Advice;
import yy.springframework.beans.factory.BeanFactory;
import yy.springframework.beans.factory.ListableBeanFactory;
import yy.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/21 8:02 下午 <br>
 * @see yy.springframework.aop.aspectj.annotation <br>
 */
public class BeanFactoryAspectJAdvisorsBuilder {

    private final Map<String, List<Advisor>> advisorCache = new ConcurrentHashMap<>();

    private final List<Advisor> advisors = new ArrayList<>();

    private static final Class<?>[] ASPECTJ_ANNOTATION_CLASSES = new Class<?>[]{
            Pointcut.class, Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class};

    private final ListableBeanFactory beanFactory;

    public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public List<Advisor> buildAllAspectJAdvisor() {

        if (this.advisors.size() > 0) {
            return this.advisors;
        }

        Set<String> beanNames = beanFactory.getBeanNames();

        for (String beanName : beanNames) {
            Class<?> clazz = beanFactory.getType(beanName);
            if (clazz == null || !isAspect(clazz))
                continue;

            List<Advisor> advisors = getAdvisors(beanName, clazz);
            advisorCache.put(beanName, advisors);
            this.advisors.addAll(advisors);
        }

        return this.advisors;
    }

    public List<Advisor> getAdvisors() {
        return this.advisors;
    }

    private List<Advisor> getAdvisors(String beanName, Class<?> clazz) {
        ArrayList<Method> methods = new ArrayList<>();
        ReflectionUtils.doWithMethods(clazz, methods::add, ReflectionUtils.USER_DECLARED_METHODS);

        ArrayList<Advisor> advisors = new ArrayList<>();

        for (Method method : methods) {
            AspectJAnnotation jAnnotation = findAspectJAnnotationOnMethod(method);
            if (jAnnotation == null) {
                continue;
            }
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(clazz, jAnnotation.pointcutExpression);
            advisors.add(new InstantiationModelAwarePointcutAdvisorImpl(beanName, method, pointcut, this));
        }

        return advisors;
    }

    private AspectJAnnotation findAspectJAnnotationOnMethod(Method method) {
        for (Class<?> ann : ASPECTJ_ANNOTATION_CLASSES) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == ann) {
                    return new AspectJAnnotation(annotation);
                }
            }
        }
        return null;
    }

    boolean isAspect(Class<?> beanType) {
        return AnnotationUtils.findAnnotation(beanType, Aspect.class) != null;
    }

    public Advice getAdvice(String aspectName, Method adviceMethod, AspectJExpressionPointcut expressionPointcut) {

        AspectJAnnotation aspectAnnotation = findAspectJAnnotationOnMethod(adviceMethod);

        AbstractAspectJAdvice abstractAspectJAdvice;

        switch (aspectAnnotation.annotationType) {
            case AtBefore:
                abstractAspectJAdvice = new AspectJBeforeAdvice(aspectName, this.beanFactory, adviceMethod);
                break;
            case AtAfter:
                abstractAspectJAdvice = new AspectJAfterAdvice(aspectName, this.beanFactory, adviceMethod);
                break;
            case AtAfterReturning:
                abstractAspectJAdvice = new AspectJReturningAdvice(aspectName, this.beanFactory, adviceMethod);
                break;
            case AtAfterThrowing:
                abstractAspectJAdvice = new AspectJAfterThrowingAdvice(aspectName, this.beanFactory, adviceMethod);
                break;
            default:
                throw new IllegalArgumentException("dont have this type advice: " + aspectAnnotation.annotationType);
        }

        abstractAspectJAdvice.setPointcut(expressionPointcut);

        return abstractAspectJAdvice;
    }

    public List<Advisor> findEligibleAdvisors(Class<?> beanClass) {
        List<Advisor> eligibleAdvisors = new ArrayList<>();
        List<Advisor> candidateAdvisor = this.getAdvisors();

        for (Advisor advisor : candidateAdvisor) {
            if (advisor instanceof PointcutAdvisor) {
                if (canApply(((PointcutAdvisor) advisor).getPointcut(), beanClass)) {
                    eligibleAdvisors.add(advisor);
                }
            }
        }

        return eligibleAdvisors;
    }

    private boolean canApply(yy.springframework.aop.Pointcut pointcut, Class<?> beanClass) {
        if (!pointcut.getClassFilter().matches(beanClass)) {
            return false;
        }

        MethodMatcher methodMatcher = pointcut.getMethodMatcher();

        Method[] methods = ReflectionUtils.getAllDeclaredMethods(beanClass);

        for (Method method : methods) {
            if (methodMatcher.matches(method)) {
                return true;
            }
        }

        return false;
    }

    private static class AspectJAnnotation<A extends Annotation> {

        private static Map<Class<?>, AspectJAnnotationType> annotationTypeMap = new HashMap<>(8);

        static {
            annotationTypeMap.put(Pointcut.class, AspectJAnnotationType.AtPointcut);
            annotationTypeMap.put(Around.class, AspectJAnnotationType.AtAround);
            annotationTypeMap.put(Before.class, AspectJAnnotationType.AtBefore);
            annotationTypeMap.put(After.class, AspectJAnnotationType.AtAfter);
            annotationTypeMap.put(AfterReturning.class, AspectJAnnotationType.AtAfterReturning);
            annotationTypeMap.put(AfterThrowing.class, AspectJAnnotationType.AtAfterThrowing);
        }

        private final A annotation;

        private final String pointcutExpression;

        private final AspectJAnnotationType annotationType;

        public AspectJAnnotation(A annotation) {
            this.annotation = annotation;
            this.annotationType = annotationTypeMap.get(annotation.annotationType());
            this.pointcutExpression = (String) AnnotationUtils.getValue(annotation, "value");
        }

        public String getPointcutExpression() {
            return pointcutExpression;
        }

        public AspectJAnnotationType getAnnotationType() {
            return annotationType;
        }
    }

    protected enum AspectJAnnotationType {

        AtPointcut, AtAround, AtBefore, AtAfter, AtAfterReturning, AtAfterThrowing
    }

}
