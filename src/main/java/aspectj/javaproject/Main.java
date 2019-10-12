package aspectj.javaproject;

import aspectj.helloworld.MyService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
  
    public static void main(String[] args) {  
        /*System.out.println("MyService begin...");
        MyService service = new MyService();
        service.sayHello("aaaa");
        System.out.println("MyService end...");*/


        System.out.println("Example init...");
        Example example = new Example();
        example.printLog();
        System.out.println("Example end...");



    }
} 