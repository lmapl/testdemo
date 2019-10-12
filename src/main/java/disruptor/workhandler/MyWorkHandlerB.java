package disruptor.workhandler;

import com.lmax.disruptor.WorkHandler;

public class MyWorkHandlerB implements WorkHandler<MyEvent> {


    @Override
    public void onEvent(MyEvent myEvent) throws Exception {
        System.out.println("Comsume MyWorkHandlerB : " + myEvent.getName());
        Thread.sleep(50);
    }
}