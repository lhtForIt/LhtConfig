package com.lht.lhtconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import com.lht.lhtconfig.client.config.ConfigMeta;
import com.lht.lhtconfig.client.config.Configs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Leo
 * @date 2024/05/07
 */
@Slf4j
@Data
public class LhtRepositoryImpl implements LhtRepository {


    private ConfigMeta configMeta;
    private Map<String, Long> versionMap = new HashMap<>();
    private Map<String, Map<String, String>> configsMap = new HashMap<>();
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private List<ChangeListener> changeListeners = new ArrayList<>();

    public LhtRepositoryImpl(ConfigMeta configMeta) {
        this.configMeta = configMeta;
        scheduledExecutorService.scheduleWithFixedDelay(this::heartBeat, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    public Map<String, String> getConfig() {
        String key = configMeta.genKey();
        if (configsMap.containsKey(key)) {
            return configsMap.get(key);
        }
        return findAll();
    }

    @NotNull
    private Map<String, String> findAll() {
        String listPath = configMeta.getConfigServer() + "/list?app=" + configMeta.getApp() + "&env=" + configMeta.getPEnv() + "&nameSpace=" + configMeta.getNamespace();
        log.debug("[LHTCONFIG] list all configs from server");
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>() {});
        Map<String, String> configMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(configs)) {
            configs.forEach(config -> {
                configMap.put(config.getPkey(), config.getPval());
            });
        }
        return configMap;
    }

    private void heartBeat() {
        String heartBeatPath = configMeta.getConfigServer() + "/version?app=" + configMeta.getApp() + "&env=" + configMeta.getPEnv() + "&nameSpace=" + configMeta.getNamespace();
        Long version = HttpUtils.httpGet(heartBeatPath, Long.class);
        String key = configMeta.genKey();
        Long oldVersion = versionMap.getOrDefault(key, -1L);
        if (version > oldVersion) {//属性发生了变化
            log.debug("[LHTCONFIG] current=" + version + ", old=" + oldVersion);
            log.debug("[LHTCONFIG] new update new configs");
            versionMap.put(key, version);
            Map<String, String> newConfigs = findAll();
            configsMap.put(key, newConfigs);
            log.debug("[LHTCONFIG] fire an EnvironmentChangeEvent with keys:" + newConfigs.keySet());
            changeListeners.stream().forEach(d -> d.onChange(new ChangeEvent(configMeta, newConfigs)));
        }
    }

}
