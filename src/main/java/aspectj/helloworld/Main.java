package aspectj.helloworld;

public class Main {
  
    public static void main(String[] args) {  
        System.out.println("main begin...");  
        MyService service = new MyService();  
        service.sayHello("aaaa");
        System.out.println("main end...");  
    }  
} 