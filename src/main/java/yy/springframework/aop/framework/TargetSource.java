package yy.springframework.aop.framework;

public class TargetSource {

    private Object target;

    private Class<?> targetClass;

    public TargetSource(Object bean) {
        this.target = bean;
        this.targetClass = bean.getClass();
    }

    public Object getTarget() {
        return this.target;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

}
