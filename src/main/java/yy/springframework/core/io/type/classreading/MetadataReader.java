package yy.springframework.core.io.type.classreading;

import yy.springframework.core.io.Resource;
import yy.springframework.core.io.type.AnnotationMetadata;
import yy.springframework.core.io.type.ClassMetadata;

public interface MetadataReader {

    /**
     * Return the resource reference for the class file.
     */
    Resource getResource();

    /**
     * Read basic class metadata for the underlying class.
     */
    ClassMetadata getClassMetadata();

    /**
     * Read full annotation metadata for the underlying class,
     * including metadata for annotated methods.
     */
    AnnotationMetadata getAnnotationMetadata();

}
