package com.zillionfortune;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;


import org.apache.zookeeper.ZooKeeper;
  
public class zkClient {
  
    private static final int TIME_OUT = 3000;
    private static final String HOST = "192.168.210.62:2181,192.168.210.63:2181,192.168.210.64:2181,192.168.210.65:2181,192.168.210.66:2181";
    
    private static MyTimer commitTimer = new MyTimer();
    
    public static void display(String id1,String id2,String id3,String id4){
    	 System.out.println(id1);
    	 System.out.println(id2);
    	 System.out.println(id3);
    	 System.out.println(id4);
    }
    
    
    public static void main(String[] args) throws Exception{
        
        commitTimer.schedule(new CommitTimerTask(), new Date(System.currentTimeMillis() + 1 * 1000), 1 * 1000);
        
//        System.out.println("=========创建节点===========");
//        if(zookeeper.exists("/test", false) == null)
//        {
//            zookeeper.create("/test", "znode1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        }
//        System.out.println("=============查看节点是否安装成功===============");
//        System.out.println(new String(zookeeper.getData("/test", false, null)));
//         
//        System.out.println("=========修改节点的数据==========");
//        String data = "zNode2";
//        zookeeper.setData("/test", data.getBytes(), -1);
//         
//        System.out.println("========查看修改的节点是否成功=========");
//        System.out.println(new String(zookeeper.getData("/test", false, null)));
//         
//        System.out.println("=======删除节点==========");
//        zookeeper.delete("/test", -1);
//         
//        System.out.println("==========查看节点是否被删除============");
//        System.out.println("节点状态：" + zookeeper.exists("/test", false));
        
      // zookeeper.close();
    } 
    
    private static class  CommitTimerTask extends TimerTask implements Serializable {
    	ZooKeeper zookeeper;
		public void run() {
			try {
				zookeeper = new ZooKeeper(HOST, TIME_OUT, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				display(new String(zookeeper.getData("/ywlog/id/partition_0", false, null)), new String(zookeeper.getData("/ywlog/id/partition_1", false, null)), new String(zookeeper.getData("/ywlog/id/partition_2", false, null)), new String(zookeeper.getData("/ywlog/id/partition_3", false, null)));
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
    
   private  static  class MyTimer extends Timer implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
    }
}