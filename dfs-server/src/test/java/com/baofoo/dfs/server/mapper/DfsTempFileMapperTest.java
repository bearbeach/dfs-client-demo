package com.baofoo.dfs.server.mapper;

import com.baofoo.dfs.server.BaseTest;
import com.baofoo.dfs.server.dal.mapper.DfsTempFileMapper;
import com.baofoo.dfs.server.dal.model.DFSTempFileDO;
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
public class DfsTempFileMapperTest extends BaseTest {

    @Autowired(required = false)
    private DfsTempFileMapper dfsTempFileMapper;

    @Repeat(100)
    @Test
    public void insert(){

        DFSTempFileDO dfsTempFileDO = new DFSTempFileDO();
        dfsTempFileDO.setDfsGroup("group1");
        dfsTempFileDO.setDfsPath("/group1/asdf/wrwe/adfawe");
        dfsTempFileDO.setFileDate("2015-11-26");
        dfsTempFileDO.setFileName("testFileName");
        dfsTempFileDO.setOrgCode(String.valueOf(new Date().getTime()));
        dfsTempFileDO.setFileGroup("group1");
        dfsTempFileDO.setRemark("remark");

        int insertRows = dfsTempFileMapper.insert(dfsTempFileDO);

        assert insertRows == 1;

    }

    @Test
    public void selectByPrimaryKey(){

        DFSTempFileDO dfsTempFileDO = dfsTempFileMapper.selectByPrimaryKey("1448508778400");

        assert dfsTempFileDO != null;

        log.debug(dfsTempFileDO.toString());

    }

    @Test
    public void update(){

        DFSTempFileDO dfsTempFileDO = new DFSTempFileDO();
        dfsTempFileDO.setId(1448508778400L);
        dfsTempFileDO.setDfsGroup("group1");
        dfsTempFileDO.setDfsPath(".............................");
        dfsTempFileDO.setFileDate("2015-11-26");
        dfsTempFileDO.setFileName("testFileName");
        dfsTempFileDO.setOrgCode(String.valueOf(new Date().getTime()));
        dfsTempFileDO.setFileGroup("group1");
        dfsTempFileDO.setRemark("remark");

        int updateRows = dfsTempFileMapper.update(dfsTempFileDO);

        assert updateRows == 1;

        log.debug("update success !");

    }

    @Test
    public void deleteByPrimaryKey(){

        int deleteRows = dfsTempFileMapper.deleteByPrimaryKey("1448508778400");

        assert deleteRows == 1;

        log.debug("delete success !");

    }

}
