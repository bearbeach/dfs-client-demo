package com.baofoo.dfs.client.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * DFS Spring Namespace
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since spring 4.x
 * @since jdk 1.7
 */
public class DfsNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        this.registerBeanDefinitionParser("dfs", new DfsBeanDefinitionParser());
    }

}
