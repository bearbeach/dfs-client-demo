package com.baofoo;

import com.baofoo.dfs.client.DfsClient;
import com.baofoo.dfs.client.enums.FileGroup;
import com.baofoo.dfs.client.model.CommandResDTO;
import com.baofoo.dfs.client.model.InsertReqDTO;
import org.junit.Test;


/**
 * DfsTest
 *
 * @author weiwei(Duan.Yu)
 * @version 1.0.0 createTime: 16/8/24 上午11:30
 */
public class DfsTest extends BaseTest {

    @Test
    public void insert() {
        InsertReqDTO insertReqDTO = new InsertReqDTO();
        insertReqDTO.setFilePath("/Users/weiwei/Downloads/dfs引入说明.html");
        insertReqDTO.setOrgCode("orgCode");
        insertReqDTO.setFileGroup(FileGroup.TRADE_INFO.getCode());
        insertReqDTO.setFileName("dfs引入说明.html");
        insertReqDTO.setFileDate("文件日期");

        CommandResDTO commandResDTO = DfsClient.upload(insertReqDTO);
        System.out.println("commandResDTO===" + commandResDTO.toString());

//        DesClient.download("M00/00/00/CgAVOVZYFPuAFEQNE8UwY4CYfGg986.zip", "D:\\aaa.zip");

//        DfsClient.download("fileName", "123", "2015-11-27", "D:\\test.sql");

//        Dfselient.download(7L, "D:\\");

//        DfsClient.delete(6L);
    }
}
