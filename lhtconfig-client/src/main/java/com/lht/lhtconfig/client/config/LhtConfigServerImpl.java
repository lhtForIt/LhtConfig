package com.lht.lhtconfig.client.config;

import java.util.Map;

/**
 * @author Leo
 * @date 2024/05/05
 */
public class LhtConfigServerImpl implements LhtConfigServer {

    Map<String, String> config;

    public LhtConfigServerImpl(Map<String, String> config) {
        this.config = config;
    }

    @Override
    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return this.config.get(name);
    }
}
