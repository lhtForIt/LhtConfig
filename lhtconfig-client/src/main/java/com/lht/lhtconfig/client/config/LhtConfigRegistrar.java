package com.lht.lhtconfig.client.config;

import com.lht.lhtconfig.client.value.SpringValueProcessor;
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

        registerBean(registry, PropertySourcesProcessor.class);
        registerBean(registry, SpringValueProcessor.class);

    }

    private static void registerBean(BeanDefinitionRegistry registry, Class<?> aClass) {
        Optional<String> first = Arrays.stream(registry.getBeanDefinitionNames()).filter(d -> aClass.getName().equals(d)).findFirst();
        if (first.isPresent()) {
            log.info(first.get() + " bean already exist");
            return;
        }
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(aClass).getBeanDefinition();
        registry.registerBeanDefinition(aClass.getName(), beanDefinition);
        log.info("register " + aClass.getName() + " bean");
    }

}
