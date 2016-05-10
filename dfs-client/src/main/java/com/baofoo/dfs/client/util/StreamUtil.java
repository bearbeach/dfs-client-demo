package com.baofoo.dfs.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 公用流处理帮助类
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/27
 */
public class StreamUtil {

    private static final Logger log = LoggerFactory.getLogger(StreamUtil.class);

    public static void closeStream(OutputStream os,InputStream in){
        try{
            if(null != os){
                os.close();
            }
            if(null != in){
                in.close();
            }
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
    }

}
