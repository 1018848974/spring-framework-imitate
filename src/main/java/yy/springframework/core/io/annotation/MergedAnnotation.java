package yy.springframework.core.io.annotation;

import java.lang.annotation.Annotation;

public interface MergedAnnotation<A extends Annotation> {

    Class<A> getType();

    String getTypeName();

    AnnotationAttributes getAttribute();

}
