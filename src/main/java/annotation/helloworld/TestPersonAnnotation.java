package annotation.helloworld;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ap
 * Create Date: 2018/6/4 16:16
 * Description: ${DESCRIPTION}
 */
//@Target(ElementType.METHOD)
    @Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface TestPersonAnnotation {

    String name();

    int age();


}
