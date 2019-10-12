package disruptor.eventhandler;

import com.lmax.disruptor.EventHandler;

public class MyEventHandlerD implements EventHandler<MyEvent> {
    public void onEvent(MyEvent myEvent, long l, boolean b) throws Exception {
        //Thread.sleep(1000);
        System.out.println("Comsume Event D : " + myEvent.getValue());
    }
}