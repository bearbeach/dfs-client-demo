package com.baofoo.dfs.client.zookeeper;

import com.baofoo.dfs.client.model.DfsNode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * DFS 服务管理类
 *
 * @author 牧之
 * @version 1.0.0 createTime: 16/2/2
 */
public class DfsServerManager extends ZooKeeperOperator implements Watcher {

    private static final Logger log = LoggerFactory.getLogger(DfsServerManager.class);

    /** zookeeper 服务注册地址 */
    public static String zookeeperAddress;

    /** zookeeper DFS 根目录 */
    private final static String ROOT_PATH = "/dfsCoreRoot";

    /** zookeeper DFS Server 目录 */
    private final static String SERVER_PATH = ROOT_PATH + "/serverList";

    /** DFS Server 服务地址列表 */
    public static List<String> dfsServers = new ArrayList<String>();

    public DfsServerManager(String zookeeper){
        DfsServerManager.zookeeperAddress = zookeeper;
    }

    /**
     * 获取zookeeper中注册的DFS Server 服务地址
     *
     * @return DFS Server 服务地址列表
     */
    public List<String> getServerList(){

        if(!dfsServers.isEmpty()){
            log.debug("缓存中的server不为空,冲缓存中获取server列表.");
            return dfsServers;
        }

        return updateServerList();
    }

    /**
     * 更新DFS Server 服务地址(从zookeeper中获取最新注册服务)
     *
     * @return DFS Server 服务地址列表
     */
    public List<String> updateServerList(){

        try{

            connect(zookeeperAddress,this);

            log.debug("连接zookeeper 服务成功.");

            Stat stat = existed(ROOT_PATH);
            if(null == stat){
                log.error("Dfs Root 节点不存在.");
                return new ArrayList<String>();
            }

            stat = existed(SERVER_PATH);
            if(null == stat){
                log.error("Dfs Server 节点不存在.");
                return new ArrayList<String>();
            }

            log.debug("Dfs Server 节点存在,开始获取DFS 服务子节点数据.");

            List<String> nodes = getChild(SERVER_PATH,true);
            log.debug("Dfs Server共有{}个服务.", nodes.size());

            dfsServers.clear();

            for (String node : nodes) {
                byte [] data = getData(SERVER_PATH+ "/"+node);
                dfsServers.add(new String(data));
                log.debug("DFS服务节点:{},数据:{}",node,new String(data));
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        return dfsServers;

    }

    public List<DfsNode> getServerNodes(){

        List<DfsNode> nodes = new ArrayList<DfsNode>();

        try{

            List<String> results = getChild(SERVER_PATH,false);
            for (String path:results){

                DfsNode dfsNode = new DfsNode();
                byte [] data = getData(SERVER_PATH+"/"+path);
                String value = new String(data);
                dfsNode.setServerNode(SERVER_PATH+"/"+path);
                dfsNode.setNodeValue(value);
                nodes.add(dfsNode);
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        return nodes;
    }

    /**
     * 监听zookeeper DFS Server 节点的变化,实时更新DFS Server服务地址列表
     *
     * @param event DFS 事件
     */
    @Override
    public void process(WatchedEvent event) {
        log.warn("event :{},{},{}",event.getPath(),event.getType(),event.getState());
        if (event.getType() == Event.EventType.NodeChildrenChanged
                && (SERVER_PATH).equals(event.getPath())){
            updateServerList();
        }
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void connectMonitor(){

        try{

            boolean isAlive = getState();
            if(isAlive){
                return;
            }

            log.info("Zookeeper 连接失败，重新注册中...");

            close();

            updateServerList();

            log.info("Zookeeper 连接失败，重新注册成功 .");

        }catch (Exception e){
            log.info("Zookeeper  重新注册连接失败 :{},{}",e.getMessage(),e);
        }

    }

    public static void main(String args[]) throws InterruptedException {
        DfsServerManager serverManager = new DfsServerManager("10.0.20.175:2181");
        serverManager.getServerList();

        for (int i = 0 ; i < 100; i ++){
            serverManager.connectMonitor();
            Thread.sleep(1000 * 5);
        }

        Thread.sleep(1000 * 60 * 60);
    }

}
