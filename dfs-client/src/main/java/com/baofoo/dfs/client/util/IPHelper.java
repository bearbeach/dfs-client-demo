package com.baofoo.dfs.client.util;

import org.apache.commons.lang3.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * IP获取帮助类
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2016/1/5
 */
public class IPHelper {

    private static volatile String IP_ADDRESS = "";
    private static final String LOCAL_IP = "127.0.0.1";

    /**
     * 获取本地IP
     *
     * @return IP地址
     */
    public static String getLocalIP() {
        if (StringUtils.isNotBlank(IP_ADDRESS)) {
            return IP_ADDRESS;
        }
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        String tip = ip.getHostAddress();
                        if(LOCAL_IP.equals(tip)){
                            continue;
                        }
                        IP_ADDRESS = tip;
                        return IP_ADDRESS;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    public static void main(String args[]){
        String ip = IPHelper.getLocalIP();
        ip = ip.substring(ip.lastIndexOf(".")+1,ip.length());
        System.out.println(StringUtils.leftPad(ip, 3, "0"));
    }

}
