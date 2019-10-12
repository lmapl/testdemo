package disruptor.eventhandler;

import com.lmax.disruptor.EventHandler;

public class MyEventHandlerC implements EventHandler<MyEvent> {
    public void onEvent(MyEvent myEvent, long l, boolean b) throws Exception {
        //Thread.sleep(5000000);
        System.out.println("Comsume Event C : " + myEvent.getName());
    }
}