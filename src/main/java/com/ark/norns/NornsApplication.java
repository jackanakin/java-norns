package com.ark.norns;

import com.ark.norns.application.MibManager;
import com.ark.norns.application.Properties;
import com.ark.norns.dataStructure.MibFile;
import com.ark.norns.dataStructure.MibFileOid;
import com.ark.norns.dataStructure.TreeNode;
import com.ark.norns.service.MibFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

        @Autowired
        private MibManager mibManager;

        @Override
        public void run(String... strings) throws Exception {
            log.info("-----------------------------------------");
            log.info("App Name: " + Properties.applicationName);
            log.info("App Data Source: " + Properties.datasourceUrl);
            log.info("Mib Source: " + Properties.mibFilesPath);
            log.info("Walk Retries: " + Properties.snmpWalkRetries);
            log.info("Walk Timeout: " + Properties.snmpWalkTimeout);
            log.info("-----------------------------------------");
            TreeNode<Integer, String, MibFileOid> root = mibManager.initializeYggdrasil(new TreeNode(1, "1", new MibFileOid("1", "iso", new MibFile(Properties.mibFileRoot)), 0));
            File folder = new File(Properties.mibFilesPath);
            List<String> folderFiles = new ArrayList<>(Arrays.asList(folder.list()));
            Set<MibFile> mibFileSet = mibManager.initializeMibsIndexes(root, folderFiles);
            mibFileService.getMibFileDAO().saveAll(mibFileSet);
        }
    }
}
