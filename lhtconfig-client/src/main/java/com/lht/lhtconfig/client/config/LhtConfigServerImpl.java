package com.lht.lhtconfig.client.config;

import com.lht.lhtconfig.client.repository.LhtRepository;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author Leo
 * @date 2024/05/05
 */
public class LhtConfigServerImpl implements LhtConfigServer{

    Map<String, String> config;
    ApplicationContext applicationContext;

    public LhtConfigServerImpl(ApplicationContext applicationContext, Map<String, String> config) {
        this.config = config;
        this.applicationContext = applicationContext;
    }

    @Override
    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    @Override
    public String getProperty(String name) {
        return this.config.get(name);
    }

    @Override
    public void onChange(LhtRepository.ChangeEvent event) {
        this.config = event.config();
        //触发EnvironmentChangeEvent事件,EnvironmentChangeEvent的构造函数参数就是配置属性的key的值得集合
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(this.config.keySet()));
    }
}
