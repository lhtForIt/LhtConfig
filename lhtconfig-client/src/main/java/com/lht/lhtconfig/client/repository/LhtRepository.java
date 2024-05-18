package com.lht.lhtconfig.client.repository;

import com.lht.lhtconfig.client.config.ConfigMeta;
import org.springframework.context.ApplicationContext;

import javax.swing.event.ChangeEvent;
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

    void addChangeListener(ChangeListener changeListener);

    interface ChangeListener {
        void onChange(ChangeEvent event);
    }

    record ChangeEvent(ConfigMeta meta, Map<String, String> config){}


}
