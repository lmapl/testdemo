package disruptor.eventhandler;

import com.lmax.disruptor.EventFactory;

public class MyEventFactory implements EventFactory<MyEvent> {
    public MyEvent newInstance() {
        return new MyEvent();
    }
}