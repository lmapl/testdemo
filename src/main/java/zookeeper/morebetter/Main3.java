package zookeeper.morebetter;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

/**
 * Created by ap
 * Create Date: 2018/6/7 11:08
 * Description: ${DESCRIPTION}
 */
public class Main3 {
    public static void main(String[] args) throws Exception{

        ZookeeperClient zk1 = new ZookeeperClient("192.168.98.211:" + "21810");
        System.out.println("主程序,建立连接完毕");

        //zk1.create("/ContentWatcherTest", "testRootPath4Value", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println("主程序 查询修改前数据"+ zk1.getData("/testChildPathOne"));
        // 修改子目录节点数据

        System.out.println("主程序 查询修改前数据"+ zk1.getData("/testChildPathOne"));

        // 关闭连接
        zk1.close();
    }

}
