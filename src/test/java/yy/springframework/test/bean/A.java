package yy.springframework.test.bean;

import yy.springframework.beans.factory.annotation.Autowired;
import yy.springframework.stereotype.Component;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/11 10:16 下午 <br>
 * @see yy.springframework.test.bean <br>
 */
@Component(scope = "")
public class A {

    @Autowired
    private B bbb;

    public B getBbb() {
        return bbb;
    }

    public void setBbb(B bbb) {
        this.bbb = bbb;
    }
}
