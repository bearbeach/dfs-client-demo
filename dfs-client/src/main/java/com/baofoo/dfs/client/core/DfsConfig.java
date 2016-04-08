package com.baofoo.dfs.client.core;

/**
 * DFS Client Configuration
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/27
 * @since 1.7
 */

public class DfsConfig {

    /** 服务端地址 */
    private static String _server_host;

    /** 服务端端口 */
    private static int    _server_port;

    /** 服务连接超时时间 */
    private static int    _connect_timeout;

    /** DFS tracker 地址列表 */
    private static String _tracker_adds;

    /** DFS tracker http 端口 */
    private static int _tracker_http_port;

    /** DFS 密钥 */
    private static String _secret_key ="1qazXsw28080";

    /** DFS网络超时时间 */
    private static int _network_timeout;

    /** DFS 最大连接数 */
    private static int _max_idle;

    /** DFS 最小连接数 */
    private static int _min_idle;

    /** DFS 总连接数 */
    private static int _max_total;

    /** 字符集编码 */
    public final static String _input_charset = "UTF-8";

    public static void set_connect_timeout(int _connect_timeout) {
        DfsConfig._connect_timeout = _connect_timeout;
    }

    public static void set_max_idle(int _max_idle) {
        DfsConfig._max_idle = _max_idle;
    }

    public static void set_max_total(int _max_total) {
        DfsConfig._max_total = _max_total;
    }

    public static void set_min_idle(int _min_idle) {
        DfsConfig._min_idle = _min_idle;
    }

    public static void set_network_timeout(int _network_timeout) {
        DfsConfig._network_timeout = _network_timeout;
    }

    public static void set_secret_key(String _secret_key) {
        DfsConfig._secret_key = _secret_key;
    }

    public static void set_server_host(String _server_host) {
        DfsConfig._server_host = _server_host;
    }

    public static void set_server_port(int _server_port) {
        DfsConfig._server_port = _server_port;
    }

    public static void set_tracker_adds(String _tracker_adds) {
        DfsConfig._tracker_adds = _tracker_adds;
    }

    public static void set_tracker_http_port(int _tracker_http_port) {
        DfsConfig._tracker_http_port = _tracker_http_port;
    }

    public static int get_connect_timeout() {
        return _connect_timeout;
    }

    public static int get_server_port() {
        return _server_port;
    }

    public static int get_max_idle() {
        return _max_idle;
    }

    public static int get_max_total() {
        return _max_total;
    }

    public static int get_min_idle() {
        return _min_idle;
    }

    public static int get_network_timeout() {
        return _network_timeout;
    }

    public static int get_tracker_http_port() {
        return _tracker_http_port;
    }

    public static String get_secret_key() {
        return _secret_key;
    }

    public static String get_server_host() {
       return _server_host;
    }

    public static String get_tracker_adds() {
        return _tracker_adds;
    }

}
