package yy.springframework.core.io;

import java.util.Set;

public interface ResourceLoader {

    Set<Resource> getResource(String basePackage);

}
