package disruptor.demotest;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class LongEventProducer
{
    private final RingBuffer<LongEvent> eventringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer)
    {
        this.eventringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb)
    {
        long sequence = eventringBuffer.next();  // Grab the next sequence
        try
        {
            LongEvent event = eventringBuffer.get(sequence); // Get the entry in the Disruptor
                                                        // for the sequence
            event.setValue(bb.getLong(0));  // Fill with data
        }
        finally
        {
            eventringBuffer.publish(sequence);
        }
    }
}