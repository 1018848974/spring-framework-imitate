package yy.springframework.core.io.type.classreading;

import org.springframework.asm.ClassReader;
import yy.springframework.core.io.Resource;
import yy.springframework.core.io.type.AnnotationMetadata;
import yy.springframework.core.io.type.ClassMetadata;

import java.io.IOException;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/13 6:29 下午 <br>
 * @see yy.springframework.core.io.type.classreading <br>
 */
public class SimpleMetadataReader implements MetadataReader{

    private static final int PARSING_OPTIONS = org.springframework.asm.ClassReader.SKIP_DEBUG
            | org.springframework.asm.ClassReader.SKIP_CODE | org.springframework.asm.ClassReader.SKIP_FRAMES;

    private final Resource resource;

    private AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(Resource resource, ClassLoader classLoader) {
        this.resource = resource;

        SimpleAnnotationMetadataReadingVisitor visitor = new SimpleAnnotationMetadataReadingVisitor(classLoader);
        try {
            new ClassReader(resource.getInputStream()).accept(visitor, PARSING_OPTIONS);
            this.annotationMetadata = visitor.getMetadata();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resource getResource() {
        return this.resource;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return this.annotationMetadata;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }
}
