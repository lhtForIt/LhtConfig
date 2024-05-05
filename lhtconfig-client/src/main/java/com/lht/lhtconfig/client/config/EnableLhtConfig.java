package com.lht.lhtconfig.client.config;

import org.springframework.context.annotation.Import;

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
@Import(LhtConfigRegistrar.class)
public @interface EnableLhtConfig {
}
