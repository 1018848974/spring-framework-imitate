package yy.springframework.aop.framework;

import org.junit.Before;
import org.junit.Test;
import yy.springframework.aop.HelloService;

import static org.junit.Assert.*;

public class CglibAopProxyTest {

    CglibAopProxy cglibAopProxy;

    @Before
    public void setUp() throws Exception {
        AdvisedSupport as = new AdvisedSupport(new TargetSource(new HelloService()), null);
        cglibAopProxy = new CglibAopProxy(as);
    }

    @Test
    public void getProxy() {
        HelloService proxy = (HelloService) cglibAopProxy.getProxy();
        proxy.sayHello("1");
    }
}