package listener.demo;

import java.util.EventObject;

class MyEvent extends EventObject {
    private Object obj;
    //此监听对象可以是自定义对象
    private String sName;

    public MyEvent(Object source, String sName) {
        super(source);
        this.obj = source;
        this.sName = sName;
    }

    public Object getObj() {
        return obj;
    }

    public String getsName() {
        return sName;
    }
}
