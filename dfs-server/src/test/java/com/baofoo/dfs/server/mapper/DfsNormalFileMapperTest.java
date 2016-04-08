package com.baofoo.dfs.server.mapper;

import com.baofoo.dfs.server.BaseTest;
import com.baofoo.dfs.server.dal.mapper.DfsNormalFileMapper;
import com.baofoo.dfs.server.dal.model.DFSNormalFileDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;
import java.util.Date;

/**
 * <ul>
 * <li>类描述</li>
 * <li>提供的方法描述，如：查询，新增，删除，修改</li>
 * <li>User: 牧之 Date:2015/11/26 </li>
 * </ul>
 */
@Slf4j
public class DfsNormalFileMapperTest extends BaseTest {

    @Autowired(required = false)
    private DfsNormalFileMapper dfsNormalFileMapper;

    @Repeat(100)
    @Test
    public void insert(){

        DFSNormalFileDO dfsNormalFileDO = new DFSNormalFileDO();
        dfsNormalFileDO.setDfsGroup("group1");
        dfsNormalFileDO.setDfsPath("/group1/asdf/wrwe/adfawe");
        dfsNormalFileDO.setFileDate("2015-11-26");
        dfsNormalFileDO.setFileName("testFileName");
        dfsNormalFileDO.setOrgCode(String.valueOf(new Date().getTime()));
        dfsNormalFileDO.setFileGroup("group1");
        dfsNormalFileDO.setRemark("remark");

        int insertRows = dfsNormalFileMapper.insert(dfsNormalFileDO);

        assert insertRows == 1;

    }

    @Test
    public void selectByPrimaryKey(){

        DFSNormalFileDO dfsNormalFileDO = dfsNormalFileMapper.selectByPrimaryKey("1448508076128");

        assert dfsNormalFileDO != null;

        log.debug(dfsNormalFileDO.toString());

    }

    @Test
    public void update(){

        DFSNormalFileDO dfsNormalFileDO = new DFSNormalFileDO();
        dfsNormalFileDO.setId(1448508076128L);
        dfsNormalFileDO.setDfsGroup("group1");
        dfsNormalFileDO.setDfsPath(".............................");
        dfsNormalFileDO.setFileDate("2015-11-26");
        dfsNormalFileDO.setFileName("testFileName");
        dfsNormalFileDO.setOrgCode(String.valueOf(new Date().getTime()));
        dfsNormalFileDO.setFileGroup("group1");
        dfsNormalFileDO.setRemark("remark");

        int updateRows = dfsNormalFileMapper.update(dfsNormalFileDO);

        assert updateRows == 1;

        log.debug("update success !");

    }

    @Test
    public void deleteByPrimaryKey(){

        int deleteRows = dfsNormalFileMapper.deleteByPrimaryKey("1448508076128");

        assert deleteRows == 1;

        log.debug("delete success !");

    }

}
