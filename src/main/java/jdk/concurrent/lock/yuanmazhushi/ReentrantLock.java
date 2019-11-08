package jdk.concurrent.lock.yuanmazhushi;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ReentrantLock implements Lock, java.io.Serializable {
    private static final long serialVersionUID = 7373984872572414699L;
    /** 同步器 ；抽象类的Sync 的实现类（内部类） */
    private final Sync sync;

    /** 无参构造器默认非公平锁   */
    public ReentrantLock() {
        sync = new NonfairSync();
    }

    /** 构造器 根据参数生成公平锁或者非公平锁   */
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

    /** 获取锁：可重入锁，获取不到会阻塞等待 */
    public void lock() {
        sync.lock();
    }

    /**获取锁，可中断锁 */
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    /** 获取锁， 获取不到不会阻塞等待 */
    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }

    /**获取锁， 获取不到不会阻塞等待,但是会在一定时间内重试 */
    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    /**释放锁*/
    public void unlock() {
        sync.release(1);
    }

    /** 还待研究 */
    public Condition newCondition() {
        return sync.newCondition();
    }

    /** 查询当前线程对此锁的持有数字 */
    public int getHoldCount() {
        return sync.getHoldCount();
    }

    /**判断当前线程释放持有这个锁 */
    public boolean isHeldByCurrentThread() {
        return sync.isHeldExclusively();
    }

    /** * 判断这个锁是否被锁住了（任一线程持有） */
    public boolean isLocked() {
        return sync.isLocked();
    }

    /**判断锁是否是公平锁 */
    public final boolean isFair() {
        return sync instanceof FairSync;
    }

    /**获取锁当前的持有者线程 */
    protected Thread getOwner() {
        return sync.getOwner();
    }

    /** 判断是否有线程再等待获取锁 */
    public final boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    /** 判断某个线程是否在等待队列  */
    public final boolean hasQueuedThread(Thread thread) {
        return sync.isQueued(thread);
    }

    /**获取等待中的线程的个数 */
    public final int getQueueLength() {
        return sync.getQueueLength();
    }

    /**获取等待中的线程 */
    protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }

    /** 判断是否有线程在这个锁上等待（根据指定的条件）  */
    public boolean hasWaiters(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

    /** 获取这个锁上等待（根据指定的条件）的线程的个数 */
    public int getWaitQueueLength(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

    /** * 获取这个锁上等待（根据指定的条件）的线程 */
    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null)
            throw new NullPointerException();
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject))
            throw new IllegalArgumentException("not owner");
        return sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject)condition);
    }

    /**转换为字符串   */
    public String toString() {
        Thread o = sync.getOwner();
        return super.toString() + ((o == null) ?
                                   "[Unlocked]" :
                                   "[Locked by thread " + o.getName() + "]");
    }


    /** 内部类：同步锁控制的基础。在下方会被实现为公平器，非公平器。使用aqs状态表示锁上的保持数 */
    abstract static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;

        /** 获取锁，需子类实现 */
        abstract void lock();

        /**尝试获取非公平锁*/
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                //等于0 ，判定为没有线程占用，可以使用CAS获取锁
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                //不等于0，判定为已经被线程占用了，判定是否是当前线程。如果是status 增加
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }

            //获取不到锁，返回false
            return false;
        }

        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        protected final boolean isHeldExclusively() {
            // While we must in general read state before owner,
            // we don't need to do so to check if current thread is owner
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        // Methods relayed from outer class

        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        final boolean isLocked() {
            return getState() != 0;
        }

        /**
         * Reconstitutes the instance from a stream (that is, deserializes it).
         */
        private void readObject(java.io.ObjectInputStream s)
                throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }

    /*** 内部类：非公平锁同步器  */
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        /**获取锁 */
        final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    /** 内部类：公平锁同步器  */
    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        final void lock() {
            //AQS模板方法， 会回调tryAcquire
            acquire(1);
        }

        /**被AQS 模板方法调用尝试获取锁
         * 获取锁：被递归调用（通知）/没有等待的请求者/是第一个请求者，可以获得锁*/
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }
}
