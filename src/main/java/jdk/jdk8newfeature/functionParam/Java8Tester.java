package jdk.jdk8newfeature.functionParam;

/**
 * Created by Ma PeiLiang
 * Create Date: 2018/4/18 17:41
 * Description: ${DESCRIPTION}
 */

import java.util.ArrayList;
import java.util.List;

public class Java8Tester {
    public static void main(String args[]){
        List names = new ArrayList();

        names.add("Google");
        names.add("Runoob");
        names.add("Taobao");
        names.add("Baidu");
        names.add("Sina");

        names.forEach(System.out::println);
    }
}
