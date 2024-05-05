package com.lht.lhtconfig.client.config;

import org.springframework.core.env.EnumerablePropertySource;

/**
 * @author Leo
 * @date 2024/05/05
 */
public class LhtPropertySource extends EnumerablePropertySource<LhtConfigServer> {

    public LhtPropertySource(String name, LhtConfigServer source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
