package yy.springframework.core.io.type;

public interface ClassMetadata {

    String getClassName();

    boolean isInterface();

    boolean isAnnotation();

    boolean isAbstract();

    default boolean isConcrete() {
        return !(isInterface() || isAbstract());
    }

    boolean isFinal();

    default boolean hasSuperClass() {
        return (getSuperClassName() != null);
    }

    String getSuperClassName();

    String[] getInterfaceNames();
}
