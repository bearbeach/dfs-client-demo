package com.baofoo.dfs.server.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * web 请求
 *
 * @author 牧之
 * @version 1.0.0 createTime: 15/4/7 下午3:53
 * @since 1.7
 */
@Slf4j
@Controller
public class DFSController {

    /**
     * 文件迁移请求处理
     *
     * @return 跳转页面
     */
    @RequestMapping(value={"/helloWord"})
    public String move() {



        return "index.jsp";
    }


}
