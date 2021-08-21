package yy.springframework.aop.aspectj.annotation;

import yy.springframework.aop.Pointcut;
import yy.springframework.aop.PointcutAdvisor;
import yy.springframework.aop.aspectj.AspectJExpressionPointcut;
import yy.springframework.aopalliance.aop.Advice;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/21 9:38 下午 <br>
 * @see yy.springframework.aop.aspectj.annotation <br>
 */
public class InstantiationModelAwarePointcutAdvisorImpl implements PointcutAdvisor, Serializable {

    private BeanFactoryAspectJAdvisorsBuilder advisorsBuilder;

    private Method adviceMethod;

    private AspectJExpressionPointcut expressionPointcut;

    private String methodName;

    private Advice advice;

    private final String aspectName;

    public InstantiationModelAwarePointcutAdvisorImpl(String aspectName, Method adviceMethod, AspectJExpressionPointcut expressionPointcut, BeanFactoryAspectJAdvisorsBuilder advisorsBuilder) {
        this.adviceMethod = adviceMethod;
        this.expressionPointcut = expressionPointcut;
        this.methodName = adviceMethod.getName();
        this.aspectName = aspectName;
        this.advisorsBuilder = advisorsBuilder;
        initAdvice();
    }

    private void initAdvice() {
        this.advice = this.advisorsBuilder.getAdvice(aspectName, adviceMethod, expressionPointcut);
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public Pointcut getPointcut() {
        return this.expressionPointcut;
    }
}
