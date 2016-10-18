package com.zillionfortune.transaction;

import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

public class zkTransaction {
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
	public static void main(String[] args) {  
        try {  
            // 开启事务  
            CuratorTransaction transaction = client.inTransaction();  
  
            Collection<CuratorTransactionResult> results = transaction.create()  
                    .forPath("/a/path", "some data".getBytes()).and().setData()  
                    .forPath("/another/path", "other data".getBytes()).and().delete().forPath("/yet/another/path")  
                    .and().commit();  
  
            for (CuratorTransactionResult result : results) {  
                System.out.println(result.getForPath() + " - " + result.getType());  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            // 释放客户端连接  
            CloseableUtils.closeQuietly(client);  
        }  
  
    }  
}
