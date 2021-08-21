package yy.springframework.aop.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.weaver.tools.*;
import org.junit.Before;
import org.junit.Test;
import yy.springframework.aop.HelloService;
import yy.springframework.aop.LogAspect;
import yy.springframework.context.annotation.AnnotationConfigApplicationContextTest;
import yy.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class AspectJExpressionPointcutTest {

    private static final String EXPRESSION = "execution(* yy.springframework.aop.HelloService.sayHello(..))";
    private AspectJExpressionPointcut aspectJExpressionPointcut;

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    @Before
    public void setUp() throws Exception {
        aspectJExpressionPointcut = new AspectJExpressionPointcut(LogAspect.class, EXPRESSION);
    }

    @Test
    public void matches() {
        boolean matches = aspectJExpressionPointcut.matches(HelloService.class);
        boolean matches2 = aspectJExpressionPointcut.matches(Object.class);
        assertTrue(matches);
        assertFalse(matches2);
    }

    @Test
    public void testMatches() throws NoSuchMethodException {
        boolean a = aspectJExpressionPointcut.matches(HelloService.class.getMethod("sayHello", String.class));
        boolean b = aspectJExpressionPointcut.matches(HelloService.class.getMethod("equals", Object.class));
        assertTrue(a);
        assertFalse(b);

    }

    @Test
    public void test() throws NoSuchMethodException {
        PointcutParser parser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                        SUPPORTED_PRIMITIVES, ClassUtils.getDefaultClassLoader());
        //  parser.registerPointcutDesignatorHandler(new BeanPointcutDesignatorHandler());
        PointcutExpression pointcutExpression = parser.parsePointcutExpression(EXPRESSION
                , LogAspect.class, new PointcutParameter[]{});

        boolean b = pointcutExpression.couldMatchJoinPointsInType(HelloService.class);
        System.out.println(b);

        boolean c = pointcutExpression.couldMatchJoinPointsInType(AnnotationConfigApplicationContextTest.class);
        System.out.println(c);

        ShadowMatch sayHello = pointcutExpression.matchesMethodExecution(HelloService.class.getMethod("sayHello", String.class));
        System.out.println(sayHello.alwaysMatches());

        ShadowMatch a = pointcutExpression.matchesMethodExecution(LogAspect.class.getMethod("logStart", JoinPoint.class));
        System.out.println(a.alwaysMatches());

    }
}