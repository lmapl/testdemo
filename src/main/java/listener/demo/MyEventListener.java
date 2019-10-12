package listener.demo;

import java.util.EventListener;

interface MyEventListener extends EventListener
{
    void handleEvent (MyEvent me);
}