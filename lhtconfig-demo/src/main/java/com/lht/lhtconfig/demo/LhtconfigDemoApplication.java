package com.lht.lhtconfig.demo;

import com.lht.lhtconfig.client.annotation.EnableLhtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableLhtConfig
@RestController
public class LhtconfigDemoApplication {

    @Value("${lhtconfig.a}")
    private String a;
    @Value("${lhtconfig.b}")
    private String b;

    @Autowired
    private LhtConfigDemo configDemo;

    public static void main(String[] args) {
        SpringApplication.run(LhtconfigDemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(){
        return "a: " + a + "\n" + "b: " + b + "\n" + "demo.a: " + configDemo.getA() + "\n" + "demo.b: " + configDemo.getB() + "\n";
    }

    @Bean
    ApplicationRunner applicationRunner(Environment env, ApplicationContext applicationContext){
        return args -> {
            ConfigurableEnvironment configEnv = (ConfigurableEnvironment) env;
            System.out.println("====> env.getActiveProfiles():" + env.getActiveProfiles());
            System.out.println("====> configEnv:" + configEnv.getPropertySources());
            System.out.println(a);
            System.out.println(configDemo.getA());
            System.out.println(applicationContext);
        };
    }


}
