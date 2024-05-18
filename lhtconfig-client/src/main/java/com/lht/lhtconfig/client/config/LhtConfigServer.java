package com.lht.lhtconfig.client.config;

import com.lht.lhtconfig.client.repository.LhtRepository;
import org.springframework.context.ApplicationContext;

/**
 * @author Leo
 * @date 2024/05/05
 */
public interface LhtConfigServer extends LhtRepository.ChangeListener{

    static LhtConfigServerImpl getDefault(ApplicationContext applicationContext, ConfigMeta configMeta) {
        LhtRepository lhtRepository = LhtRepository.getDefault(configMeta);
        LhtConfigServerImpl lhtConfigServer = new LhtConfigServerImpl(applicationContext, lhtRepository.getConfig());
        lhtRepository.addChangeListener(lhtConfigServer);
        return lhtConfigServer;
    }

    String[] getPropertyNames();

    String getProperty(String name);


}
