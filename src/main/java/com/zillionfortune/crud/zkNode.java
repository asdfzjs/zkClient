package com.zillionfortune.crud;

import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.data.Stat;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class zkNode {
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
	    private static final String PATH = "/crud";  
	  
	    public static void main(String[] args) {  
	        try {  
	            client.create().forPath(PATH, "I love messi".getBytes());  
	            byte[] bs = client.getData().forPath(PATH);  
	            System.out.println("新建的节点，data为:" + new String(bs));  
	  
	            client.setData().forPath(PATH, "I love football".getBytes());  
	  
	            //由于是在background模式下获取的data，此时的bs可能为null  
	            byte[] bs2 = client.getData().watched().inBackground().forPath(PATH);  
	            System.out.println("修改后的data为" + new String(bs2 != null ? bs2 : new byte[0]));  
	  
	            client.delete().forPath(PATH);  
	            Stat stat = client.checkExists().forPath(PATH);  
	            // Stat就是对zonde所有属性的一个映射， stat=null表示节点不存在！  
	            System.out.println(stat);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            CloseableUtils.closeQuietly(client);  
	        }  
	    }  
}
