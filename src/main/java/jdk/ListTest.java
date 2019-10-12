package jdk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mapeiliang
 * Create Date: 2018/4/16 19:49
 * Description: ${DESCRIPTION}
 */
public class ListTest {

    public  static void main(String[] args){
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        list.add("ddd");



        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            Object bean = iterator.next();
            //System.out.println(iterator.next());
            iterator.remove();
            //list.remove(bean);

        }

        System.out.println(list.size());
    }
}
