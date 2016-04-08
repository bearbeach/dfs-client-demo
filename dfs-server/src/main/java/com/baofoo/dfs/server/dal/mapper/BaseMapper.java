package com.baofoo.dfs.server.dal.mapper;

/**
 * 公用数据库操作Mapper
 *
 * @author 牧之
 * @version 1.0.0 createTime: 2015/11/30
 * @since 1.7
 */
public interface BaseMapper<T> {

    /**
     * 根据主键ID删除
     *
     * @param id    主键ID
     * @return      受影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 根据主键ID删除
     *
     * @param id    主键ID
     * @return      受影响行数
     */
    int deleteByPrimaryKey(String id);

    /**
     * 新增
     *
     * @param record    新增model
     * @return          受影响行数
     */
    int insert(T record);

    /**
     * 主键ID查询
     *
     * @param id        主键ID
     * @return          查询结果
     */
    T selectByPrimaryKey(Long id);

    /**
     * 主键ID查询
     *
     * @param id        主键ID
     * @return          查询结果
     */
    T selectByPrimaryKey(String id);

    /**
     * 更新
     *
     * @param record    查询对象
     * @return          受影响行数
     */
    int update(T record);

}
