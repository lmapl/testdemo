package jdk.jdk8newfeature.lambda;

/**
 * Created by Ma PeiLiang
 * Create Date: 2018/4/18 17:07
 * Description: ${DESCRIPTION}
 */
public class Java8Tester2 {

    final static String salutation = "Hello! ";
    public static String salutation1 = "hello";

    public static void main(String args[]){
        GreetingService greetService1 = message -> {
            salutation1 = "ddd";
           // System.out.println(salutation + message+salutation1);
            System.out.println(salutation + message+salutation1);
        };
        greetService1.sayMessage("Runoob");
    }

    interface GreetingService {
        void sayMessage(String message);
    }
}
