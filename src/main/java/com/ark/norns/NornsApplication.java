package com.ark.norns;

import com.ark.norns.application.Properties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@SpringBootApplication
@EnableConfigurationProperties(Properties.class)
public class NornsApplication {
    static Logger log = Logger.getLogger(Properties.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(NornsApplication.class, args);
    }

    @Service
    static class Startup implements CommandLineRunner {
        @Override
        public void run(String... strings) throws Exception {
            log.info("-----------------------------------------");
            log.info("App Name: " + Properties.applicationName);
            log.info("App Data Source: " + Properties.datasourceUrl);
            log.info("-----------------------------------------");
        }
    }
}
