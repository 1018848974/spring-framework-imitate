package yy.springframework.context.annotation;

public interface AnnotationConfigRegistry {

    void register(Class<?>... componentClasses);

    void scan(String... basePackages);

}
