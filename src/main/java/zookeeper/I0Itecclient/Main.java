package zookeeper.I0Itecclient;

import org.I0Itec.zkclient.ContentWatcher;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

/**
 * Created by ap
 * Create Date: 2018/6/8 15:17
 * Description: ${DESCRIPTION}
 */
public class Main {

    public static void main(String[] args) throws Exception{
        ZkClient zkClient = new ZkClient("192.168.98.211:21810");
        //String aaa = zkClient.create("/testRootPath11", "testRootPath4Value", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //zkClient.writeData("/testRootPath10","000",-1);
        //zkClient.readData("/testRootPath10",null);


       /* ContentWatcher<String> contentWatcher = new ContentWatcher<>(zkClient,"/ContentWatcherTest");
        contentWatcher.start();
        zkClient.readData("/testRootPath11",null);
        long start = System.currentTimeMillis();
        while (true){
            long end  = System.currentTimeMillis();
            if((end - start) >5000){
                break;
            }
        }
        System.out.println(contentWatcher.getContent());*/

        final ContentWatcher<String> watcher = new ContentWatcher<String>(zkClient, "ContentWatcherTest");
        watcher.start();
        System.out.println(watcher.getContent());

        // update the content
        zkClient.writeData("ContentWatcherTest", "b");

        /*String contentFromWatcher = TestUtil.waitUntil("b", new Callable<String>() {

            @Override
            public String call() throws Exception {
                return watcher.getContent();
            }
        }, TimeUnit.SECONDS, 5);*/
        long start = System.currentTimeMillis();
        while (true){
            long end  = System.currentTimeMillis();
            if((end - start) >500){
                break;
            }
        }
        System.out.println(watcher.getContent());
        watcher.stop();
    }
}
