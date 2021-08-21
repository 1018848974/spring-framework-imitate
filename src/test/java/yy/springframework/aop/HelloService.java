package yy.springframework.aop;

import yy.springframework.stereotype.Component;

@Component
public class HelloService {

    public HelloService() {
        System.out.println("....");
    }

    public String sayHello(String name) {
        String result = "你好：" + name;
        System.out.println(result);
        int length = name.length();
        return result + "---" + length;
    }
}
