package com.lht.lhtconfig.client.repository;

import com.lht.lhtconfig.client.config.ConfigMeta;

import java.util.Map;

/**
 *
 * interface for get config from remote
 *
 * @author Leo
 * @date 2024/05/07
 */
public interface LhtRepository {

    static LhtRepository getDefault(ConfigMeta configMeta) {
        return new LhtRepositoryImpl(configMeta);
    }

    Map<String,String> getConfig();

}
