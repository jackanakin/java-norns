package com.ark.norns;

import com.ark.norns.application.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(Properties.class)
public class NornsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NornsApplication.class, args);
    }
}
