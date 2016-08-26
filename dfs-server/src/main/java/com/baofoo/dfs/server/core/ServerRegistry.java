package com.baofoo.dfs.server.core;

import com.baofoo.dfs.client.core.DfsException;
import com.baofoo.dfs.client.enums.ErrorCode;
import com.baofoo.dfs.client.zookeeper.ZooKeeperOperator;
import com.baofoo.dfs.client.util.IPHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * FastDfs Server zookeeper服务注册
 *
 * @author 牧之
 * @version 1.0.0 createTime: 16/2/2
 */
@Slf4j
@Component
public class ServerRegistry implements Watcher{

    @Value(value = "${zookeeperAddress}")
    private String _zookeeper_address;

    /** DFS 服务 socket 端口 */
    @Value(value = "${dfsServerPort}")
    private int _dfs_server_port;

    private final static String ROOT_PATH = "/dfsCoreRoot";

    private final static String SERVER_PATH = ROOT_PATH + "/serverList";

    static ZooKeeperOperator zooKeeperOperator = new ZooKeeperOperator();

    private final static String localIP = IPHelper.getLocalIP();

    private final static String SERVER_NODE = SERVER_PATH + "/server_" + localIP.replaceAll("\\.","_");

    /**
     * 注册服务到zookeeper
     */
    public void registry(){

        try{

            zooKeeperOperator.connect(_zookeeper_address,this);
            log.info("连接到 zookeeper，zk 地址为：{}",_zookeeper_address);

            Stat stat = zooKeeperOperator.existed(ROOT_PATH);
            if(stat == null){
                zooKeeperOperator.createPath(ROOT_PATH, ROOT_PATH.getBytes(), CreateMode.PERSISTENT);
                log.info("zookeeper 节点:{}不存在，创建节点成功", ROOT_PATH);
            }


            Stat result = zooKeeperOperator.existed(SERVER_PATH);

            if(result == null){
                zooKeeperOperator.createPath(SERVER_PATH, SERVER_PATH.getBytes(), CreateMode.PERSISTENT);
                log.info("zookeeper 节点:{}不存在，创建节点成功", SERVER_PATH);
            }

            Stat serverNodeStat = zooKeeperOperator.existed(SERVER_NODE);

            String DFS_SERVER_HOST = localIP +":" + _dfs_server_port;

            if(serverNodeStat == null){
                zooKeeperOperator.createPath(SERVER_NODE, DFS_SERVER_HOST.getBytes(), CreateMode.EPHEMERAL);
                log.info("zookeeper 节点:{}不存在，创建节点成功", SERVER_NODE);
            }

        }catch (Exception e){
            log.error("注册zookeeper服务异常，系统将退出。{},{}",e.getMessage(),e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        }

    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void connectMonitor(){

        try{

            boolean isAlive = zooKeeperOperator.getState();
            if(isAlive){
                log.debug("zookeeper 服务正常无需重连...", localIP);
                return;
            }

            log.info("DFS 服务节点：{} 连接失败，重新注册中...", localIP);

            zooKeeperOperator.close();

            registry();

            log.info("DFS 服务节点：{} 连接失败，重新注册成功 .",localIP);

        }catch (Exception e){
            log.info("DFS 服务节点：{} 重新注册连接失败 :{},{}",localIP,e.getMessage(),e);
        }
    }

}
