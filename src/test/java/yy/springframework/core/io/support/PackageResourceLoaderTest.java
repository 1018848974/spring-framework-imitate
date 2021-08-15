package yy.springframework.core.io.support;

import org.junit.Before;
import org.junit.Test;
import yy.springframework.core.io.Resource;
import yy.springframework.util.Assert;

import java.util.Set;

public class PackageResourceLoaderTest {

    private PackageResourceLoader packageResourceLoader;

    @Before
    public void setUp(){
        packageResourceLoader = new PackageResourceLoader();
    }

    @Test
    public void testGetResources(){
        Set<Resource> resource = packageResourceLoader.getResource("yy.springframework.test.bean");
        Assert.notEmpty(resource);
    }

}