package zookeeper.simple;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by ap
 * Create Date: 2018/6/7 11:30
 * Description: ${DESCRIPTION}
 */
public class MyWatcher implements Watcher {

    private ZooKeeper zooKeeper;

    public MyWatcher(ZooKeeper zooKeeper){
        this.zooKeeper = zooKeeper;
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
      //  System.out.println("已经触发了" + watchedEvent.getType() + "事件！" + " "+watchedEvent.getPath());
        try {
            if(watchedEvent.getType().equals(Event.EventType.NodeDataChanged)){
                System.out.println("new value = "+new String(zooKeeper.getData(watchedEvent.getPath(),true,null)));
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
