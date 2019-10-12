package jdk.jdk8newfeature.lambda;

/**
 * Created by Ma PeiLiang
 * Create Date: 2018/4/18 17:11
 * Description: ${DESCRIPTION}
 */
public class Java8Tester3 {
    public static void main(String args[]) {
        int num = 1;
        Converter<Integer, String> s = (param) -> {
            System.out.println(String.valueOf(param + num));
        };
        s.convert(2);  // 输出结果为 3

    }

    public interface Converter<T1, T2> {
        void convert(int i);
    }
}
