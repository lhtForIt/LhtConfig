package com.lht.lhtconfig.client.config;

import com.lht.lhtconfig.client.repository.LhtRepository;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<String> keys = calcChangeSet(this.config, event.config());
        this.config = event.config();
        if (!CollectionUtils.isEmpty(keys)) {
            //触发EnvironmentChangeEvent事件,EnvironmentChangeEvent的构造函数参数就是配置属性的key的值得集合
            this.applicationContext.publishEvent(new EnvironmentChangeEvent(keys));
        }
    }

    private Set<String> calcChangeSet(Map<String, String> oldConfig, Map<String, String> newConfig) {

        if (CollectionUtils.isEmpty(oldConfig)||CollectionUtils.isEmpty(newConfig)) return CollectionUtils.isEmpty(oldConfig) ? newConfig.keySet() : oldConfig.keySet();

        //分两段考虑，old里面和new不等的，包括值改变或者old删除；new里面新增的
        Set<String> news = oldConfig.keySet().stream().filter(key -> !newConfig.get(key).equals(oldConfig.get(key))).collect(Collectors.toSet());
        newConfig.keySet().stream().filter(key -> !oldConfig.containsKey(key)).forEach(news::add);

        return news;
    }
}
