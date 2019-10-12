package aspectj.helloworld;

/**
 * Created by ap 
 * Create Date: 2018/6/1 18:05
 * Description: ${DESCRIPTION}
 */
public aspect HelloAspect {

    pointcut HelloWorldPointCut() : execution(* aspectj.helloworld.MyService.sayHello(..));

    before() : HelloWorldPointCut(){
        System.out.println("begin intercept");
    }

    after() : HelloWorldPointCut(){
        System.out.println("end intercept");
    }

}


