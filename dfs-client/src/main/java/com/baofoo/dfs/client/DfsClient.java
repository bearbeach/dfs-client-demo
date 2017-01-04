package com.baofoo.dfs.client;

import com.baofoo.Response;
import com.baofoo.dfs.client.core.DfsConfig;
import com.baofoo.dfs.client.core.DfsException;
import com.baofoo.dfs.client.enums.ErrorCode;
import com.baofoo.dfs.client.enums.FileGroup;
import com.baofoo.dfs.client.enums.Operation;
import com.baofoo.dfs.client.model.*;
import com.baofoo.dfs.client.util.DateUtil;
import com.baofoo.dfs.client.util.FastDFSUtil;
import com.baofoo.dfs.client.util.FileUtil;
import com.baofoo.dfs.client.util.SocketUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.common.IOUtils;
import org.csource.common.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;
import java.util.Map;

/**
 * DFS Socket Client（为规范使用，此类将被结扎）
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/27
 */
public final class DfsClient {

    /**
     * SL4Fj 日志
     */
    private static final Logger log = LoggerFactory.getLogger(DfsClient.class);

    /**
     * 获取文件HTTP下载地址（时效控制）
     *
     * @param reqDTO DFS 文件记录信息查询model
     * @return HTTP下载地址
     */
    public static String getDownloadUri(QueryReqDTO reqDTO) {

        if (reqDTO.getFileId() == null || reqDTO.getFileId() < 1) {

            if (StringUtils.isBlank(reqDTO.getFileName())) {
                throw new DfsException(ErrorCode.INVALID_PARAM, "文件名不能为空!");
            }
            if (StringUtils.isBlank(reqDTO.getOrgCode())) {
                throw new DfsException(ErrorCode.INVALID_PARAM, "机构代码不能为空!");
            }
            if (StringUtils.isBlank(reqDTO.getFileDate())) {
                throw new DfsException(ErrorCode.INVALID_PARAM, "文件日期不能为空!");
            }
        }

        reqDTO.setOperation(Operation.QUERY);

        try {

            //第一步、根据文件记录ID获取DFS文件存放信息
            Response response = SocketUtil.sendMessage(reqDTO);
            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.GET_FILE_INFO_ERROR, response.getErrorMsg());
            }

            CommandResDTO resDTO = (CommandResDTO) response.getResult();

            String downloadUri = FastDFSUtil.getDownloadUrl(resDTO.getDfsGroup(), resDTO.getDfsPath());

            log.debug("getDownload req:{},response:{}", reqDTO, downloadUri);

            return downloadUri;

        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }

    }

    /**
     * 获取文件HTTP下载地址（时效控制）
     *
     * @param dfsGroup DFS 组别
     * @param dfsPath  DFS 文件存放路径
     * @return HTTP下载地址
     */
    public static String getDownloadUri(String dfsGroup, String dfsPath) {

        if (StringUtils.isBlank(dfsGroup)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "Group不能为空!");
        }
        if (StringUtils.isBlank(dfsPath)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "DFS path不能为空!");
        }

        try {

            String downloadUri = FastDFSUtil.getDownloadUrl(dfsGroup, dfsPath);

            log.debug("getDownload dfsGroup:{},dfsPath:{},response:{}", dfsGroup, dfsPath, downloadUri);

            return downloadUri;

        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param insertReqDTO 上传命令参数封装对象（源文件路径，机构代码，文件名，文件组,源文件日期不能为空）
     * @return UploadResDTO     DFS 文件上传响应参数model
     */
    public static CommandResDTO upload(InsertReqDTO insertReqDTO) {

        if (StringUtils.isBlank(insertReqDTO.getFilePath())) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "上传文件全路径不能为空");
        }
        if (StringUtils.isBlank(insertReqDTO.getOrgCode())) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "机构代码不能为空");
        }
        if (StringUtils.isBlank(insertReqDTO.getFileName())) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "文件名称不能为空");
        }
        if (null == insertReqDTO.getFileGroup()) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "源文件组不能为空");
        }
        if (StringUtils.isBlank(insertReqDTO.getFileDate())) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "源文件日期不能为空");
        }

        insertReqDTO.setOperation(Operation.INSERT);

        if (insertReqDTO.getFileGroup().getDay() > 0) {
            insertReqDTO.setDeadline(DateUtil.computeDate(new Date(), insertReqDTO.getFileGroup().getDay()));
        }

        CommandResDTO uploadResDTO = new CommandResDTO();

        try {
            FastDFSUtil.putQueue(insertReqDTO.getFilePath());

            File file = new File(insertReqDTO.getFilePath());
            if (!file.exists()) {
                throw new DfsException(ErrorCode.FILE_NOT_EXITED);
            }

            insertReqDTO.setFileSize(file.length());

            // 第一步、新增DFS文件记录信息（重复校验）
            Response response = SocketUtil.sendMessage(insertReqDTO);

            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR, response.getErrorMsg());
            }

            // 第二步、上传DFS文件
            Map<String, String> resultMap = FastDFSUtil.upload(insertReqDTO.getFilePath());
            if (null == resultMap) {
                throw new DfsException(ErrorCode.UPLOAD_FAILURE, "文件上传失败");
            }

            UpdateReqDTO updateReqDTO = new UpdateReqDTO();
            Long fileId = (Long) response.getResult();
            String dfsGroup = resultMap.get(FastDFSUtil.KEY_GROUP);
            String dfsPath = resultMap.get(FastDFSUtil.KEY_REMOTE_FILE_NAME);
            updateReqDTO.setDfsGroup(dfsGroup);
            updateReqDTO.setDfsPath(dfsPath);
            updateReqDTO.setFileId(fileId);
            updateReqDTO.setOperation(Operation.UPDATE);
            updateReqDTO.setDeadline(insertReqDTO.getDeadline());

            //第三步、更新DFS文件记录信息
            response = SocketUtil.sendMessage(updateReqDTO);

            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR, response.getErrorMsg());
            }

            uploadResDTO.setFileId(fileId);
            uploadResDTO.setDfsGroup(dfsGroup);
            uploadResDTO.setDfsPath(dfsPath);
            uploadResDTO.setDownloadUrl(FastDFSUtil.getDownloadUrl(dfsGroup, dfsPath));

            log.debug("文件:{}上传成功，响应结果:{}", insertReqDTO.getFilePath(), uploadResDTO);

        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            FastDFSUtil.takeQueue();
        }

        return uploadResDTO;
    }

    /**
     * 上传文件
     *
     * @param in           文件输入流
     * @param insertReqDTO 上传命令参数封装对象（机构代码，文件名，文件组,源文件日期不能为空）
     * @return DFS 文件上传响应参数model
     */
    public static CommandResDTO upload(InputStream in, InsertReqDTO insertReqDTO) {

        if (null == in) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "上传文件流不能为空");
        }
        if (StringUtils.isBlank(insertReqDTO.getOrgCode())) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "机构代码不能为空");
        }
        if (StringUtils.isBlank(insertReqDTO.getFileName())) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "文件名称不能为空");
        }
        if (null == insertReqDTO.getFileGroup()) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "源文件组不能为空");
        }
        if (StringUtils.isBlank(insertReqDTO.getFileDate())) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "源文件日期不能为空");
        }

        CommandResDTO uploadResDTO = new CommandResDTO();

        try {

            String folder = DfsConfig.get_upload_temp_dir();

            if (StringUtils.isBlank(folder)) {
                folder = System.getProperty("java.io.tmpdir");
            }

            File uploadFile = new File(folder + File.separator + insertReqDTO.getFileName());

            FileUtil.inputstreamtofile(in, uploadFile);

            insertReqDTO.setFileSize(uploadFile.length());
            insertReqDTO.setFilePath(uploadFile.getAbsolutePath());

            uploadResDTO = upload(insertReqDTO);

        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        return uploadResDTO;
    }


    /**
     * 上传文件
     *
     * @param insertReqDTO 上传命令参数封装对象（机构代码，文件名，文件组,源文件日期不能为空）
     * @return UploadResDTO     DFS 文件上传响应参数model
     */
    public static CommandResDTO upload(byte[] bytes, InsertReqDTO insertReqDTO) {

        if (bytes == null || bytes.length < 1) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "文件源不能为空");
        }
        if (StringUtils.isBlank(insertReqDTO.getOrgCode())) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "机构代码不能为空");
        }
        if (StringUtils.isBlank(insertReqDTO.getFileName())) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "文件名称不能为空");
        }
        if (null == insertReqDTO.getFileGroup()) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "源文件组不能为空");
        }
        if (StringUtils.isBlank(insertReqDTO.getFileDate())) {
            throw new DfsException(ErrorCode.EMPTY_PARAM, "源文件日期不能为空");
        }

        insertReqDTO.setOperation(Operation.INSERT);

        if (insertReqDTO.getFileGroup().getDay() > 0) {
            insertReqDTO.setDeadline(DateUtil.computeDate(new Date(), insertReqDTO.getFileGroup().getDay()));
        }

        CommandResDTO uploadResDTO = new CommandResDTO();

        try {
            insertReqDTO.setFileSize(bytes.length);

            // 第一步、新增DFS文件记录信息（重复校验）
            Response response = SocketUtil.sendMessage(insertReqDTO);

            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR, response.getErrorMsg());
            }

            // 第二步、上传DFS文件
            Map<String, String> resultMap = FastDFSUtil.upload(bytes, insertReqDTO.getFileName(), new NameValuePair[]{});
            if (null == resultMap) {
                throw new DfsException(ErrorCode.UPLOAD_FAILURE, "文件上传失败");
            }

            UpdateReqDTO updateReqDTO = new UpdateReqDTO();
            Long fileId = (Long) response.getResult();
            String dfsGroup = resultMap.get(FastDFSUtil.KEY_GROUP);
            String dfsPath = resultMap.get(FastDFSUtil.KEY_REMOTE_FILE_NAME);
            updateReqDTO.setDfsGroup(dfsGroup);
            updateReqDTO.setDfsPath(dfsPath);
            updateReqDTO.setFileId(fileId);
            updateReqDTO.setOperation(Operation.UPDATE);
            updateReqDTO.setDeadline(insertReqDTO.getDeadline());

            //第三步、更新DFS文件记录信息
            response = SocketUtil.sendMessage(updateReqDTO);

            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.SAVE_UPLOAD_INFO_ERROR, response.getErrorMsg());
            }

            uploadResDTO.setFileId(fileId);
            uploadResDTO.setDfsGroup(dfsGroup);
            uploadResDTO.setDfsPath(dfsPath);
            uploadResDTO.setDownloadUrl(FastDFSUtil.getDownloadUrl(dfsGroup, dfsPath));

            log.debug("文件:{}上传成功，响应结果:{}", insertReqDTO.getFileName(), uploadResDTO);

        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            log.error("DFS 字节流上传DFS异常：", e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }

        return uploadResDTO;
    }

    /**
     * 上传文件
     *
     * @param insertReqDTO 上传命令参数封装对象（机构代码，文件名，文件组,源文件日期不能为空）
     * @return UploadResDTO     DFS 文件上传响应参数model
     */
    public static CommandResDTO uploadInputStream(InputStream inputStream, InsertReqDTO insertReqDTO) {
        try {
            byte[] fileBuff = new byte[inputStream.available()];
            inputStream.read(fileBuff);
            return upload(fileBuff, insertReqDTO);
        } catch (Exception e) {
            log.error("DFS 文件流上传异常：", e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }

    }

    /**
     * 上传文件
     *
     * @param insertReqDTO 上传命令参数封装对象（机构代码，文件名，文件组,源文件日期不能为空）
     * @return UploadResDTO     DFS 文件上传响应参数model
     */
    public static CommandResDTO upload(File file, InsertReqDTO insertReqDTO) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return uploadInputStream(fis, insertReqDTO);
        } catch (Exception e) {
            log.error("上传文件失败:{}", e);
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            IOUtils.closeStream(fis);
        }
    }

    /**
     * 下载文件
     * <p>
     * <p>文件ID与（文件名，机构代码，文件日期）不能同时为空</p>
     *
     * @param reqDTO  下载请求model
     * @param saveDir 下载本地保存目录
     */
    public static void download(QueryReqDTO reqDTO, String saveDir) {

        if (StringUtils.isBlank(saveDir)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "下载本地保存目录不能为空!");
        }

        if (reqDTO.getFileId() == null || reqDTO.getFileId() < 1) {

            if (StringUtils.isBlank(reqDTO.getFileName())) {
                throw new DfsException(ErrorCode.INVALID_PARAM, "文件名不能为空!");
            }
            if (StringUtils.isBlank(reqDTO.getOrgCode())) {
                throw new DfsException(ErrorCode.INVALID_PARAM, "机构代码不能为空!");
            }
            if (StringUtils.isBlank(reqDTO.getFileDate())) {
                throw new DfsException(ErrorCode.INVALID_PARAM, "文件日期不能为空!");
            }
        }

        reqDTO.setOperation(Operation.QUERY);

        try {

            FastDFSUtil.putQueue(saveDir);

            //第一步、根据文件记录ID获取DFS文件存放信息
            Response response = SocketUtil.sendMessage(reqDTO);
            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.GET_FILE_INFO_ERROR, response.getErrorMsg());
            }

            CommandResDTO resDTO = (CommandResDTO) response.getResult();

            //第二步、根据获取到的文件记录信息从DFS下载文件到本地
            int result = FastDFSUtil.download(resDTO.getDfsGroup(), resDTO.getDfsPath(),
                    saveDir + File.separator + resDTO.getFileName());
            if (result != 0) {
                throw new DfsException(ErrorCode.DOWNLOAD_FAILURE);
            }

            log.debug("文件:{}下载成功，保存地址:{}", resDTO.getFileName(), saveDir + File.separator + resDTO.getFileName());

        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            FastDFSUtil.takeQueue();
        }

    }

    /**
     * 下载文件
     *
     * @param dfsFilePath   DFS 服务器保存目录
     * @param localFilePath 下载本地保存路径
     */
    public static void download(String dfsFilePath, String localFilePath) {

        if (StringUtils.isBlank(dfsFilePath)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "DFS 文件路径不能为空!");
        }
        if (StringUtils.isBlank(localFilePath)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "下载本地保存路径不能为空!");
        }

        try {

            FastDFSUtil.putQueue(localFilePath);

            //直接从DFS 下载文件到本地
            int result = FastDFSUtil.download(dfsFilePath, localFilePath);

            if (result != 0) {
                throw new DfsException(ErrorCode.DOWNLOAD_FAILURE);
            }

            log.debug("文件:{}下载成功，保存地址:{}", dfsFilePath, localFilePath);

        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            FastDFSUtil.takeQueue();
        }
    }

    /**
     * 下载字节文件
     *
     * @param dfsGroupName DFS 文件服务器组
     * @param dfsPath      DFS文件地址
     * @return 返回字节信息
     */
    public static byte[] downloadByte(String dfsGroupName, String dfsPath) {

        if (StringUtils.isBlank(dfsGroupName)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "DFS 文件组不能为空!");
        }
        if (StringUtils.isBlank(dfsPath)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "DFS 文件路径不能为空!");
        }
        try {

            FastDFSUtil.putQueue(dfsPath);

            //直接从DFS 下载文件到本地
            byte[] result = FastDFSUtil.downloadByte(dfsGroupName, dfsPath);

            if (result == null || result.length == 0) {
                throw new DfsException(ErrorCode.DOWNLOAD_FAILURE);
            }

            log.debug("文件，组：{}，文件地址：{},下载成功，", dfsGroupName, dfsPath);
            return result;
        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            FastDFSUtil.takeQueue();
        }
    }

    /**
     * 下载文件
     * <p>
     * <p>文件ID与（文件名，机构代码，文件日期）不能同时为空</p>
     *
     * @param reqDTO 下载请求model
     */
    public static byte[] downloadByte(QueryReqDTO reqDTO) {

        if (reqDTO.getFileId() == null || reqDTO.getFileId() < 1) {

            if (StringUtils.isBlank(reqDTO.getFileName())) {
                throw new DfsException(ErrorCode.INVALID_PARAM, "文件名不能为空!");
            }
            if (StringUtils.isBlank(reqDTO.getOrgCode())) {
                throw new DfsException(ErrorCode.INVALID_PARAM, "机构代码不能为空!");
            }
            if (StringUtils.isBlank(reqDTO.getFileDate())) {
                throw new DfsException(ErrorCode.INVALID_PARAM, "文件日期不能为空!");
            }
        }
        reqDTO.setOperation(Operation.QUERY);
        //第一步、根据文件记录ID获取DFS文件存放信息
        Response response = SocketUtil.sendMessage(reqDTO);
        if (!response.isSuccess()) {
            throw new DfsException(ErrorCode.GET_FILE_INFO_ERROR, response.getErrorMsg());
        }
        CommandResDTO resDTO = (CommandResDTO) response.getResult();

        return downloadByte(resDTO.getDfsGroup(), resDTO.getDfsPath());

    }

    /**
     * 删除DFS 文件
     *
     * @param fileId 文件记录ID
     */
    public static void delete(long fileId) {

        if (fileId < 1) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "文件ID不能为空!");
        }

        try {

            FastDFSUtil.putQueue(String.valueOf(fileId));

            QueryReqDTO queryReqDTO = new QueryReqDTO();
            queryReqDTO.setFileId(fileId);
            queryReqDTO.setOperation(Operation.QUERY);

            //第一步、根据文件记录ID获取DFS文件存放信息
            Response response = SocketUtil.sendMessage(queryReqDTO);
            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.GET_FILE_INFO_ERROR, response.getErrorMsg());
            }

            CommandResDTO resDTO = (CommandResDTO) response.getResult();

            //第二步、根据获取到的文件记录信息从DFS删除文件
            int delResult = FastDFSUtil.delete(resDTO.getDfsGroup(), resDTO.getDfsPath());
            if (delResult != 0) {
                throw new DfsException(ErrorCode.DELETE_FAILURE);
            }

            DeleteReqDTO deleteReqDTO = new DeleteReqDTO();
            deleteReqDTO.setFileId(fileId);
            deleteReqDTO.setOperation(Operation.DELETE);

            //第三步、删除DFS文件记录
            response = SocketUtil.sendMessage(deleteReqDTO);
            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.GET_FILE_INFO_ERROR, response.getErrorMsg());
            }

            log.info("文件:{}删除成功，", fileId);

        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            FastDFSUtil.takeQueue();
        }

    }

    /**
     * 删除DFS 文件
     *
     * @param fileName 源文件名称（上传前）
     * @param orgCode  机构代码
     * @param fileDate 源文件日期
     */
    public static void delete(String fileName, String orgCode, String fileDate) {

        if (StringUtils.isBlank(fileName)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "文件名不能为空!");
        }
        if (StringUtils.isBlank(orgCode)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "机构代码不能为空!");
        }
        if (StringUtils.isBlank(fileDate)) {
            throw new DfsException(ErrorCode.INVALID_PARAM, "文件日期不能为空!");
        }

        DeleteReqDTO deleteReqDTO = new DeleteReqDTO();
        deleteReqDTO.setFileName(fileName);
        deleteReqDTO.setFileDate(fileDate);
        deleteReqDTO.setOrgCode(orgCode);

        try {

            FastDFSUtil.putQueue(fileName);

            QueryReqDTO queryReqDTO = new QueryReqDTO();
            queryReqDTO.setOrgCode(deleteReqDTO.getOrgCode());
            queryReqDTO.setFileDate(deleteReqDTO.getFileDate());
            queryReqDTO.setFileName(deleteReqDTO.getFileName());
            queryReqDTO.setOperation(Operation.QUERY);

            //第一步、根据文件记录ID获取DFS文件存放信息
            Response response = SocketUtil.sendMessage(queryReqDTO);
            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.GET_FILE_INFO_ERROR, response.getErrorMsg());
            }

            CommandResDTO resDTO = (CommandResDTO) response.getResult();
            deleteReqDTO.setFileId(resDTO.getFileId());

            if (StringUtils.isBlank(resDTO.getDfsPath()) || StringUtils.isBlank(resDTO.getDfsGroup())) {
                throw new DfsException(ErrorCode.FILE_INFO_NOT_FOUND);
            }

            //第二步、根据获取到的文件记录信息从DFS删除文件
            int delResult = FastDFSUtil.delete(resDTO.getDfsGroup(), resDTO.getDfsPath());
            if (delResult != 0) {
                throw new DfsException(ErrorCode.DELETE_FAILURE, response.getErrorMsg());
            }

            deleteReqDTO.setOperation(Operation.DELETE);

            //第三步、删除DFS文件记录
            response = SocketUtil.sendMessage(deleteReqDTO);
            if (!response.isSuccess()) {
                throw new DfsException(ErrorCode.GET_FILE_INFO_ERROR, response.getErrorMsg());
            }

            log.info("文件:{}删除成功，", fileName);

        } catch (DfsException e) {
            throw e;
        } catch (Exception e) {
            throw new DfsException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            FastDFSUtil.takeQueue();
        }

    }

    public static void main(String args[]) throws IOException, KeeperException, InterruptedException {

        DfsConfig.set_connect_timeout(1000 * 5);
        DfsConfig.set_secret_key("1qazXsw28080");
        DfsConfig.set_zookeeper_address("10.0.20.175:2181");
        DfsConfig.set_max_idle(50);
        DfsConfig.set_max_total(50);
        DfsConfig.set_min_idle(3);
        DfsConfig.set_tracker_adds("10.0.21.130:22122");
        DfsConfig.set_tracker_http_port(8080);
        DfsConfig.set_http_server("10.0.21.130");

        FastDFSUtil.init();

        String fileDate = DateUtil.getCurrent();
        String orgCode = DateUtil.getCurrent();

        File file = new File("E:/付款到银行模版.xls");
        InsertReqDTO insertReqDTO = new InsertReqDTO();
        insertReqDTO.setOrgCode(orgCode);
        insertReqDTO.setFileName("付款到银行模版.xls");
        insertReqDTO.setFileSize(0);
        insertReqDTO.setDeadline(DateUtil.computeDate(new Date(), -1));
        insertReqDTO.setRemark("remark");
        insertReqDTO.setFileGroup(FileGroup.CLEARING);
        insertReqDTO.setFileDate(fileDate);
        CommandResDTO commandResDTO = DfsClient.upload(file, insertReqDTO);
        log.debug("upload result =:{}", commandResDTO);

        /*QueryReqDTO queryReqDTO = new QueryReqDTO();
        queryReqDTO.setFileId(50000012L);
        DfsClient.getDownloadUri(queryReqDTO);*/

    }

}
