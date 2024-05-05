package com.lht.lhtconfig.client;

import java.lang.annotation.*;


/**
 * config client entrypoint
 *
 * @author Leo
 * @date 2024/05/05
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface EnableLhtConfig {
}
