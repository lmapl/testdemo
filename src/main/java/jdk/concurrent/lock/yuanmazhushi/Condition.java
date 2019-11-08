package jdk.concurrent.lock.yuanmazhushi;
import java.util.concurrent.TimeUnit;
import java.util.Date;

/**提供了类似Object的监视器方法，与Lock配合可以实现等待/通知模式 */
public interface Condition {

    /**当前线程进入等待状态直到被通知（signal）或中断  */
    void await() throws InterruptedException;

    /**当前线程进入等待状态直到被通知，该方法不响应中断。 */
    void awaitUninterruptibly();

    /**当前线程进入等待状态直到被通知、中断或者超时，返回值表示剩余超时时间。 */
    long awaitNanos(long nanosTimeout) throws InterruptedException;

    /**当前线程进入等待状态直到被通知、中断或者到某个时间。如果没有到指定时间就被通知，方法返回true，否则，表示到了指定时间，返回false。 */
    boolean await(long time, TimeUnit unit) throws InterruptedException;

    /**唤醒一个等待在Condition上的线程，该线程从等待方法返回前必须获得与Condition相关联的锁 */
    boolean awaitUntil(Date deadline) throws InterruptedException;

    /**唤醒一个等待在Condition上的线程，该线程从等待方法返回前必须获得与Condition相关联的锁。 */
    void signal();

    /**唤醒所有等待在Condition上的线程，能够从等待方法返回的线程必须获得与Condition相关联的锁。*/
    void signalAll();
}
