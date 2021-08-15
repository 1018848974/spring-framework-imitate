package yy.springframework.core.io.annotation;

import yy.springframework.core.io.annotation.MergedAnnotation;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/13 8:37 下午 <br>
 * @see yy.springframework.core.io.type <br>
 */
public class TypeAnnotation<A extends Annotation> implements MergedAnnotation<A> {

    private final Class<A> annotationType;

    private final String annotationName;

    private final AnnotationAttributes attributes;

    public TypeAnnotation(Class<A> annotationType, String annotationName, AnnotationAttributes attributes) {
        this.annotationType = annotationType;
        this.annotationName = annotationName;
        this.attributes = attributes;
    }

    @Override
    public Class<A> getType() {
        return annotationType;
    }

    @Override
    public String getTypeName() {
        return annotationName;
    }

    @Override
    public AnnotationAttributes getAttribute() {
        return attributes;
    }
}
