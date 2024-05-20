package com.lht.lhtconfig.server;

import com.lht.lhtconfig.server.dao.ConfigsMapper;
import com.lht.lhtconfig.server.model.Configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * config server endpoint
 *
 * @author Leo
 * @date 2024/05/05
 */
@RestController
public class LhtConfigController {

    @Autowired
    ConfigsMapper configsMapper;

    @Autowired
    DistributedLocks locks;

    Map<String, Long> VERSION = new HashMap<>();

    @GetMapping("/list")
    public List<Configs> list(String app, String env, String nameSpace) {
        return configsMapper.list(app, env, nameSpace);
    }

    @PostMapping("/update")
    public List<Configs> update (@RequestParam("app") String app,
                                 @RequestParam("env") String env,
                                 @RequestParam("nameSpace") String nameSpace,
                                 @RequestBody Map<String,String> params) {
        params.forEach((k,v)->{
            insertOrUpdate(new Configs(app, env, nameSpace, k, v));
        });
        VERSION.put(app + "-" + env + "-" + nameSpace, System.currentTimeMillis());
        return configsMapper.list(app, env, nameSpace);
    }

    @GetMapping("/status")
    public boolean status(){
        return locks.getLocked().get();
    }

    private void insertOrUpdate(Configs configs) {
        Configs conf = configsMapper.select(configs);
        if (conf == null) {
            configsMapper.insert(configs);
        } else {
            configsMapper.update(configs);
        }
    }

    @GetMapping("/version")
    public Long getVersion(String app, String env, String nameSpace) {
        return VERSION.getOrDefault(app + "-" + env + "-" + nameSpace, -1L);
    }


}
