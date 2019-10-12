package disruptor.ringbuffer;

import com.lmax.disruptor.RingBuffer;
import disruptor.demotest.LongEvent;
import disruptor.demotest.LongEventFactory;

/**
 * ringBuffer环状覆盖保存
 */
public class RingBuufferTestMain {
    public static void main(String[] args) throws Exception {
        // The factory for the event
        LongEventFactory factory = new LongEventFactory();
        RingBuffer<LongEvent> ringBuffer = RingBuffer.createSingleProducer(factory, 8);

        for (long l = 1; l < 100; l++) {
            System.out.println("insert into   name"+l);
            long sequence = ringBuffer.next();  // Grab the next sequence
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