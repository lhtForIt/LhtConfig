package com.lht.lhtconfig.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author Leo
 * @date 2024/05/18
 */
@Data
@AllArgsConstructor
public class SpringValue {

    private Object bean;
    private String beanName;
    private String key;
    private String placeholder;
    private Field field;

}
