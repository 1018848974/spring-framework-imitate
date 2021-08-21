package yy.springframework.core.io.type;

import yy.springframework.core.io.annotation.AnnotationAttributes;
import yy.springframework.core.io.annotation.MergedAnnotation;
import yy.springframework.core.io.annotation.TypeAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/15 3:36 下午 <br>
 * @see yy.springframework.core.io.type <br>
 */
public class StandardAnnotationMetadata implements AnnotationMetadata {

    private final Class<?> source;

    private final List<MergedAnnotation<?>> annotations = new ArrayList<>();

    private final Set<String> annotationTypes = new HashSet<>();

    public StandardAnnotationMetadata(Class<?> source) {
        this.source = source;
        Annotation[] annotations = source.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            annotationTypes.add(annotationType.getName());

            AnnotationAttributes attributes = new AnnotationAttributes();
            for (Method method : annotation.annotationType().getMethods()) {
                try {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length > 0) continue;
                    Object v = method.invoke(annotation);
                    attributes.put(method.getName(), v);
                } catch (Exception e) {

                }
            }

            TypeAnnotation<Annotation> typeAnnotation = new TypeAnnotation(annotationType, annotationType.getName(), attributes);
            this.annotations.add(typeAnnotation);
        }
    }

    @Override
    public List<MergedAnnotation<?>> getAnnotations() {
        return this.annotations;
    }

    @Override
    public String getClassName() {
        return source.getName();
    }

    @Override
    public boolean isInterface() {
        return source.isInterface();
    }

    @Override
    public boolean isAnnotation() {
        return source.isAnnotation();
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(source.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(source.getModifiers());
    }

    @Override
    public String getSuperClassName() {
        return source.getSuperclass().getName();
    }

    @Override
    public String[] getInterfaceNames() {
        //TODO
        return null;
    }
}
