package com.baofoo.dfs.client.util;

import com.baofoo.dfs.client.core.DfsConfig;
import com.baofoo.dfs.client.core.DfsException;
import com.baofoo.dfs.client.core.pool.FastDFSFactory;
import com.baofoo.dfs.client.enums.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * FastDFS 操作类
 *
 * @author muzhi
 * @version 1.0.0 createTime: 15/11/25 下午13:02
 */
public class FastDFSUtil {

    private static final Logger log = LoggerFactory.getLogger(FastDFSUtil.class);

    private static ObjectPool<TrackerServer> pool;

    public static final String KEY_GROUP = "GROUP_NAME";

    public static final String KEY_REMOTE_FILE_NAME = "REMOTE_FILE_NAME";

    /** 有界阻塞队列 */
    public static BlockingQueue<String> connectQueue = new LinkedBlockingQueue<String>(20);

    private static String httpServer;

    /**
     * 初始化DFS配置
     */
    public static void init() {
        try {

            log.info("DFS beginning start .....");

            if(StringUtils.isBlank(DfsConfig.get_tracker_adds())){
                log.error("DFS init error:please check the properties 'tracker' .");
                System.exit(1);
            }

            String [] trackers = DfsConfig.get_tracker_adds().split(",");

            log.info("DFS init find {} tracker server .",trackers.length);

            InetSocketAddress[] tracker_servers = new InetSocketAddress[trackers.length];
            int position = 0;
            for(String tracker : trackers){
                String address = tracker.split(":")[0];
                int port = Integer.valueOf(tracker.split(":")[1]);
                tracker_servers[position] = new InetSocketAddress(address, port);
                position ++;
            }

            TrackerGroup trackerGroup = new TrackerGroup(tracker_servers);

            ClientGlobal.setG_tracker_group(trackerGroup);
            ClientGlobal.setG_charset(DfsConfig._input_charset);
            ClientGlobal.setG_secret_key(DfsConfig.get_secret_key());
            ClientGlobal.setG_tracker_http_port(DfsConfig.get_tracker_http_port());
            ClientGlobal.setG_connect_timeout(DfsConfig.get_connect_timeout());
            ClientGlobal.setG_network_timeout(DfsConfig.get_network_timeout());
            ClientGlobal.setG_anti_steal_token(true);

            initPool();

            log.info("DFS init finished success");

        } catch (Exception e) {
            log.error("FastDFSUtil init exception: {},{}", e.getMessage(),e);
            System.exit(1);
        }
      }

    /**
     * 初始化连接池
     */
    private static void initPool() {
        PooledObjectFactory<TrackerServer> factory = new FastDFSFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(DfsConfig.get_max_idle());
        config.setMinIdle(DfsConfig.get_min_idle());
        config.setMaxTotal(DfsConfig.get_max_total());
        connectQueue = new LinkedBlockingQueue<String>(DfsConfig.get_max_idle());
        pool = new GenericObjectPool<TrackerServer>(factory, config);
    }

    /**
     * 下载文件
     *
     * @param dfsPath           DFS 路径
     * @param localFileName     下载到本地的文件名
     * @return int              文件下载结果
     */
    public static int download(String dfsPath, String localFileName) {

        int result = -1;
        TrackerServer trackerServer = null;
        try {

            trackerServer = pool.borrowObject();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            // 下载文件
            result = storageClient.download_file("group1", dfsPath, localFileName);

            log.debug("download result:remoteFilename: {}, localFileName: {}", dfsPath, localFileName);

        } catch (Exception e) {
            log.error("下载文件失败,remoteFilename:{},localFileName:{},{},{}"
                    ,dfsPath,localFileName,e.getMessage(),e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        } finally {
            closeTrackerServer(trackerServer);
        }
        return result;
    }

    /**
     * 下载文件
     *
     * @param groupName         下载组，默认为Group1
     * @param remoteFilename    DFS上面的文件名
     * @param localFileName     下载到本地的文件名
     * @return int              文件下载结果
     */
    public static int download(String groupName, String remoteFilename, String localFileName) {
        int result = -1;
        TrackerServer trackerServer = null;
        try {

            trackerServer = pool.borrowObject();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            // 下载文件
            groupName = StringUtils.isEmpty(groupName) ? "group1" : groupName;
            result = storageClient.download_file(groupName, remoteFilename, localFileName);
            log.debug("download result: group_name: {} , remoteFilename: {}, localFileName: {}",
                    groupName, remoteFilename, localFileName);

        } catch (Exception e) {
            log.error("下载文件失败,remoteFilename:{},localFileName:{},{},{}"
                    ,remoteFilename,localFileName,e.getMessage(),e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        } finally {
            closeTrackerServer(trackerServer);
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @param groupName         组名
     * @param remoteFilename    文件名
     * @return                  执行结果
     */
    public static int delete(String groupName, String remoteFilename) {
        int result = -1;
        TrackerServer trackerServer = null;
        try {

            trackerServer = pool.borrowObject();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            // 删除文件
            groupName = StringUtils.isEmpty(groupName) ? "group1" : groupName;
            result = storageClient.delete_file(groupName, remoteFilename);

        } catch (Exception e) {
            log.error("删除文件失败， remoteFilename: {}, :{},{}",remoteFilename,e.getMessage(),e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        } finally {
            closeTrackerServer(trackerServer);
        }
        return result;
    }

    /**
     * 上传文件到FastDFS服务器
     *
     * @param localFilePath     文件流
     * @return                  远程返回的文件名称和group名称
     */
    public static Map<String, String> upload(String localFilePath) {

        TrackerServer trackerServer = null;
        try {

            trackerServer = pool.borrowObject();
            StorageClient storageClient = new StorageClient(trackerServer, null);

            String[] results = storageClient.upload_file(localFilePath, null, null);
            if (results == null) {
                log.error("FastDFS文件上传失败,Error Code【{}】", storageClient.getErrorCode());
                throw new DfsException(ErrorCode.UPLOAD_FAILURE,String.valueOf(storageClient.getErrorCode()));
            }

            log.info("SUCCESS T0 UPLOAD, localFilePath:{}, GROUP_NAME:{}, REMOTE_FILE_NAME:{}",
                    localFilePath, results[0], results[1]);

            Map<String, String> dfsMap = new HashMap<String, String>();
            // 远程返回的文件名称
            dfsMap.put("GROUP_NAME", results[0]);
            // 文件的groupId
            dfsMap.put("REMOTE_FILE_NAME", results[1]);

            return dfsMap;

        } catch (Exception e) {
            log.error("FastDFS文件上传异常:{},{}", e.getMessage(),e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        } finally {
            closeTrackerServer(trackerServer);
        }
    }

    /**
     * 上传文件到FastDFS服务器
     *
     * @param group             文件分组
     * @param fileSize          文件大小
     * @param uploadCallback    文件上传回调
     * @param file_ext_name     文件名称
     * @param meta_list         文件META头信息
     * @return                  远程返回的文件名称和group名称
     */
    public static Map<String, String> upload(String group,long fileSize,UploadCallback uploadCallback,
                                             String file_ext_name,NameValuePair[] meta_list) {

        TrackerServer trackerServer = null;
        try {

            trackerServer = pool.borrowObject();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            if(StringUtils.isBlank(group)){
                group = "group1";
            }

            String[] results = storageClient.upload_file(group,fileSize,uploadCallback,file_ext_name,meta_list);
            if (results == null) {
                log.error("FastDFS文件上传失败,Error Code【{}】", storageClient.getErrorCode(),storageClient);
                throw new DfsException(ErrorCode.UPLOAD_FAILURE,String.valueOf(storageClient.getErrorCode()));
            }

            log.info("SUCCESS T0 UPLOAD,group:{},file_ext_name:{},REMOTE_FILE_NAME:{}",group,file_ext_name,results[1]);

            Map<String, String> dfsMap = new HashMap<String, String>();
            // 远程返回的文件名称
            dfsMap.put("GROUP_NAME", results[0]);
            // 文件的groupId
            dfsMap.put("REMOTE_FILE_NAME", results[1]);

            return dfsMap;

        } catch (Exception e) {
            log.error("FastDFS文件上传异常:{},{}", e.getMessage(),e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR,e.getMessage());
        } finally {
            closeTrackerServer(trackerServer);
        }
    }

    /**
     * 获取HTTP 下载地址
     *
     * @param dfsGroup          DFS 存放组别
     * @param dfsPath           DFS 存放目录
     * @return                  HTTP下载地址
     */
    public static String getDownloadUrl(String dfsGroup, String dfsPath) {

        String file_id;
        int ts;
        String token;
        String file_url;

        file_id = dfsPath;

        file_url = "http://" + DfsConfig.get_http_server();
        if (ClientGlobal.g_tracker_http_port != 80){
            file_url += ":" + ClientGlobal.g_tracker_http_port;
        }

        file_url += "/" + dfsGroup + "/" + dfsPath;
        if (ClientGlobal.g_anti_steal_token){
            ts = (int)(System.currentTimeMillis() / 1000);
            try {
                token = ProtoCommon.getToken(file_id, ts, ClientGlobal.g_secret_key);
            }catch (Exception e) {
                log.error(e.getMessage(),e);
                throw new DfsException(ErrorCode.SYSTEM_ERROR,"生成FastDFS Token 失败");
            }

            file_url += "?token=" + token + "&ts=" + ts;
        }

        return file_url;
    }

    /**
     * 关闭队列服务
     *
     * @param trackerServer     trackerServer队列服务
     */
    public static void closeTrackerServer(TrackerServer trackerServer) {
        // 退出前,一定要将队列服务关闭
        try {
            if (trackerServer != null) {
                pool.returnObject(trackerServer);
            }
        } catch (Exception e) {
            log.error("队列服务关闭异常:{},{}", e.getMessage(),e);
        }
    }

    public static void putQueue(String info){
        try {
            log.info("----------connectQueue add {},size = {}",info,connectQueue.size());
            connectQueue.put(info);
            log.info("----------connectQueue add {} finished !",info,connectQueue.size());
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
            throw new DfsException(ErrorCode.QUEUE_FULL);
        }
    }

    public static String takeQueue(){
        try {
            return connectQueue.take();
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
            throw new DfsException(ErrorCode.QUEUE_FULL);
        }
    }

}
