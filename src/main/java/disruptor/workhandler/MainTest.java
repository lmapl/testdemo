package disruptor.workhandler;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ap
 * Create Date: 2018/6/5 16:37
 * Description: ${DESCRIPTION}
 */
public class MainTest {
    public static void main(String[] args){
        EventFactory<MyEvent> myEventFactory = new MyEventFactory();
        Executor executor = Executors.newCachedThreadPool();
        int ringBufferSize = 8;

        Disruptor<MyEvent> disruptor = new Disruptor<>(myEventFactory,ringBufferSize,executor, ProducerType.MULTI,new BlockingWaitStrategy());
        MyWorkHandlerA a = new MyWorkHandlerA();
        MyWorkHandlerB b = new MyWorkHandlerB();
        disruptor.handleEventsWithWorkerPool(b,a);
        RingBuffer<MyEvent> ringBuffer = disruptor.start();
        for(int i=0; i<100; i++) {
            //System.out.println("producer :" +" name"+i);
            long sequence = ringBuffer.next();
            try {
                MyEvent myEvent = ringBuffer.get(sequence);
                myEvent.setName("name"+i);
                myEvent.setValue(i);
            } finally {
                ringBuffer.publish(sequence);
            }
        }
        //ringBuffer = disruptor.start();
        System.out.println("prod end ..");
        disruptor.shutdown();
    }
}
