package com.baofoo.dfs.client.core;

import java.io.Serializable;

/**
 * 简介
 *
 * @author shuzhen(mu.zhi)
 * @version 5.0 createTime: 16/8/26
 */
public class RegistryConfig implements Serializable{

    /** zookeeper 服务地址 */
    private String  zookeeper;

    /** 服务连接超时时间 */
    private int     connectTimeout;

    /** 网络连接超时时间 */
    private int     networkConnectTimeout;

    /** DFS tracker 地址列表 */
    private String  trackers;

    /** DFS tracker http 端口 */
    private int     trackerHttpPort;

    /** DFS http 服务地址 */
    private String  httpServer;

    /** DFS 密钥 */
    private String  secretKey ="1qazXsw28080";

    /** DFS 最大连接数 */
    private int     maxIdle;

    /** DFS 最小连接数 */
    private int     minIdle;

    /** DFS 总连接数 */
    private int     maxTotal;

    public String getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(String zookeeper) {
        this.zookeeper = zookeeper;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getNetworkConnectTimeout() {
        return networkConnectTimeout;
    }

    public void setNetworkConnectTimeout(int networkConnectTimeout) {
        this.networkConnectTimeout = networkConnectTimeout;
    }

    public String getTrackers() {
        return trackers;
    }

    public void setTrackers(String trackers) {
        this.trackers = trackers;
    }

    public int getTrackerHttpPort() {
        return trackerHttpPort;
    }

    public void setTrackerHttpPort(int trackerHttpPort) {
        this.trackerHttpPort = trackerHttpPort;
    }

    public String getHttpServer() {
        return httpServer;
    }

    public void setHttpServer(String httpServer) {
        this.httpServer = httpServer;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
}
