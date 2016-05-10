package com.baofoo.dfs.client.zookeeper;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.ZooKeeper.States;

/**
 * zookeeper 公共类
 *
 * @author 牧之
 * @version 1.0.0 createTime: 16/2/2
 */
public class AbstractZooKeeper {

    private static final int SESSION_TIME   = 1000 * 2;

    protected static ZooKeeper zooKeeper;

    public void connect(String hosts,Watcher watcher) throws IOException, InterruptedException{
        if(zooKeeper != null && zooKeeper.getState().isAlive()){
            return;
        }
        zooKeeper = new ZooKeeper(hosts,SESSION_TIME,watcher);
    }

    public boolean getState(){
        try{
            States states = zooKeeper.getState();
            return states.isAlive();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void close(){

        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
