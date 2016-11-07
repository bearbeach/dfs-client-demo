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
        insertReqDTO.setFileGroup(FileGroup.TRADE_INFO);
        insertReqDTO.setFileName("1.txt");
        insertReqDTO.setFileDate("2016-11-17");

        DfsClient.upload(insertReqDTO);
    }

    /**
     * 查询下载的地址
     */
    @Test
    public void queryDfsUrl() {
        QueryReqDTO queryReqDTO = new QueryReqDTO();
        queryReqDTO.setFileName("1.txt");
        queryReqDTO.setOrgCode("orgCode");
        queryReqDTO.setFileDate("2016-11-17");
        String url = DfsClient.getDownloadUri(queryReqDTO);
        System.out.println("url:" + url);
    }


    /**
     * 查询下载地址
     */
    @Test
    public void queryDfsUrl2() {
        String group = FileGroup.TRADE_INFO.getCode();
        String dfs_path = "/Users/weiwei/Downloads/1.txt";
        String url = DfsClient.getDownloadUri(group, dfs_path);
        System.out.println("url:" + url);
    }

    /**
     * 通过fileId获取地址
     */
    @Test
    public void queryDfsUrlByFileId() {
        QueryReqDTO queryReqDTO = new QueryReqDTO();
        queryReqDTO.setFileId(50005417L);
        String url = DfsClient.getDownloadUri(queryReqDTO);
        System.out.println("url:" + url);
    }

    /**
     * 通过fileId下载
     */
    @Test
    public void download() {
        QueryReqDTO queryReqDTO = new QueryReqDTO();
        queryReqDTO.setFileId(50005417L);
        DfsClient.download(queryReqDTO, "/Users/weiwei/Downloads/test/");
    }

    /**
     * 下载
     */
    @Test
    public void downloadByFileID() {
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
        DfsClient.delete("1.txt", "orgCode", "2016-11-17");
    }
}
