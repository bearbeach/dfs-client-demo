package com.baofoo.dfs.client.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * zookeeper 公共操作类
 *
 * @author 牧之
 * @version 1.0.0 createTime: 16/2/2
 */
public class ZooKeeperOperator extends AbstractZooKeeper {

    public void createPath(String path,byte[] data,CreateMode createMode)throws KeeperException, InterruptedException{
        this.zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,createMode);
    }

    public List<String> getChild(String path,boolean watcher) throws KeeperException, InterruptedException{
        try{
            return this.zooKeeper.getChildren(path, watcher);
        }catch (KeeperException.NoNodeException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    public byte[] getData(String path) throws KeeperException, InterruptedException {
        return  this.zooKeeper.getData(path, false,null);
    }

    public Stat existed(String path) throws KeeperException, InterruptedException {
        return this.zooKeeper.exists(path,false);
    }

}
