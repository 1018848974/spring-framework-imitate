package yy.springframework.core.io.type.classreading;

import org.springframework.asm.Opcodes;
import yy.springframework.core.io.annotation.AnnotationAttributes;
import yy.springframework.core.io.annotation.MergedAnnotation;
import yy.springframework.core.io.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/13 6:50 下午 <br>
 * @see yy.springframework.core.io.type.classreading <br>
 */
public class SimpleAnnotationMetadata implements AnnotationMetadata {

    private final String[] interfaceNames;

    private final String className;

    private final int access;

    private final String superClassName;

    private List<MergedAnnotation<?>> annotations;

    private Set<String> annotationTypes;

    public SimpleAnnotationMetadata(String[] interfaceNames, String className, int access, String superClassName, List<MergedAnnotation<?>> annotations) {
        this.interfaceNames = interfaceNames;
        this.className = className;
        this.access = access;
        this.superClassName = superClassName;
        this.annotations = annotations;
    }



    @Override
    public Set<String> getAnnotationTypes() {
        Set<String> annotationTypes = this.annotationTypes;
        if (annotationTypes == null) {
            annotationTypes = Collections.unmodifiableSet(AnnotationMetadata.super.getAnnotationTypes());
            this.annotationTypes = annotationTypes;
        }
        return annotationTypes;
    }

    @Override
    public List<MergedAnnotation<?>> getAnnotations() {
        return this.annotations;
    }


    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public boolean isInterface() {
        return (this.access & Opcodes.ACC_INTERFACE) != 0;
    }

    @Override
    public boolean isAnnotation() {
        return (this.access & Opcodes.ACC_ANNOTATION) != 0;
    }

    @Override
    public boolean isAbstract() {
        return (this.access & Opcodes.ACC_ABSTRACT) != 0;
    }

    @Override
    public boolean isFinal() {
        return (this.access & Opcodes.ACC_FINAL) != 0;
    }

    @Override
    public String getSuperClassName() {
        return this.superClassName;
    }

    @Override
    public String[] getInterfaceNames() {
        return this.interfaceNames;
    }
}
