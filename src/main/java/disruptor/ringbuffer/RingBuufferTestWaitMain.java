package disruptor.ringbuffer;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import disruptor.demotest.LongEvent;
import disruptor.demotest.LongEventFactory;
import disruptor.demotest.LongEventHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ringBuffer环状覆盖保存
 * 阻塞等待
 */
public class RingBuufferTestWaitMain {
    public static void main(String[] args) throws Exception {
        // The factory for the event
        Executor executor = Executors.newCachedThreadPool();

        // The factory for the event
        LongEventFactory factory = new LongEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 8;

        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

        // Connect the handler
        disruptor.handleEventsWith(new LongEventHandler());

        // Start the Disruptor, starts all threads running
        // disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        for (long l = 1; l < 100; l++) {
            System.out.println("insert into   name"+l);
            long sequence = ringBuffer.next();  // Grab the next sequence
            //System.out.println("insert into   name"+l);
            //获取ringBuffer中对应索引为的元素
            LongEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            //对元素赋值，传递给后续逻辑处理
            event.setName("name"+l);
            event.setValue(l);

            //发布到可执行
            ringBuffer.publish(sequence);

            Thread.sleep(500);

            //打印ringBuffer现状
            for (int i = 0; i < ringBuffer.getBufferSize(); i++) {
                LongEvent longEvent = ringBuffer.get(i);
                System.out.print(i + "==" + longEvent.getName() + " ; ");
            }
            System.out.println("");


        }


    }
}