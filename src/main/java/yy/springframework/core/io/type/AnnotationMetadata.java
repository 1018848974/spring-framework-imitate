package yy.springframework.core.io.type;

import yy.springframework.core.io.annotation.AnnotationAttributes;
import yy.springframework.core.io.annotation.MergedAnnotation;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface AnnotationMetadata extends ClassMetadata {

    default Set<String> getAnnotationTypes() {
        return getAnnotations().stream()
                .map(MergedAnnotation::getTypeName)
                .collect(Collectors.toSet());
    }

    public List<MergedAnnotation<?>> getAnnotations();

    default boolean hasAnnotation(String annotationName) {
        return this.getAnnotationTypes().contains(annotationName);
    }

    default AnnotationAttributes getAttributes(String annotationName) {
        Optional<MergedAnnotation<?>> annotation = this.getAnnotations().stream().filter(as -> as.getTypeName().equals(annotationName)).findFirst();
        return annotation.isPresent() ? annotation.get().getAttribute() : null;
    }
}
