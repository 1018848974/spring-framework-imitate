package yy.springframework;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import yy.springframework.aop.aspectj.AspectJExpressionPointcutTest;
import yy.springframework.context.annotation.ClassPathBeanDefinitionScannerTest;
import yy.springframework.core.io.support.PackageResourceLoaderTest;
import yy.springframework.core.io.type.classreading.SimpleMetadataReaderTest;

@RunWith(Suite.class)
@SuiteClasses({SimpleMetadataReaderTest.class, PackageResourceLoaderTest.class, ClassPathBeanDefinitionScannerTest.class, AspectJExpressionPointcutTest.class})
public class ExecuteAllTests {

}
