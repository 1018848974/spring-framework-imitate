package yy.springframework.aop.framework;

import yy.springframework.aop.Advisor;

import java.util.List;

public interface Advised {

    TargetSource getTargetSource();

    List<Advisor> getAdvisors();

    Class<?> getTargetClass();

}
