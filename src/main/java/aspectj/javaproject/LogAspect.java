package aspectj.javaproject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 不和spring结合使用
 */
@Aspect
public class LogAspect {

   /* //所有实例方法调用截获
    private static final String INSTANCE_METHOD_CALL =
            "call(* aspectj.javaproject..*.*(..))&&target(Object)";

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
    }*/





}