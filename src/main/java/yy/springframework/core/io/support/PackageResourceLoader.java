package yy.springframework.core.io.support;

import yy.springframework.core.io.ClassFileResource;
import yy.springframework.core.io.Resource;
import yy.springframework.core.io.ResourceLoader;
import yy.springframework.util.Assert;
import yy.springframework.util.ClassUtils;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/11 9:26 下午 <br>
 * @see yy.springframework.core.io.support <br>
 */
public class PackageResourceLoader implements ResourceLoader {

    private final ClassLoader classLoader;

    public PackageResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public Set<Resource> getResource(String basePackage) {
        Assert.notNull(basePackage, "basePackage can not be null");
        String classLocation = ClassUtils.convertClassNameToResourcePath(basePackage);

        URL resource = this.classLoader.getResource(classLocation);
        File file = new File(resource.getFile());

        Set<File> fileSet = retrieveFiles(file);

        return fileSet.stream().map(fileSource -> new ClassFileResource(fileSource)).collect(Collectors.toSet());
    }

    private Set<File> retrieveFiles(File file) {

        if (!file.exists()) {
            return Collections.emptySet();
        }

        if (!file.canRead()) {
            return Collections.emptySet();
        }

        if (!file.isDirectory()) {
            return Collections.emptySet();
        }

        HashSet<File> fileSet = new HashSet<>();

        doRetrieveFiles(file, fileSet);

        return fileSet;
    }

    private void doRetrieveFiles(File rootFile, Set<File> result) {
        if (rootFile == null) return;

        for (File file : rootFile.listFiles()) {
            if (file.isDirectory() && file.canRead()) {
                doRetrieveFiles(file, result);
            } else {
                result.add(file);
            }
        }

    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

}
