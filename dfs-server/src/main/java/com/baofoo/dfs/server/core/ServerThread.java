package com.baofoo.dfs.server.core;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

/**
 * DFS socket server daemon thread
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 */
@Slf4j
@AllArgsConstructor
public class ServerThread extends Thread{

    private DfsServer dfsServer;

    @Override
    public void run() {

        try {

            dfsServer.startUp();

        } catch (IOException e) {
            log.error("DFS Server start exception ,System exit.{},{}",e.getMessage(),e);
            System.exit(0);
        }
    }
}
