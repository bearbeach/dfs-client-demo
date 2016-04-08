package com.baofoo.dfs.client.core.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * FastDFS连接池 TrackerServer管理
 *
 * @author tiger
 * @version 1.0.0 createTime: 15/4/20 下午6:46
 * @see org.apache.commons.pool2.BasePooledObjectFactory
 * @see org.apache.commons.pool2.PooledObject
 * @since 1.7
 */
public class FastDFSFactory extends BasePooledObjectFactory<TrackerServer> {

    /**
     * 创建 TrackerServer 连接
     *
     * @return TrackerServer 对象
     * @throws Exception
     */
    @Override
    public TrackerServer create() throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient.getConnection();
    }

    /**
     * 包装创建 TrackerServer 连接的实现
     *
     * @param trackerServer TrackerServer 连接
     * @return 连接池对象
     */
    @Override
    public PooledObject<TrackerServer> wrap(TrackerServer trackerServer) {
        return new DefaultPooledObject<TrackerServer>(trackerServer);
    }

    /**
     * 销毁 TrackerServer 连接
     *
     * @param pooledObject 连接池对象
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<TrackerServer> pooledObject) throws Exception {
        TrackerServer trackerServer = pooledObject.getObject();
        if (null != trackerServer) {
            trackerServer.close();
        }
    }
}
