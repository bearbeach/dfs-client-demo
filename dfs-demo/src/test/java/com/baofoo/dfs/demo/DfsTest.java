package com.baofoo.dfs.demo;

import com.baofoo.dfs.client.DfsClient;
import com.baofoo.dfs.client.enums.FileGroup;
import com.baofoo.dfs.client.model.InsertReqDTO;
import com.baofoo.dfs.client.model.QueryReqDTO;
import org.junit.Test;

/**
 * DfsTest
 *
 * @author weiwei(Duan.Yu)
 * @version 1.0.0 createTime: 16/8/24 上午11:30
 */
public class DfsTest extends BaseTest {

    /**
     * 上传文件
     */
    @Test
    public void upload() {
        InsertReqDTO insertReqDTO = new InsertReqDTO();
        insertReqDTO.setFilePath("/Users/weiwei/Downloads/1.txt");
        insertReqDTO.setOrgCode("orgCode");
        insertReqDTO.setFileGroup(FileGroup.FINANCE.getCode());
        insertReqDTO.setFileName("1.txt");
        insertReqDTO.setFileDate("2016-08-24");

        DfsClient.upload(insertReqDTO);
    }


    /**
     * 下载
     */
    @Test
    public void download() {
        DfsClient.download("M00/00/00/CgAVOVZYFPuAFEQNE8UwY4CYfGg986.zip", "/Users/weiwei/Downloads/dfs引入说明.html2");
    }

    /**
     * 查询下载
     */
    @Test
    public void queryDownload() {
        QueryReqDTO queryReqDTO = new QueryReqDTO();
        queryReqDTO.setFileName("1.txt");
        queryReqDTO.setOrgCode("orgCode");
        queryReqDTO.setFileDate("2016-08-24");
        DfsClient.download(queryReqDTO, "/Users/weiwei/Downloads/download");
    }

    /**
     * 删除文件
     */
    @Test
    public void delete() {
        DfsClient.delete("1.txt","orgCode","2016-08-24");
    }
}
