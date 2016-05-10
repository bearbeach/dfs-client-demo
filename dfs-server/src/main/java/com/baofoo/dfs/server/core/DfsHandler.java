package com.baofoo.dfs.server.core;

import com.baofoo.Response;
import com.baofoo.dfs.client.core.DfsException;
import com.baofoo.dfs.client.enums.ErrorCode;
import com.baofoo.dfs.client.model.*;
import com.baofoo.dfs.client.util.SocketUtil;
import com.baofoo.dfs.client.util.StreamUtil;
import com.baofoo.dfs.server.manager.DfsFileManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * DFS Socket server handler
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 */
@Slf4j
@AllArgsConstructor
public class DfsHandler implements Runnable {

    /** Socket 请求 */
    private Socket socket;

    /** 普通文件记录操作Mapper */
    private DfsFileManager dfsFileManager;

    @Override
    public void run() {

        ObjectInputStream oin = null;
        ObjectOutputStream oot = null;

        Response<Object> response = new Response<Object>();

        try {

            oin = new ObjectInputStream(socket.getInputStream());
            oot = new ObjectOutputStream(socket.getOutputStream());

            CommandDTO command = (CommandDTO)oin.readObject();
            log.info("收到对象请求：{}", command.toString());

            switch (command.getOperation()){
                case INSERT:
                    response.setResult(insert(command));
                    return;
                case UPDATE:
                    response.setResult(update(command));
                    return;
                case QUERY:
                    response.setResult(query(command));
                    return;
                case DELETE:
                    response.setResult(delete(command));
                    break;
            }

        } catch (DfsException ex) {
            log.error(ex.getMessage(),ex);
            response.setErrorCode(ex.getCode());
            response.setErrorMsg(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
            response.setErrorCode(ErrorCode.SYSTEM_ERROR.getCode());
            response.setErrorMsg(ErrorCode.SYSTEM_ERROR.getDesc());
        } finally {
            SocketUtil.receiveMessage(oot,response);
            StreamUtil.closeStream(oot,oin);
            SocketUtil.closeSocket(socket);
        }
    }

    private Long insert(CommandDTO command){
        InsertReqDTO insertReqDTO = (InsertReqDTO)command;
        return dfsFileManager.insert(insertReqDTO);
    }

    /**
     * 更新DFS 文件记录
     *
     * @param command   Socket 命令请求对象
     * @return          DFS 文件记录ID
     */
    private Long update(CommandDTO command){
        UpdateReqDTO updateReqDTO = (UpdateReqDTO)command;
        dfsFileManager.update(updateReqDTO);
        return updateReqDTO.getFileId();

    }

    /**
     * 获取DFS文件记录
     *
     * @param command   Socket 命令请求对象
     * @return          DFS文件记录信息
     */
    private CommandResDTO query(CommandDTO command){
        QueryReqDTO queryReqDTO = (QueryReqDTO)command;
        return dfsFileManager.queryFileInfo(queryReqDTO);
    }

    /**
     * 删除DFS文件记录
     *
     * @param command   Socket 命令请求对象
     * @return          DFS文件记录信息
     */
    private CommandDTO delete(CommandDTO command){
        DeleteReqDTO deleteReqDTO = (DeleteReqDTO)command;
        dfsFileManager.delete(deleteReqDTO);
        return command;
    }

}
