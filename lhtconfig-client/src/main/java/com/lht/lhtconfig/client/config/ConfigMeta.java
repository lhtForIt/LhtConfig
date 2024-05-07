package com.lht.lhtconfig.client.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leo
 * @date 2024/05/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigMeta {

    private String app;
    private String pEnv;
    private String namespace;
    private String configServer;

    public String genKey() {
        return this.app + this.pEnv + this.namespace;
    }

}
