package unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by ma peiliang
 * Create Date: 2019/10/9 14:12
 * Description: ${DESCRIPTION}
 */
public class InitTest {

    public  static void main(String[] args){
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            unsafe = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
