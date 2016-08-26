package com.baofoo.dfs.client.schema;

import com.baofoo.dfs.client.core.DfsConfig;
import com.baofoo.dfs.client.util.FastDFSUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
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

    private final Class<?> beanClass;

    DfsBeanDefinitionParser(Class beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        log.info("DFS 开始解析Spring 配置文件，初始化配置。");
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);

        String trackers = element.getAttribute("trackers");
        assert StringUtils.isNotBlank(trackers);
        String zookeeper = element.getAttribute("zookeeper");
        assert StringUtils.isNotBlank(zookeeper);

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

        String httpServer = element.getAttribute("httpServer");
        assert StringUtils.isNotBlank(httpServer);

        String secretKey = element.getAttribute("secretKey");
        assert StringUtils.isNotBlank(secretKey);

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

        log.info("初始化DFS ,trackers:{}",trackers);

        beanDefinition.getPropertyValues().addPropertyValue("zookeeper", zookeeper);
        beanDefinition.getPropertyValues().addPropertyValue("trackers", trackers);
        beanDefinition.getPropertyValues().addPropertyValue("trackerHttpPort", trackerHttpPort);
        beanDefinition.getPropertyValues().addPropertyValue("httpServer", httpServer);
        beanDefinition.getPropertyValues().addPropertyValue("connectTimeout", connectTimeout);
        beanDefinition.getPropertyValues().addPropertyValue("networkConnectTimeout", networkConnectTimeout);
        beanDefinition.getPropertyValues().addPropertyValue("minIdle", minIdle);
        beanDefinition.getPropertyValues().addPropertyValue("maxIdle", maxIdle);
        beanDefinition.getPropertyValues().addPropertyValue("maxTotal", maxTotal);
        beanDefinition.getPropertyValues().addPropertyValue("secretKey", secretKey);

        parserContext.getRegistry().registerBeanDefinition("RegistryConfig", beanDefinition);

        MutablePropertyValues mv = parserContext.getRegistry().getBeanDefinition("RegistryConfig").getPropertyValues();

        DfsConfig.set_zookeeper_address((String)mv.getPropertyValue("zookeeper").getValue());
        DfsConfig.set_tracker_adds((String)mv.getPropertyValue("trackers").getValue());
        DfsConfig.set_http_server((String)mv.getPropertyValue("httpServer").getValue());
        DfsConfig.set_secret_key((String)mv.getPropertyValue("secretKey").getValue());
        DfsConfig.set_tracker_http_port((Integer)mv.getPropertyValue("trackerHttpPort").getValue());
        DfsConfig.set_connect_timeout((Integer)mv.getPropertyValue("connectTimeout").getValue());
        DfsConfig.set_network_timeout((Integer)mv.getPropertyValue("networkConnectTimeout").getValue());
        DfsConfig.set_min_idle((Integer)mv.getPropertyValue("minIdle").getValue());
        DfsConfig.set_max_idle((Integer)mv.getPropertyValue("maxIdle").getValue());
        DfsConfig.set_max_total((Integer)mv.getPropertyValue("maxTotal").getValue());

        System.out.println(DfsConfig.get_zookeeper_address());

        FastDFSUtil.init();

        return beanDefinition;
    }
}
