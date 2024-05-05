package com.lht.lhtconfig.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Leo
 * @date 2024/05/05
 */
@Data
@AllArgsConstructor
public class Configs {

    private String app;
    private String env;
    private String nameSpace;
    private String pkey;
    private String pval;



}
