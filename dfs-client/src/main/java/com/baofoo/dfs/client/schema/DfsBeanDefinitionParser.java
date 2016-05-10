package com.baofoo.dfs.client.schema;

import com.baofoo.dfs.client.core.DfsConfig;
import com.baofoo.dfs.client.util.FastDFSUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * 解析Spring 标签
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since spring 4.x
 */
public class DfsBeanDefinitionParser implements BeanDefinitionParser{

    /** SL4Fj 日志 */
    private static final Logger log = LoggerFactory.getLogger(DfsBeanDefinitionParser.class);

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        log.info("DFS 开始解析Spring 配置文件，初始化配置。");
        RootBeanDefinition beanDefinition = new RootBeanDefinition();

        String trackers = element.getAttribute("trackers");
        String zookeeper = element.getAttribute("zookeeper");

        int connectTimeout = Integer.valueOf(element.getAttribute("connectTimeout"));
        if(connectTimeout == 0){
            connectTimeout = 30000;
        }

        int networkConnectTimeout = Integer.valueOf(element.getAttribute("networkConnectTimeout"));
        if(networkConnectTimeout == 0){
            networkConnectTimeout = 30000;
        }

        int trackerHttpPort = Integer.valueOf(element.getAttribute("trackerHttpPort"));
        if(trackerHttpPort == 0){
            trackerHttpPort = 8080;
        }

        String secretKey = element.getAttribute("secretKey");
        if(StringUtils.isBlank(secretKey)){
            secretKey = "1qazXsw28080";
        }

        int maxIdle = Integer.valueOf(element.getAttribute("maxIdle"));
        if(maxIdle == 0){
            maxIdle = 50;
        }

        int minIdle = Integer.valueOf(element.getAttribute("minIdle"));
        if(minIdle == 0){
            maxIdle = 1;
        }

        int maxTotal = Integer.valueOf(element.getAttribute("maxTotal"));
        if(minIdle == 0){
            maxIdle = 50;
        }

        DfsConfig.set_zookeeper_address(zookeeper);
        DfsConfig.set_tracker_adds(trackers);
        DfsConfig.set_tracker_http_port(trackerHttpPort);
        DfsConfig.set_min_idle(minIdle);
        DfsConfig.set_max_idle(maxIdle);
        DfsConfig.set_max_total(maxTotal);
        DfsConfig.set_connect_timeout(connectTimeout);
        DfsConfig.set_network_timeout(networkConnectTimeout);
        DfsConfig.set_secret_key(secretKey);

        log.info("初始化DFS ,trackers:{}",trackers);

        FastDFSUtil.init();

        beanDefinition.getPropertyValues().addPropertyValue("zookeeper", zookeeper);
        beanDefinition.getPropertyValues().addPropertyValue("trackers", trackers);
        beanDefinition.getPropertyValues().addPropertyValue("trackerHttpPort", trackerHttpPort);
        beanDefinition.getPropertyValues().addPropertyValue("minIdle", minIdle);
        beanDefinition.getPropertyValues().addPropertyValue("maxIdle", maxIdle);
        beanDefinition.getPropertyValues().addPropertyValue("maxTotal", maxTotal);
        beanDefinition.getPropertyValues().addPropertyValue("connectTimeout", connectTimeout);
        beanDefinition.getPropertyValues().addPropertyValue("networkConnectTimeout", networkConnectTimeout);

        return beanDefinition;
    }
}
