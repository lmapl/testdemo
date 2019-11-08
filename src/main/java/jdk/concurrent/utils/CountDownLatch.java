package jdk.concurrent.utils;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/** 基于AbstractQueuedSynchronizer同步器：用于实现一个或者多个线程  等待指定个数的其他线程中的操作完成  才可以继续执行的场景
 *  原理是： AQS同步器的 status，从设置的初始值，依次减一，直到0
 * */
public class CountDownLatch {

    /** AQS同步器  */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        /**构造器： count 是 等待线程的个数 */
        Sync(int count) {
            setState(count);
        }

        int getCount() {
            return getState();
        }

        /**尝试获取锁（共享锁），判断是否归0*/
        protected int tryAcquireShared(int acquires) {
            return (getState() == 0) ? 1 : -1;
        }

        /**尝试释放锁（共享锁）*/
        protected boolean tryReleaseShared(int releases) {
            // Decrement count; signal when transition to zero
            for (;;) {
                int c = getState();
                if (c == 0)
                    return false;

                //status 减 1
                int nextc = c-1;
                if (compareAndSetState(c, nextc))
                    return nextc == 0;
            }
        }
    }

    private final Sync sync;

    /** 构造器 */
    public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }

    /** 阻塞等待 CountDownLatch 归0*/
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    /** 阻塞等待，一段时间或者 CountDownLatch 归0*/
    public boolean await(long timeout, TimeUnit unit)
        throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    /** CountDownLatch 计数器的个数 -1 ，并且在归0 时，释放所有等待的线程。
     * 原理是同步器的status -1，直到为0 */
    public void countDown() {
        sync.releaseShared(1);
    }

    /** 获取当前剩余的数量   */
    public long getCount() {
        return sync.getCount();
    }

    /**转换为字符串  */
    public String toString() {
        return super.toString() + "[Count = " + sync.getCount() + "]";
    }
}
