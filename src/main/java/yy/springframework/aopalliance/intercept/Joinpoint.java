package yy.springframework.aopalliance.intercept;

public interface Joinpoint {

    Object proceed() throws Throwable;

}
