package yy.springframework.aop.aspectj;

import org.aspectj.weaver.tools.*;
import yy.springframework.aop.ClassFilter;
import yy.springframework.aop.MethodMatcher;
import yy.springframework.aop.Pointcut;
import yy.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <Description> <br>
 *
 * @author sunyang<br>
 * @version 1.0<br>
 * @createDate 2021/08/18 9:59 下午 <br>
 * @see yy.springframework.aop.aspectj <br>
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {

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

    private final Class<?> pointcutDeclarationClass;

    private transient ClassLoader pointcutClassLoader;

    private final String expression;

    private transient PointcutExpression pointcutExpression;

    private transient Map<Method, ShadowMatch> shadowMatchCache = new ConcurrentHashMap<>(32);

    public AspectJExpressionPointcut(Class<?> pointcutDeclarationClass, String expression) {
        if (expression == null) {
            throw new IllegalArgumentException(" MUST SET EXPRESSION");
        }
        this.expression = expression;
        this.pointcutDeclarationClass = pointcutDeclarationClass;
    }

    @Override
    public boolean matches(Class<?> clazz) {
        obtainPointcutExpression();
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method) {
        obtainPointcutExpression();
        ShadowMatch shadowMatch = getTargetShadowMatch(method);
        return shadowMatch.alwaysMatches();
    }

    private ShadowMatch getTargetShadowMatch(Method method) {
        ShadowMatch shadowMatch = shadowMatchCache.get(method);

        if (shadowMatch == null) {
            synchronized (this.shadowMatchCache) {
                shadowMatch = this.shadowMatchCache.get(method);
                if (shadowMatch == null) {
                    shadowMatch = pointcutExpression.matchesMethodExecution(method);
                }
            }
        }

        return shadowMatch;
    }

    @Override
    public ClassFilter getClassFilter() {
        obtainPointcutExpression();
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    private PointcutExpression obtainPointcutExpression() {
        PointcutExpression pointcutExpression = this.pointcutExpression;
        if (pointcutExpression == null) {
            this.pointcutExpression = buildPointExpression();
        }
        return this.pointcutExpression;
    }

    private PointcutExpression buildPointExpression() {
        PointcutParser parser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                        SUPPORTED_PRIMITIVES, ClassUtils.getDefaultClassLoader());
        return parser.parsePointcutExpression(getExpression(), pointcutDeclarationClass, new PointcutParameter[]{});
    }

}
