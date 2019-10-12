package aspectj.javaproject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ap
 * Create Date: 2018/6/4 15:10
 * Description: ${DESCRIPTION}
 */
public class WithSpring {

    public static void main(String[] args){
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        try{
            Example performer = (Example)ctx.getBean("example");
            performer.printLog();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            ((ClassPathXmlApplicationContext)ctx).close();
        }

    }
}
