package com.ark.norns;

import com.ark.norns.application.MibManager;
import com.ark.norns.application.Properties;
import com.ark.norns.dataStructure.MibFile;
import com.ark.norns.service.MibFileService;
import com.ark.norns.specification.MibFileSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.domain.Specification;
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
        @Autowired
        private MibFileService mibFileService;

        @Override
        public void run(String... strings) throws Exception {
            log.info("-----------------------------------------");
            log.info("App Name: " + Properties.applicationName);
            log.info("App Data Source: " + Properties.datasourceUrl);
            log.info("Mib Source: " + Properties.mibFilesPath);
            log.info("Walk Retries: " + Properties.snmpWalkRetries);
            log.info("Walk Timeout: " + Properties.snmpWalkTimeout);
            log.info("-----------------------------------------");
            MibManager.initializeYggdrasil(MibManager.root);
            MibManager.initializeMibsIndexes();
            Specification<MibFile> mibFileSpecification = MibFileSpecification.translateVariableBindingIntoMibFileOid();
            //Set<MibFile> mibFiles = new HashSet<MibFile>(mibFileService.getMibFileDAO().getMibFileRepository().findAll(mibFileSpecification));
        }
    }
}
