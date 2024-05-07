package com.lht.lhtconfig.client.config;

import com.lht.lhtconfig.client.repository.LhtRepository;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leo
 * @date 2024/05/05
 */
@Data
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, PriorityOrdered, EnvironmentAware {

    private final static String LHT_CONFIG_PROPERTY_SOURCE_NAME = "lhtPropertySource";
    private final static String LHT_CONFIG_PROPERTY_SOURCES_NAME = "lhtPropertySources";
    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        //已经扔进去了就不管
        if (env.containsProperty(LHT_CONFIG_PROPERTY_SOURCES_NAME)) {
            return;
        }

        String app = env.getProperty("lhtconfig.app", "app1");
        String pEnv = env.getProperty("lhtconfig.env", "dev");
        String namespace = env.getProperty("lhtconfig.namespace", "public");
        String configServer = env.getProperty("lhtconfig.configServer", "http:localhost:9129");

        ConfigMeta meta = new ConfigMeta(app, pEnv, namespace, configServer);

        LhtConfigServer lhtConfigServer = LhtConfigServer.getDefault(meta);
        LhtPropertySource lhtPropertySource = new LhtPropertySource(LHT_CONFIG_PROPERTY_SOURCE_NAME, lhtConfigServer);
        //这个是组合的配置源，可以组合多个配置源，比如一个环境一个
        CompositePropertySource compositePropertySource = new CompositePropertySource(LHT_CONFIG_PROPERTY_SOURCES_NAME);
        compositePropertySource.addPropertySource(lhtPropertySource);

        env.getPropertySources().addFirst(compositePropertySource);
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }
}
