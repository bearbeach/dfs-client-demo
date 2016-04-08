package com.baofoo.dfs.server;

import com.baofoo.dfs.server.core.DfsServer;
import com.baofoo.dfs.server.core.ServerThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * DFS Socket server daemon thread
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since 1.7
 */
@Slf4j
@Component
public class SocketStart {

    /** DFS 服务端 */
    @Autowired
    private DfsServer dfsServer;

    /**
     * 初始化DFS Socket 服务，守护线程启动
     */
    @PostConstruct
    public void initData(){

        try{

            Thread serverThread = new ServerThread(dfsServer);
            serverThread.setDaemon(true);
            serverThread.setName("Dfs Server Socket");
            serverThread.start();

        }catch (Exception e){
            log.error(e.getMessage(),e);
            System.exit(1);
        }

    }

}
