package com.baofoo.dfs.server.controller;

import com.baofoo.dfs.client.core.DfsConfig;
import com.baofoo.dfs.client.model.DfsNode;
import com.baofoo.dfs.client.zookeeper.DfsServerManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ������
 *
 * @author ��֮
 * @version 1.0.0 createTime: 2016/3/15
 */
@Controller
public class BaseController {

    @RequestMapping(value = "/")
    public String show(HttpServletRequest request){
        DfsServerManager dfsServerManager = new DfsServerManager(DfsConfig.get_zookeeper_address());
        List<DfsNode> servers = dfsServerManager.getServerNodes();
        request.setAttribute("servers", servers);
        return "../index";
    }
}
