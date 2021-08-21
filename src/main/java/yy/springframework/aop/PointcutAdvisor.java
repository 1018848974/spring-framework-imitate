package yy.springframework.aop;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}
