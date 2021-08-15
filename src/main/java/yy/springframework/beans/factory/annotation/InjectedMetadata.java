package yy.springframework.beans.factory.annotation;

import yy.springframework.beans.factory.BeanFactory;
import yy.springframework.beans.factory.NoSuchBeanDefinitionException;
import yy.springframework.util.ClassUtils;
import yy.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/15 1:08 下午 <br>
 * @see yy.springframework.beans.factory.annotation <br>
 */
public class InjectedMetadata {

    private final Class<?> targetClass;

    private final List<InjectedElement> injectedElements;

    public InjectedMetadata(Class<?> targetClass, List<InjectedElement> injectedElements) {
        this.targetClass = targetClass;
        this.injectedElements = injectedElements;
    }

    public void injected(Object bean) throws Throwable {
        for (InjectedElement element : injectedElements) {
            element.Injected(bean);
        }
    }

    public static class AutowiredInjectedElement extends InjectedElement {

        private final boolean required;

        public AutowiredInjectedElement(Member member, boolean required, BeanFactory beanFactory) {
            super(member, beanFactory);
            this.required = required;
        }

        @Override
        public void Injected(Object bean) throws Throwable {
            Field field = (Field) this.getMember();
            Object value = resolveFieldValue(ClassUtils.getShortName(field.getType()));

            if (required && value == null) {
                throw new NoSuchBeanDefinitionException(field.getType().getName());
            }

            if (value != null){
                ReflectionUtils.makeAccessible(field);
                field.set(bean, value);
            }

        }
    }

    public abstract static class InjectedElement {

        private final Member member;

        private final BeanFactory beanFactory;

        public InjectedElement(Member member, BeanFactory beanFactory) {
            this.member = member;
            this.beanFactory = beanFactory;
        }

        public Member getMember() {
            return member;
        }

        public Object resolveFieldValue(String name) {
            return beanFactory.getBean(name);
        }

        public void Injected(Object bean) throws Throwable{};
    }

}
