package com.baofoo.dfs.server;

import lombok.extern.slf4j.Slf4j;
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

    @Autowired(required = false)
    private SocketStart socketStart;

    @Test
    public void starting() throws InterruptedException {
        socketStart.initData();
        Thread.sleep(1000*60);
    }

}
