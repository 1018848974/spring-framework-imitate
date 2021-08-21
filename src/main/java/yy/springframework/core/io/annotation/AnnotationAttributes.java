package yy.springframework.core.io.annotation;

import yy.springframework.util.Assert;

import java.util.LinkedHashMap;

import static java.lang.String.format;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/13 9:12 下午 <br>
 * @see yy.springframework.core.io.annotation <br>
 */
public class AnnotationAttributes extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 5179112708359148810L;

    public String getString(String attributeName) {
        return doGet(attributeName, String.class);
    }

    public String[] getStringArray(String attributeName) {
        return doGet(attributeName, String[].class);
    }

    public boolean getBoolean(String attributeName) {
        return doGet(attributeName, Boolean.class);
    }

    @SuppressWarnings("unchecked")
    public <N extends Number> N getNumber(String attributeName) {
        return (N) doGet(attributeName, Integer.class);
    }

    @SuppressWarnings("unchecked")
    public <E extends Enum<?>> E getEnum(String attributeName) {
        return (E) doGet(attributeName, Enum.class);
    }

    @SuppressWarnings("unchecked")
    public <T> Class<? extends T> getClass(String attributeName) {
        return doGet(attributeName, Class.class);
    }

    public Class<?>[] getClassArray(String attributeName) {
        return doGet(attributeName, Class[].class);
    }

    @SuppressWarnings("unchecked")
    private <T> T doGet(String attributeName, Class<T> expectedType) {

        Object value = this.get(attributeName);
        //Assert.notNull(value, format("Attribute '%s' not found", attributeName));
        return (T) value;
    }
}
