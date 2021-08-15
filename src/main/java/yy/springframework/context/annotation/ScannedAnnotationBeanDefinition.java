package yy.springframework.context.annotation;

import yy.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import yy.springframework.beans.support.AbstractBeanDefinition;
import yy.springframework.core.io.annotation.AnnotationAttributes;
import yy.springframework.core.io.type.AnnotationMetadata;
import yy.springframework.core.io.type.classreading.MetadataReader;
import yy.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/11 11:36 下午 <br>
 * @see yy.springframework.context.annotation <br>
 */
public class ScannedAnnotationBeanDefinition extends AbstractBeanDefinition implements AnnotatedBeanDefinition {

    private final AnnotationMetadata annotationMetadata;

    public ScannedAnnotationBeanDefinition(MetadataReader metadataReader) {
        this.annotationMetadata = metadataReader.getAnnotationMetadata();
        this.setBeanClassName(annotationMetadata.getClassName());
        Optional<AnnotationAttributes> attributes = Optional.ofNullable(annotationMetadata.getAttributes(Component.class.getName()));
        this.setScope(attributes.map(attr -> attr.getString("scope")).orElse(""));
    }

    public AnnotationMetadata getAnnotationMetadata() {
        return annotationMetadata;
    }
}
