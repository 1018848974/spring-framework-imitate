package yy.springframework.context.annotation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import yy.springframework.test.bean.A;
import yy.springframework.test.bean.MainConfig;
import yy.springframework.util.ClassUtils;

import java.util.Set;

import static org.junit.Assert.*;

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
}