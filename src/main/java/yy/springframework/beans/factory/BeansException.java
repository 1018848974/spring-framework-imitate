package yy.springframework.beans.factory;

public class BeansException extends RuntimeException {

    private static final long serialVersionUID = 8268875102213220234L;

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
