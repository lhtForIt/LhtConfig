package com.lht.lhtconfig.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Optional;

/**
 *
 * register lht config property source bean
 *
 * @author Leo
 * @date 2024/05/05
 */
@Slf4j
public class LhtConfigRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        log.info("register propertySourcesProcessor bean");
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames()).filter(d -> "propertySourcesProcessor".equals(d)).findFirst();
        if (first.isPresent()) {
            log.info("propertySourcesProcessor bean already exist");
            return;
        }
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(PropertySourcesProcessor.class).getBeanDefinition();
        registry.registerBeanDefinition("propertySourcesProcessor", beanDefinition);

    }
}
