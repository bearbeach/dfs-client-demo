package com.baofoo.dfs.client.util;

import com.baofoo.Response;
import com.baofoo.dfs.client.core.DfsConfig;
import com.baofoo.dfs.client.core.DfsException;
import com.baofoo.dfs.client.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Socket 帮助类
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/27
 * @since 1.7
 */
public class SocketUtil {

    private static final Logger log = LoggerFactory.getLogger(SocketUtil.class);


    static String hosts [] = DfsConfig.get_server_host().split(",");


    public static Socket getSocketConnection() throws IOException {

        Map<String,String> failedHosts = new HashMap<String,String>();

        Socket socket;

        int port = DfsConfig.get_server_port();
        int index = new Random().nextInt(hosts.length);
        String host = hosts[index];

        try{

            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), DfsConfig.get_connect_timeout());

            log.info("连接服务端：{}：{}成功！", host, port);

        }catch (SocketException e){
            log.error("连接服务端：{}：{}异常，正在寻找新服务:{},{}",host,port,e.getMessage(),e);
            failedHosts.put(host, host);
            socket = retryConnect(failedHosts);
        }catch (SocketTimeoutException e){
            log.error("连接服务端：{}：{}超时，正在寻找新服务:{},{}",host,port,e.getMessage(),e);
            failedHosts.put(host, host);
            socket = retryConnect(failedHosts);
        }

        return socket;
    }

    private static Socket retryConnect(Map<String,String> failedHosts) throws IOException {
        if(hosts.length <2){
            return null;
        }

        int port = DfsConfig.get_server_port();

        for(int i=0;i< hosts.length;i++){
            String host = hosts[i];
            try{

                if(null != failedHosts.get(host)){
                    continue;
                }

                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), DfsConfig.get_connect_timeout());
                log.info("连接服务端：{}：{}成功！",host,port);
                return socket;

            }catch (SocketException e){
                log.error("连接服务端：{}：{}异常，正在寻找新服务:{},{}",host,port,e.getMessage(),e);
                failedHosts.put(host,host);
            }catch (SocketTimeoutException e){
                log.error("连接服务端：{}：{}超时，正在寻找新服务:{},{}",host,port,e.getMessage(),e);
                failedHosts.put(host, host);
            }
        }

        throw new DfsException(ErrorCode.NETWORK_ERROR,"找不到可用的服务端");

    }

    public static void closeSocket(Socket socket){
        try{
            if (socket != null) {
                socket.close();
            }
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
    }

    public static Response sendMessage(Object command){

        Socket socket = null;
        ObjectOutputStream oot = null;
        ObjectInputStream oin = null;

        Response response = null;

        try {

            socket = SocketUtil.getSocketConnection();
            oot = new ObjectOutputStream(socket.getOutputStream());
            oin = new ObjectInputStream(socket.getInputStream());
            oot.writeObject(command);
            oot.flush();

            response = (Response)oin.readObject();

        }catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new DfsException(ErrorCode.NETWORK_ERROR,e.getMessage());
        } finally{
            SocketUtil.closeSocket(socket);
            StreamUtil.closeStream(oot, oin);
         }

        return response;
    }

    public static void receiveMessage(ObjectOutputStream oot,Response response){
        try{
            if(null != oot){
                log.info("服务端响应信息：{}",response);
                oot.writeObject(response);
                oot.flush();
            }

        }catch (IOException e){
            log.error(e.getMessage(),e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        }
    }

}
