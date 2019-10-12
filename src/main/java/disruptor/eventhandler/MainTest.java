package disruptor.eventhandler;

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
 * event执行过程：disruptor.start -- > EventProcessorInfo.start -->BatchEventProcessor#run -->
 * BatchEventProcessor#processEvents --> 自定义eventHandler.onEvent
 */
public class MainTest {
    public static void main(String[] args){
        EventFactory<MyEvent> myEventFactory = new MyEventFactory();
        Executor executor = Executors.newCachedThreadPool();
        int ringBufferSize = 8;

        Disruptor<MyEvent> disruptor = new Disruptor<>(myEventFactory,ringBufferSize,executor, ProducerType.MULTI,new BlockingWaitStrategy());
        EventHandler<MyEvent> b = new MyEventHandlerB();
       // EventHandler<MyEvent> c = new MyEventHandlerC();
      //  EventHandler<MyEvent> d = new MyEventHandlerD();
        disruptor.handleEventsWith(b);
        /*SequenceBarrier sequenceBarrier2 = disruptor.handleEventsWith(b,c).asSequenceBarrier();
        BatchEventProcessor processord = new BatchEventProcessor(disruptor.getRingBuffer(),sequenceBarrier2,d);
        disruptor.handleEventsWith(processord);*/
        // disruptor.after(b,c).handleEventsWith(d);              // 此行能代替上两行的程序逻辑
        RingBuffer<MyEvent> ringBuffer = disruptor.start();    // 启动Disruptor
        for(int i=0; i<100; i++) {
            System.out.println("producer :" +" name"+i);
            long sequence = ringBuffer.next();                 // 申请位置
            try {
                MyEvent myEvent = ringBuffer.get(sequence);
                myEvent.setName("name"+i);
                myEvent.setValue(i);                           // 放置数据
            } finally {
                ringBuffer.publish(sequence);                  // 提交，如果不提交完成事件会一直阻塞
            }
            try{
                Thread.sleep(5000);
            }catch (Exception e){
            }
        }
        System.out.println("prod end ..");
        disruptor.shutdown();
    }
}
