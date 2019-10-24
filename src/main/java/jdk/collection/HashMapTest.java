package jdk.collection;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ma peiliang
 * Create Date: 2019/10/15 19:46
 * Description: ${DESCRIPTION}
 */
public class HashMapTest {
    public static void main(String[] args){

        Map<String,String> map = new HashMap<>();
        System.out.println(map.size());

        map.put("aa","bbbbb");
        map.put("bbb","aaaa");
        map.get("aaa");
        Set<String> ksy = map.keySet();
        Set<Map.Entry<String,String>> entries =  map.entrySet();
    }
}
