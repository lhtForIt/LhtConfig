package com.lht.lhtconfig.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Leo
 * @date 2024/05/05
 */
@Data
@Component
@ConfigurationProperties(prefix = "lhtconfig")
public class LhtConfigDemo {
    private String a;
    private String b;
    private String c;
}
