package yy.springframework.beans.factory.annotation;

import yy.springframework.beans.config.BeanDefinition;
import yy.springframework.core.io.type.AnnotationMetadata;

public interface AnnotatedBeanDefinition extends BeanDefinition {

    AnnotationMetadata getAnnotationMetadata();

}
