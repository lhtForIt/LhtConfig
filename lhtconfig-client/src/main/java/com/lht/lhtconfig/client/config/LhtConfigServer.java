package com.lht.lhtconfig.client.config;

/**
 * @author Leo
 * @date 2024/05/05
 */
public interface LhtConfigServer {

    String[] getPropertyNames();

    String getProperty(String name);


}
