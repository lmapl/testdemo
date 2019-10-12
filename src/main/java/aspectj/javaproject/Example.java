package aspectj.javaproject;

import org.springframework.stereotype.Service;

@Service
public class Example {
   String value = "value";
   public void printLog() {
     String str = getValue();
     System.out.println(str);
   }
   public String getValue() {
     return value;
   }
}