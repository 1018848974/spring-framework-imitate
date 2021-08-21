package yy.springframework.core.io.type.classreading;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.SpringAsmInfo;
import org.springframework.asm.Type;
import yy.springframework.core.io.annotation.AnnotationAttributes;
import yy.springframework.core.io.annotation.MergedAnnotation;
import yy.springframework.core.io.annotation.TypeAnnotation;
import yy.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/13 8:49 下午 <br>
 * @see yy.springframework.core.io.type.classreading <br>
 */
public class MergedAnnotationReadingVisitor<A extends Annotation> extends AnnotationVisitor {

    private final ClassLoader classLoader;

    private final Class<A> annotationType;

    private final Consumer<MergedAnnotation<A>> consumer;

    private final AnnotationAttributes attributes = new AnnotationAttributes();

    public MergedAnnotationReadingVisitor(ClassLoader classLoader, Class<A> annotationType, Consumer<MergedAnnotation<A>> consumer) {
        super(SpringAsmInfo.ASM_VERSION);
        this.annotationType = annotationType;
        this.classLoader = classLoader;
        this.consumer = consumer;
    }

    @Override
    public void visit(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public void visitEnd() {
        consumer.accept(new TypeAnnotation(annotationType, annotationType.getName(), attributes));
    }

    public static <A extends Annotation> MergedAnnotationReadingVisitor get(ClassLoader classLoader, String typeDesc, boolean visible, Consumer<MergedAnnotation<A>> consumer) {
        if (!visible) {
            return null;
        }

        String className = Type.getType(typeDesc).getClassName();

        try {
            Class<?> annotationClass = ClassUtils.forName(className, classLoader);
            return new MergedAnnotationReadingVisitor(classLoader, annotationClass, consumer);
        } catch (ClassNotFoundException e) {
            return null;
        }

    }
}
