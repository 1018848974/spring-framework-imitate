package yy.springframework.test.bean;

import yy.springframework.beans.factory.annotation.Autowired;
import yy.springframework.context.annotation.ComponentScan;
import yy.springframework.context.annotation.Configuration;
import yy.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/14 8:32 下午 <br>
 * @see yy.springframework.test.bean <br>
 */

@Configuration
@ComponentScan("yy.springframework.test.bean")
public class MainConfig {

    @Autowired(required = false)
    private Object object;

    public static void main(String[] args) {
        Annotation[] annotations = MainConfig.class.getAnnotations();
        for (Annotation annotation : annotations) {
            for (Method method : annotation.annotationType().getMethods()) {
                try {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if(parameterTypes.length > 0) continue;;
                    Object v = method.invoke(annotation);
                    System.out.println("key :   " +  method.getName()+ "|   v：" + v.toString());
                } catch (Exception e) {
                    System.err.println(method.getName());
//                    e.printStackTrace();
                }
            }


        }
    }
}
