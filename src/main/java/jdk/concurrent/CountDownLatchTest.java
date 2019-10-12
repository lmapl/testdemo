package jdk.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 并发工具类
 * 控制一个或多个线程等待其他线程完成后，才可以执行后续逻辑。
 * 只能使用一次
 */
public class CountDownLatchTest {

    static CountDownLatch c = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println(2);
            c.countDown();
        }).start();

        //假设没有这一段，永远不会输出3
        new Thread(() -> {
            System.out.println(1);
            c.countDown();
        }).start();

        //时间到了也会继续执行
        c.await();
        //c.await(3000, TimeUnit.MILLISECONDS);
        System.out.println("3");

    }
}
