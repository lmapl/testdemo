package jdk.concurrent;

import java.util.concurrent.CyclicBarrier;

/**
 * 并发工具类
 * 同步屏障CyclicBarrier
 * 要求指定数量的线程都到达一个屏障，屏障才会开门，所有被屏障拦截的线程才会继续干活。
 * 在到达屏障的线程数不够时，所有线程被阻塞
 *
 * 可以通过reset重复使用
 */
public class CyclicBarrierTest {

   // static CyclicBarrier c = new CyclicBarrier(2);

    /**
     * 满足屏障，开门前先执行RunAction
     */
    static CyclicBarrier c = new CyclicBarrier(2,new RunAction());

    public static void main(String[] args) {

        new Thread(() -> {
            try {
                c.await();
            } catch (Exception e) {

            }
            System.out.println("线程1");
        }).start();

        try {
            Thread.sleep(1000);
            System.out.println("被阻塞线程数 "+c.getNumberWaiting());
            //reset会清空被阻塞的线程，从头开始阻塞
            c.reset();
            System.out.println("被阻塞线程数 "+c.getNumberWaiting());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            c.await();
        } catch (Exception e) {

        }
        System.out.println("主线程");

    }

    static class RunAction implements Runnable {
        @Override

        public void run() {
            System.out.println("RunAction");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
