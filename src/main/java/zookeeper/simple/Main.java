package zookeeper.simple;

import org.apache.zookeeper.*;

/**
 * Created by ap
 * Create Date: 2018/6/7 11:08
 * Description: ${DESCRIPTION}
 */
public class Main {
    public static void main(String[] args) throws Exception{
        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper("192.168.98.211:" + "21810",
                200, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType()+event.getPath() + "事件！");
            }
        });
        // 创建一个目录节点
        //zk.create("/testRootPath", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //System.out.println("1 "+ new String(zk.getData("/testRootPath",true,null)));

        // 创建一个子目录节点
        //zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);

        //System.out.println("2 "+ new String(zk.getData("/testRootPath/testChildPathOne",true,null)));

        // 修改子目录节点数据
        zk.setData("/testRootPath/testChildPathOne","123".getBytes(),-1);
        //System.out.println("2 "+ new String(zk.getData("/testRootPath/testChildPathOne",false,null)));
        zk.setData("/testRootPath/testChildPathOne","1234".getBytes(),-1);
        // 创建另外一个子目录节点
       // zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(),
               // ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        //System.out.println("3  "+ new String(zk.getData("/testRootPath/testChildPathTwo",true,null)));

        //zk.setData("/testRootPath/testChildPathTwo","xiugai ".getBytes(),-1);


        // 删除子目录节点
       // zk.delete("/testRootPath/testChildPathTwo",-1);
       // zk.delete("/testRootPath/testChildPathOne",-1);
        // 删除父目录节点
        //zk.delete("/testRootPath",-1);
        // 关闭连接
        zk.close();
    }



/*    public static void main(String[] args) throws Exception{
        //监控用
        ZooKeeper watchZk = new ZooKeeper("192.168.98.211:" + "21810",
                200, null);

        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper("192.168.98.211:" + "21810",
                200, new MyWatcher(watchZk) );

        // 创建一个目录节点
        zk.create("/testRootPath", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        // 创建一个子目录节点
        zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        //System.out.println(new String(zk.getData("/testRootPath",false,null)));
        // 取出子目录节点列表
        System.out.println(zk.getChildren("/testRootPath",true));
        // 修改子目录节点数据
        zk.setData("/testRootPath/testChildPathOne","123456".getBytes(),-1);
        System.out.println("目录节点状态：["+zk.exists("/testRootPath",true)+"]");
        // 创建另外一个子目录节点
        zk.create("/testRootPath/testChildPathTwo", "2345678".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        //System.out.println(new String(zk.getData("/testRootPath/testChildPathTwo",true,null)));
        // 删除子目录节点
        zk.delete("/testRootPath/testChildPathTwo",-1);
        zk.delete("/testRootPath/testChildPathOne",-1);
        // 删除父目录节点
        zk.delete("/testRootPath",-1);
        // 关闭连接
        zk.close();
    }*/


}
