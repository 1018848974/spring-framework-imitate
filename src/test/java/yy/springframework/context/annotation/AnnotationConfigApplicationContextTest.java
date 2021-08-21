package yy.springframework.context.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.weaver.tools.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import yy.springframework.aop.HelloService;
import yy.springframework.aop.LogAspect;
import yy.springframework.test.bean.A;
import yy.springframework.test.bean.MainConfig;
import yy.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Set;

public class AnnotationConfigApplicationContextTest {

    AnnotationConfigApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
    }

    @Test
    public void getBean() {
        MainConfig mainConfig = applicationContext.getBean("MainConfig", MainConfig.class);
        Assert.assertNotNull(mainConfig);

        Set<String> beanNames = applicationContext.getBeanNames();
        Assert.assertTrue(beanNames.contains(ClassUtils.getShortName(A.class)));

        A a = applicationContext.getBean("A", A.class);
        Assert.assertNotNull(a);
        Assert.assertNotNull(a.getBbb());

    }

    @Test
    public void aopTest() {
        HelloService helloService = applicationContext.getBean("HelloService", HelloService.class);
        helloService.sayHello("aa");

    }

}