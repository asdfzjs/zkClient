package com.zillionfortune;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.zillionfortune.conf.Conf;

import org.apache.zookeeper.ZooKeeper;
  
@SuppressWarnings("deprecation")
public class zkClient {
  
    private static final int TIME_OUT = 3000;
    private static final String HOST = "192.168.210.62:2181,192.168.210.63:2181,192.168.210.64:2181,192.168.210.65:2181,192.168.210.66:2181";
    
    private static MyTimer commitTimer = new MyTimer();
    
    //MONGO
    private static final String HOST2 = "192.168.210.66";
    private static final int PORT = 27017;
    private static final String DB_NAME = "zjs";  
    private static final String COLLECTION = "offset";
    private static Mongo conn=null;  
    private static DB myDB=null;  
    private static DBCollection myCollection=null;  
      
    static{  
        try {  
            conn=new Mongo(HOST2,PORT);//建立数据库连接
            myDB=conn.getDB(DB_NAME);//使用zjs数据库 
            myCollection=myDB.getCollection(COLLECTION);
            Conf.Instance().Init(HOST, 1000);  
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (MongoException e) {  
            e.printStackTrace();  
        }  
    }  
    
    
    public static void insertOffset(String id1,String id2,String id3,String id4,String date){
    	 List<DBObject> dbList = new ArrayList<DBObject>();
    	 BasicDBObject doc1 = new BasicDBObject();
    	 doc1.put("partition_0", id1);
    	 doc1.put("partition_1", id2);
    	 doc1.put("partition_2", id3);
    	 doc1.put("partition_3", id4);
    	 doc1.put("datetime", date);
    	 dbList.add(doc1);
    	 myCollection.insert(dbList);
    }
    
    
    public static void main(String[] args) throws Exception{
        commitTimer.schedule(new CommitTimerTask(), new Date(System.currentTimeMillis() + 1 * 1000), 60 * 1000);
    } 
    
    private static class  CommitTimerTask extends TimerTask implements Serializable {
		private static final long serialVersionUID = 1L;
		ZooKeeper zookeeper;
		public void run() {
			try {
				zookeeper = new ZooKeeper(HOST, TIME_OUT, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String s = sdf.format(d);
			insertOffset(Conf.Instance().Get("ywlog.id.partition_0"), Conf.Instance().Get("ywlog.id.partition_1"), Conf.Instance().Get("ywlog.id.partition_2"), Conf.Instance().Get("ywlog.id.partition_3"),s);
		}
	}
    
   private  static  class MyTimer extends Timer implements Serializable {
    }
}