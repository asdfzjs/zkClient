package com.zillionfortune.watcher;

import java.nio.charset.Charset;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.RetryNTimes;
//Path Cache：监视一个路径下1）孩子结点的创建、2）删除，3）以及结点数据的更新。产生的事件会传递给注册的PathChildrenCacheListener。
//Node Cache：监视一个结点的创建、更新、删除，并将结点的数据缓存在本地。
//Tree Cache：Path Cache和Node Cache的“合体”，监视路径下的创建、更新、删除事件，并缓存路径下所有孩子结点的数据。
public class zkWatcher {
	private static final int TIME_OUT = 3000;
	 private static Charset charset = Charset.forName("utf-8");
	 private static CuratorFramework client;
	 private static final String HOST = "192.168.210.62:2181,192.168.210.63:2181,192.168.210.64:2181,192.168.210.65:2181,192.168.210.66:2181";
	    static{  
	    	CuratorFramework client = CuratorFrameworkFactory.newClient(
					HOST,
	                new RetryNTimes(10, 5000)
	        );
	        client.start();
	    } 
	public static void main(String[] args) throws Exception {
        // 2.Register watcher
        PathChildrenCache watcher = new PathChildrenCache(
                client,
                "/zjs",
                true    // if cache data
        );
        watcher.getListenable().addListener((client1, event) -> {
            ChildData data = event.getData();
            if (data == null) {
                System.out.println("No data in event[" + event + "]");
            } else {
                System.out.println("Receive event: "
                        + "type=[" + event.getType() + "]"
                        + ", path=[" + data.getPath() + "]"
                        + ", data=[" + new String(data.getData()) + "]"
                        + ", stat=[" + data.getStat() + "]");
            }
        });
        watcher.start(StartMode.BUILD_INITIAL_CACHE);
        System.out.println("Register zk watcher successfully!");

        Thread.sleep(Integer.MAX_VALUE);
    }
}
