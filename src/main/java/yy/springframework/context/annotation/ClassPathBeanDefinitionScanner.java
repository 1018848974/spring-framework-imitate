package yy.springframework.context.annotation;

import yy.springframework.beans.config.BeanDefinition;
import yy.springframework.beans.support.BeanDefinitionRegistry;
import yy.springframework.core.io.Resource;
import yy.springframework.core.io.ResourceLoader;
import yy.springframework.core.io.support.PackageResourceLoader;
import yy.springframework.core.io.type.AnnotationMetadata;
import yy.springframework.core.io.type.classreading.MetadataReader;
import yy.springframework.core.io.type.classreading.SimpleMetadataReader;
import yy.springframework.stereotype.Component;
import yy.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/10 9:51 下午 <br>
 * @see yy.springframework.context.annotation <br>
 */
public class ClassPathBeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;

    private ResourceLoader resourceLoader;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public int doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            List<BeanDefinition> candidateBeanDefinitions = findCandidateComponents(basePackage);
            candidateBeanDefinitions.forEach(candidate -> {
                registry.registerBeanDefinition(ClassUtils.getShortName(candidate.getBeanClassName()), candidate);
            });
        }
        return 0;
    }

    private List<BeanDefinition> findCandidateComponents(String basePackage) {
        return doScanCandidateComponents(basePackage);
    }

    private List<BeanDefinition> doScanCandidateComponents(String basePackage) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        Set<Resource> resources = getResourceLoader().getResource(basePackage);

        for (Resource resource : resources) {
            if (resource.canRead()) {
                MetadataReader reader = new SimpleMetadataReader(resource, ClassUtils.getDefaultClassLoader());
                if (isCandidateComponent(reader)) {
                    ScannedAnnotationBeanDefinition sb = new ScannedAnnotationBeanDefinition(reader);
                    if (isCandidateComponent(sb)) {
                        beanDefinitions.add(sb);
                    }
                }
            }
        }

        return beanDefinitions;
    }

    private boolean isCandidateComponent(ScannedAnnotationBeanDefinition sb) {
        AnnotationMetadata annotationMetadata = sb.getAnnotationMetadata();
        return annotationMetadata.isConcrete();
    }

    private boolean isCandidateComponent(MetadataReader reader) {
        return reader.getAnnotationMetadata().hasAnnotation(Component.class.getName());
    }

    public ResourceLoader getResourceLoader() {
        ResourceLoader resourceLoader = this.resourceLoader;
        if (resourceLoader == null) {
            resourceLoader = new PackageResourceLoader();
        }
        return resourceLoader;
    }

}
