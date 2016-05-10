package com.baofoo.dfs.client.model;

import java.io.Serializable;

public class DfsNode implements Serializable {

    private static final long serialVersionUID = -2080838030655318364L;

    private String serverNode;

    private String nodeValue;

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public String getServerNode() {
        return serverNode;
    }

    public void setServerNode(String serverNode) {
        this.serverNode = serverNode;
    }
}
