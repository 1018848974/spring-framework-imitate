package yy.springframework.core.io.type.classreading;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.SpringAsmInfo;
import yy.springframework.core.io.annotation.MergedAnnotation;
import yy.springframework.core.io.type.AnnotationMetadata;
import yy.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/11 10:55 下午 <br>
 * @see yy.springframework.core.io.type.classreading <br>
 */
public class SimpleAnnotationMetadataReadingVisitor extends ClassVisitor {

    private final ClassLoader classLoader;

    private int access;

    private String className = "";

    private String superClassName;

    private String[] interfaceNames = new String[0];

    private List<MergedAnnotation<?>> annotations = new ArrayList<>();

    private SimpleAnnotationMetadata annotationMetadata;

    public SimpleAnnotationMetadataReadingVisitor(ClassLoader classLoader) {
        super(SpringAsmInfo.ASM_VERSION);
        this.classLoader = classLoader;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String supername, String[] interfaces) {
        this.className = ClassUtils.convertResourcePathToClassName(name);
        if (supername != null && !isInterface(access)) {
            this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
        }
        this.interfaceNames = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            interfaceNames[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String typeName, boolean visible) {
        return MergedAnnotationReadingVisitor.get(this.classLoader, typeName, visible, annotations::add);
    }

    @Override
    public void visitEnd() {
        annotationMetadata = new SimpleAnnotationMetadata(this.interfaceNames, this.className,
                this.access, this.superClassName, this.annotations);
    }

    public AnnotationMetadata getMetadata() {
        return this.annotationMetadata;
    }

    private boolean isInterface(int access) {
        return (access & Opcodes.ACC_INTERFACE) != 0;
    }
}
