package com.baofoo.dfs.server;

import com.baofoo.dfs.server.scheduled.TempFileProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类描述
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/26
 * @since 1.7
 */
@Slf4j
public class TestSocket extends BaseTest{

    @Autowired
    TempFileProcessor tempFileProcessor;

    @Test
    public void starting(){

        try{
            tempFileProcessor.scanFileRecord();

            Thread.sleep(1000 * 60 * 60);

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }

}
