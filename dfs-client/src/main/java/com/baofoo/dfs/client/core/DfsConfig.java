package com.baofoo.dfs.client.core;

/**
 * DFS Client Configuration
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/27
 */

public class DfsConfig {

    /** zookeeper 服务地址 */
    private static String _zookeeper_address;

    /** 服务连接超时时间 */
    private static int    _connect_timeout;

    /** DFS tracker 地址列表 */
    private static String _tracker_adds;


    /** DFS tracker http 端口 */
    private static int _tracker_http_port;

    /** DFS http 服务地址 */
    private static String _http_server;

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

    /** DFS 临时文件目录 */
    private static String _upload_temp_dir;

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

    public static void set_tracker_adds(String _tracker_adds) {
        DfsConfig._tracker_adds = _tracker_adds;
    }

    public static void set_tracker_http_port(int _tracker_http_port) {
        DfsConfig._tracker_http_port = _tracker_http_port;
    }

    public static int get_connect_timeout() {
        return _connect_timeout;
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

    public static String get_tracker_adds() {
        return _tracker_adds;
    }

    public static String get_zookeeper_address() {
        return DfsConfig._zookeeper_address;
    }

    public static void set_zookeeper_address(String _zookeeper_address) {
        DfsConfig._zookeeper_address = _zookeeper_address;
    }

    public static String get_upload_temp_dir() {
        return _upload_temp_dir;
    }

    public static void set_upload_temp_dir(String _upload_temp_dir) {
        DfsConfig._upload_temp_dir = _upload_temp_dir;
    }

    public static String get_http_server() {
        return DfsConfig._http_server;
    }

    public static void set_http_server(String _http_server) {
        DfsConfig._http_server = _http_server;
    }
}
