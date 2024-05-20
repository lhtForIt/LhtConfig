package com.lht.lhtconfig.server.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * mapper for dist locks
 *
 * @author Leo
 * @date 2024/05/19
 */
@Mapper
public interface LocksMapper {

    /**
     * 获取分布式锁
     */
    @Select("select app from locks where id=1 for update")
    String selectForUpdate();

    @Update("set innodb_lock_wait_timeout=5")
    void waitTime();



}
