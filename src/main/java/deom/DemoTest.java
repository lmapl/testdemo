package deom;

import java.io.PipedOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ma peiliang
 * Create Date: 2018/6/27 15:38
 * Description: ${DESCRIPTION}
 */
public class DemoTest {

    public static void main(String[] args){

        Reader reader;

        PipedOutputStream in = new PipedOutputStream();
        List<Demo> list = new ArrayList<>();
        Demo demo = new Demo();
        demo.setAge(10);
        list.add(demo);

        Demo demo1 = new Demo();
        demo1.setAge(11);
        list.add(demo1);

        //提前过滤掉
        List<Demo> list1 = list.stream().filter(item -> item.getAge()> 10).collect(Collectors.toList());
        list1.forEach(item -> System.out.println(item.getName()));

        //使用continue过滤掉
        for(Demo item : list){
            if(item.getAge() < 10){
                continue;
            }
            System.out.println(item.getName());
        }

        //忌执行逻辑和过滤逻辑混在一起
        for(Demo item : list){
            if(item.getAge() >10){
                System.out.println(item.getName());
            }
        }
    }


}

class Demo{
    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
