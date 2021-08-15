package yy.springframework.context.annotation;

import yy.springframework.beans.config.BeanDefinition;
import yy.springframework.beans.factory.BeanDefinitionRegistryPostProcessor;
import yy.springframework.beans.factory.BeansException;
import yy.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import yy.springframework.beans.support.BeanDefinitionRegistry;
import yy.springframework.core.io.annotation.AnnotationAttributes;
import yy.springframework.core.io.type.AnnotationMetadata;
import yy.springframework.core.io.type.StandardAnnotationMetadata;
import yy.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/14 1:28 下午 <br>
 * @see yy.springframework.context.annotation <br>
 */
public class ConfigurationPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private ClassPathBeanDefinitionScanner scanner;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        scanner = new ClassPathBeanDefinitionScanner(registry);

        HashSet<AnnotationMetadata> candidates = new HashSet<>();

        //拿所有BeanDefinition ， 判断含有Configuration注解的进行解析
        for (String beanName : registry.getBeanNames()) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            AnnotationMetadata metadata;
            if (beanDefinition instanceof AnnotatedBeanDefinition) {
                metadata = ((AnnotatedBeanDefinition) beanDefinition).getAnnotationMetadata();
            } else {
                metadata = new StandardAnnotationMetadata(beanDefinition.getBeanClass());
            }

            if (metadata.hasAnnotation(Configuration.class.getName())) {
                candidates.add(metadata);
            }
        }

        //解析配置类
        parserConfigurationClass(candidates, registry);
    }

    private void parserConfigurationClass(HashSet<AnnotationMetadata> candidates, BeanDefinitionRegistry registry) {
        //解析ComponentScan
        List<String> sanPackages = new ArrayList<>();
        for (AnnotationMetadata candidate : candidates) {
            if (candidate.hasAnnotation(ComponentScan.class.getName())) {
                AnnotationAttributes attributes = candidate.getAttributes(ComponentScan.class.getName());
                String[] scanPackage = attributes.getStringArray("value");
                sanPackages.addAll(Arrays.asList(scanPackage));
            }
        }

        scanner.doScan(StringUtils.toStringArray(sanPackages));
    }

}
