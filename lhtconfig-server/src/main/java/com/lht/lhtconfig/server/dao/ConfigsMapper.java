package com.lht.lhtconfig.server.dao;

import com.lht.lhtconfig.server.model.Configs;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Leo
 * @date 2024/05/05
 */
@Mapper
public interface ConfigsMapper {

    @Select("select * from configs where app=#{app} and env=#{env} and namespace=#{nameSpace}")
    List<Configs> list(@Param("app") String app, @Param("env") String env, @Param("nameSpace") String nameSpace);


    @Select("select * from configs where app=#{app} and env=#{env} and namespace=#{nameSpace} and pkey=#{pkey}")
    Configs select(Configs configs);


    @Insert("insert into configs(app,env,namespace,pkey,pval) values(#{app},#{env},#{nameSpace},#{pkey},#{pval})")
    void insert(Configs configs);

    @Update("update configs set pval=#{pval} where app=#{app} and env=#{env} and namespace=#{nameSpace} and pkey=#{pkey}")
    void update(Configs configs);
}
