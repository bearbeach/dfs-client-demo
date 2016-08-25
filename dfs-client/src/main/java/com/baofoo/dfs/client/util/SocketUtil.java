package com.baofoo.dfs.client.util;

import com.baofoo.Response;
import com.baofoo.dfs.client.core.DfsConfig;
import com.baofoo.dfs.client.core.DfsException;
import com.baofoo.dfs.client.enums.ErrorCode;
import com.baofoo.dfs.client.model.InsertReqDTO;
import com.baofoo.dfs.client.model.InsertReqSTO;
import com.baofoo.dfs.client.zookeeper.DfsServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Random;

/**
 * Socket 帮助类
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/27
 */
public class SocketUtil {

    private static final Logger log = LoggerFactory.getLogger(SocketUtil.class);

    /**
     * 获取DFS Server Socket连接,Server地址随机从zookeeper中获取
     *
     * @return Socket连接
     * @throws IOException
     */
    public static Socket getSocketConnection() throws IOException {

        DfsServerManager dfsServerManager = new DfsServerManager(DfsConfig.get_zookeeper_address());
        List<String> serverHost = dfsServerManager.getServerList();

        if(serverHost.isEmpty()){
            throw new DfsException(ErrorCode.NO_SERVER_PROVIDER);
        }

        String [] hosts = serverHost.toArray(new String [serverHost.size()]);

        Socket socket;

        int index = new Random().nextInt(hosts.length);
        String host = hosts[index];

        int port = Integer.valueOf(host.split(":")[1]);
        host = host.split(":")[0];
        try{

            String localIP = IPHelper.getLocalIP();
            if(localIP.equals(host)){
                host = "127.0.0.1";
            }
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), DfsConfig.get_connect_timeout());

            log.info("连接服务端：{}：{}成功！", host, port);

        }catch (SocketException e){
            log.error("连接服务端：{}：{}异常，正在寻找新服务:{},{}",host,port,e.getMessage(),e);
            socket = retryConnect();
        }catch (SocketTimeoutException e){
            log.error("连接服务端：{}：{}超时，正在寻找新服务:{},{}",host,port,e.getMessage(),e);
            socket = retryConnect();
        }

        return socket;
    }

    /**
     * 连接重试
     *
     * @return Socket连接
     * @throws IOException
     */
    private static Socket retryConnect() throws IOException {

        DfsServerManager dfsServerManager = new DfsServerManager(DfsConfig.get_zookeeper_address());
        List<String> serverHost = dfsServerManager.getServerList();
        String [] hosts = serverHost.toArray(new String [serverHost.size()]);

        if(hosts.length <1){
            return null;
        }

        for (String currentHost : hosts) {
            String host = currentHost;

            int port = Integer.valueOf(host.split(":")[1]);
            host = host.split(":")[0];

            try {

                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), DfsConfig.get_connect_timeout());
                log.info("连接服务端：{}：{}成功！", host, port);
                return socket;

            } catch (SocketException e) {
                log.error("连接服务端：{}：{}异常，正在寻找新服务:{},{}", host, port, e.getMessage(), e);
            } catch (SocketTimeoutException e) {
                log.error("连接服务端：{}：{}超时，正在寻找新服务:{},{}", host, port, e.getMessage(), e);
            }
        }

        throw new DfsException(ErrorCode.NETWORK_ERROR,"找不到可用的服务端");

    }

    /**
     * 关闭Socket连接
     *
     * @param socket Socket连接
     */
    public static void closeSocket(Socket socket){
        try{
            if (socket != null) {
                socket.close();
            }
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
    }

    /**
     * 向Server 发送消息命令
     *
     * @param command 消息命令
     * @return 响应消息
     */
    public static Response sendMessage(Object command){

        Socket socket = null;
        ObjectOutputStream oot = null;
        ObjectInputStream oin = null;

        Response response = null;

        InsertReqSTO reqSTO = null;

        try {

            if (command.getClass().getName().equals("com.baofoo.dfs.client.model.InsertReqDTO")) {

                InsertReqDTO dto = (InsertReqDTO)command;
                reqSTO = new InsertReqSTO();
                reqSTO.setDeadline(dto.getDeadline());
                reqSTO.setFileName(dto.getFileName());
                reqSTO.setFileDate(dto.getFileDate());
                reqSTO.setFilePath(dto.getFilePath());
                reqSTO.setOrgCode(dto.getOrgCode());
                reqSTO.setFileSize(dto.getFileSize());
                reqSTO.setRemark(dto.getRemark());
                reqSTO.setOperation(dto.getOperation());
                reqSTO.setFileGroup(dto.getFileGroup().getCode());

                command = reqSTO;
            }

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

    /**
     * 接受Server 返回消息
     *
     * @param oot   ObjectOutputStream
     * @param response Response
     */
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
