package annotation.helloworld;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by ap
 * Create Date: 2018/6/4 16:25
 * Description: ${DESCRIPTION}
 */
public class ExtendsMain {

    public static void main(String[] args){

        Method[] methods = UseAnnotation.class.getMethods();
        if(methods != null){
            Annotation[] annotations;
            for(Method method :methods){
                annotations =  method.getAnnotations();
                for(Annotation annotation : annotations){
                    if(annotation instanceof  MethonAnnotation){
                        MethonAnnotation testPersonAnnotation = (MethonAnnotation) annotation;
                        System.out.println(testPersonAnnotation.name());
                    }
                }
            }
        }
    }
}
