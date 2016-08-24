package com.baofoo.dfs.demo;

import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 单元测试父类
 *
 * @author tiger
 * @version 1.0.0 createTime: 15/3/24 下午2:07
 * @since 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:dfs-config.xml")
public abstract class BaseTest extends TestCase {

}
