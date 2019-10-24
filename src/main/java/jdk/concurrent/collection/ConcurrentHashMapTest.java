package jdk.concurrent.collection;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ma peiliang
 * Create Date: 2019/10/21 11:33
 * Description: ${DESCRIPTION}
 */
public class ConcurrentHashMapTest {

    public  static void main(String[] args){
        Object object = new Object();
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("aa","bb");
        concurrentHashMap.put("aaa","bb");
        concurrentHashMap.put("gggg122222333","bbb");
        concurrentHashMap.put("999999999999","bbb");
        concurrentHashMap.get("bb");

    }
}
