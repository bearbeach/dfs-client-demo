package com.baofoo.dfs.server.core;

import com.baofoo.dfs.server.manager.DfsFileManager;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * DFS 服务端
 *
 * @author muzhi
 * @version 1.0.0 createTime: 15/11/25 下午13:02
 * @since 1.7
 */
@Slf4j
@Setter
@Component
public class DfsServer {

    /** DFS 服务 socket 端口 */
    @Value(value = "${dfsServerPort}")
    private int _dfs_server_port;

    /** DFS 文件记录管理接口 */
    @Autowired
    private DfsFileManager dfsFileManager;

    @Autowired
    private ServerRegistry serverRegistry;

    /**
     * 启动DFS socket 服务
     *
     * @throws IOException
     */
    public void startUp() throws IOException {

        log.info("socket server:{} starting ",_dfs_server_port);

        ServerSocket server = new ServerSocket(_dfs_server_port);

        serverRegistry.registry();

        while (true) {

            Socket socket = server.accept();
            new Thread(new DfsHandler(socket,dfsFileManager)).start();

        }

    }

}
