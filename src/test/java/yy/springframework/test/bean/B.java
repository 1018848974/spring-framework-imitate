package yy.springframework.test.bean;

import yy.springframework.beans.factory.annotation.Autowired;
import yy.springframework.stereotype.Component;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/15 5:22 下午 <br>
 * @see yy.springframework.test.bean <br>
 */
@Component
public class B {

    @Autowired
    private A aaa;

}
