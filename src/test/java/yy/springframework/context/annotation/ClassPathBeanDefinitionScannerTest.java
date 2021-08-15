package yy.springframework.context.annotation;



import org.junit.Before;
import org.junit.Test;
import yy.springframework.beans.factory.BeanFactory;
import yy.springframework.beans.support.DefaultBeanFactory;

import static org.junit.Assert.assertTrue;

public class ClassPathBeanDefinitionScannerTest {

    ClassPathBeanDefinitionScanner scanner;

    DefaultBeanFactory beanFactory;

    @Before
    public void setUp() {
        beanFactory = new DefaultBeanFactory();
        scanner = new ClassPathBeanDefinitionScanner(beanFactory);
    }

    @Test
    public void doScan() {
        scanner.doScan("yy.springframework.test.bean");
        assertTrue(beanFactory.getBeanNames().size() > 0);
    }
}