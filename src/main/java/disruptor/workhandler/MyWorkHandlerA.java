package disruptor.workhandler;

import com.lmax.disruptor.WorkHandler;

public class MyWorkHandlerA implements WorkHandler<MyEvent> {


    @Override
    public void onEvent(MyEvent myEvent) throws Exception {
        System.out.println("Comsume MyWorkHandlerA : " + myEvent.getName());
        Thread.sleep(500);
    }
}