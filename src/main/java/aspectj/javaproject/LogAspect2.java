package aspectj.javaproject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogAspect2 {

    //所有实例方法调用截获
    private static final String INSTANCE_METHOD_CALL =
            "execution(* aspectj.javaproject.*.*(..)) ";

    @Pointcut(INSTANCE_METHOD_CALL)
    public void instanceMethodCall() {
    }

    //实例方法调用前后Advice
    @Before("instanceMethodCall()")
    public void beforInstanceCall(JoinPoint joinPoint) {
        System.out.println("before instance call");
    }

    @After("instanceMethodCall()")
    public void afterInstanceCall(JoinPoint joinPoint) {
        System.out.println("after instance call");
    }


}