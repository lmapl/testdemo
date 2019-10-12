package zookeeper.simple;

import org.apache.zookeeper.ZooKeeper;

/**
 * Created by ap
 * Create Date: 2018/6/7 11:08
 * Description: ${DESCRIPTION}
 */
public class Main2 {
    public static void main(String[] args) throws Exception{

        ZooKeeper zk1 = new ZooKeeper("192.168.98.211:" + "21810",
                200, null);

        MyWatcher myWatcher = new MyWatcher(zk1);
        // 创建一个与服务器的连接
        ZooKeeper zk = new ZooKeeper("192.168.98.211:" + "21810",
                200, myWatcher);
        System.out.println("主程序 "+ new String(zk.getData("/testRootPath/testChildPathOne",true,null)));
        // 修改子目录节点数据
        zk.setData("/testRootPath/testChildPathOne","123".getBytes(),-1);
        //System.out.println("2 "+ new String(zk.getData("/testRootPath/testChildPathOne",false,null)));
        zk.setData("/testRootPath/testChildPathOne","1234".getBytes(),-1);

        // 关闭连接
        zk.close();
    }

}
