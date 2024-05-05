package com.lht.lhtconfig.demo;

import com.lht.lhtconfig.client.config.EnableLhtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableLhtConfig
public class LhtconfigDemoApplication {

    @Value("${lht.a}")
    private String a;

    @Autowired
    private LhtConfigDemo configDemo;

    public static void main(String[] args) {
        SpringApplication.run(LhtconfigDemoApplication.class, args);
    }


    @Bean
    ApplicationRunner applicationRunner(Environment env){
        return args -> {
            ConfigurableEnvironment configEnv = (ConfigurableEnvironment) env;
            System.out.println("====> env.getActiveProfiles():" + env.getActiveProfiles());
            System.out.println("====> configEnv:" + configEnv.getPropertySources());
            System.out.println(a);
            System.out.println(configDemo.getA());
        };
    }


}
