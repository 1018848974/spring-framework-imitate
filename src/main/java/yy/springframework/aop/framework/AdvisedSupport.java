package yy.springframework.aop.framework;

import yy.springframework.aop.Advisor;

import java.util.List;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/21 4:37 下午 <br>
 * @see yy.springframework.aop.framework <br>
 */
public class AdvisedSupport implements Advised {

    private TargetSource targetSource;

    private List<Advisor> advisors;

    public AdvisedSupport(TargetSource targetSource, List<Advisor> advisors) {
        this.targetSource = targetSource;
        this.advisors = advisors;
    }

    @Override
    public TargetSource getTargetSource() {
        return this.targetSource;
    }

    @Override
    public List<Advisor> getAdvisors() {
        return this.advisors;
    }

    @Override
    public Class<?> getTargetClass() {
        return targetSource.getTargetClass();
    }
}
