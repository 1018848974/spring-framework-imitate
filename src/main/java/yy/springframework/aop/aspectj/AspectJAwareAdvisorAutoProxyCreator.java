package yy.springframework.aop.aspectj;

import yy.springframework.aop.Advisor;
import yy.springframework.aop.aspectj.annotation.BeanFactoryAspectJAdvisorsBuilder;
import yy.springframework.aop.framework.AdvisedSupport;
import yy.springframework.aop.framework.CglibAopProxy;
import yy.springframework.aop.framework.TargetSource;
import yy.springframework.beans.factory.BeanFactory;
import yy.springframework.beans.factory.BeanFactoryAware;
import yy.springframework.beans.factory.BeansException;
import yy.springframework.beans.factory.ListableBeanFactory;
import yy.springframework.beans.factory.config.BeanPostProcessor;
import yy.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import yy.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/21 7:15 下午 <br>
 * @see yy.springframework.aop.aspectj <br>
 */
public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private BeanFactoryAspectJAdvisorsBuilder advisorsBuilder;

    private ListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (advisorsBuilder != null) {
            advisorsBuilder.buildAllAspectJAdvisor();
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return wrapIfNecessary(bean, beanName);
    }

    private Object wrapIfNecessary(Object bean, String beanName) {

        List<Advisor> eligibleAdvisors = advisorsBuilder.findEligibleAdvisors(bean.getClass());

        if (!CollectionUtils.isEmpty(eligibleAdvisors)) {
            return createProxy(bean, beanName, eligibleAdvisors);
        }

        return bean;
    }

    private Object createProxy(Object bean, String beanName, List<Advisor> eligibleAdvisors) {
        AdvisedSupport advisedSupport = new AdvisedSupport(new TargetSource(bean), eligibleAdvisors);
        return new CglibAopProxy(advisedSupport).getProxy();
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return wrapIfNecessary(bean, beanName);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
        advisorsBuilder = new BeanFactoryAspectJAdvisorsBuilder(this.beanFactory);
    }
}
