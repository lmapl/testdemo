package annotation.helloworld;

/**
 * Created by ap
 * Create Date: 2018/6/4 16:17
 * Description: ${DESCRIPTION}
 */
@TestPersonAnnotation(name="helloWorld",age = 14)
public class UseAnnotation {

    @MethonAnnotation(name = "helloMethod")
    public void f0(){
        System.out.println("111");
    }

}
