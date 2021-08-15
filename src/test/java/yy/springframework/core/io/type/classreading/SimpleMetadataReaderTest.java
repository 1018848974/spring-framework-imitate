package yy.springframework.core.io.type.classreading;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import yy.springframework.core.io.Resource;
import yy.springframework.core.io.support.PackageResourceLoader;
import yy.springframework.core.io.type.AnnotationMetadata;
import yy.springframework.core.io.type.ClassMetadata;

import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class SimpleMetadataReaderTest {

    private SimpleMetadataReader simpleMetadataReader;

    @Before
    public void setUp() throws Exception {
        PackageResourceLoader packageResourceLoader = new PackageResourceLoader();
        Set<Resource> resource = packageResourceLoader.getResource("yy.springframework.test.bean");
        simpleMetadataReader = new SimpleMetadataReader(resource.stream().findFirst().get(), packageResourceLoader.getClassLoader());
    }

    @Test
    public void testGetAnnotationMetadata() {
        AnnotationMetadata annotationMetadata = simpleMetadataReader.getAnnotationMetadata();
        assertNotNull(annotationMetadata);
        assertNotNull(annotationMetadata.getAnnotations());
    }
}