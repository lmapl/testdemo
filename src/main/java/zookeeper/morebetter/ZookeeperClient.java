package zookeeper.morebetter;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Created by ap
 * Create Date: 2018/6/8 14:08
 * Description: ${DESCRIPTION}
 */
public class ZookeeperClient implements Watcher {

    ZooKeeper zk;

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("watcher type"+watchedEvent.getType());
        try {
            if(watchedEvent.getType().equals(Event.EventType.NodeDataChanged)){
                System.out.println("watcher 更新后的值 = "+new String(zk.getData(watchedEvent.getPath(),true,null)));
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ZookeeperClient(String ipport) throws Exception{
        zk = new ZooKeeper(ipport,200,this);
    }

    public String getData(String path) throws Exception{
        return  new String(zk.getData(path,true,null));
    }

    public Stat setData(String path,String value,int version) throws Exception{
        return  zk.setData(path,value.getBytes(),version);
    }

    public void create(String path, String data, List<ACL> acl, CreateMode createMode) throws Exception{
        zk.create(path, data.getBytes(), acl, createMode);
    }

    public void close() throws Exception{
         zk.close();
    }




}
