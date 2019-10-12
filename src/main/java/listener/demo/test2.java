package listener.demo;

import java.util.HashMap;
import java.util.Map;

public class test2 {
    public static void main(String args[]) {
        Map<Integer, String> map = new HashMap<Integer, String>();
        Mylistener mylistener = new Mylistener(map);
    }
}