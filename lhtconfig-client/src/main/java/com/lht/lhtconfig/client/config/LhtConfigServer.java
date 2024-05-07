package com.lht.lhtconfig.client.config;

import com.lht.lhtconfig.client.repository.LhtRepository;

/**
 * @author Leo
 * @date 2024/05/05
 */
public interface LhtConfigServer {

    static LhtConfigServerImpl getDefault(ConfigMeta configMeta){
        LhtRepository lhtRepository = LhtRepository.getDefault(configMeta);
        return new LhtConfigServerImpl(lhtRepository.getConfig());
    }

    String[] getPropertyNames();

    String getProperty(String name);


}
