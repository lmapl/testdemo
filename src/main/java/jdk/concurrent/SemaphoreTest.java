package jdk.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 并发工具类
 * 控制同一时间的并发线程数，用于对有限共用资源做流量（访问量）控制
 * 当达到上限时，不允许访问
 * 可以通过reset重复使用
 */
public class SemaphoreTest {

    private static final int THREAD_COUNT = 30;

    private static ExecutorService threadPool = Executors
            .newFixedThreadPool(THREAD_COUNT);

    private static Semaphore s = new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(new Action(i,s));
        }
        System.out.println("end");

        threadPool.shutdown();

    }


}

class  Action  implements  Runnable{
    private Semaphore s;
    private int i;
    public Action(int i,Semaphore s){
        this.i = i;
        this.s = s;
    }

    @Override
    public void run() {
        try{
            s.acquire();
            //System.out.println(s.getQueueLength());
            System.out.println("save data"+i);
           // s.release();
        }catch(InterruptedException e){
        }
    }
}
