package com.ark.norns.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

@Component
public class StartupManager implements ApplicationListener<ApplicationReadyEvent> {
    static Logger log = Logger.getLogger(Properties.class.getName());

    @Autowired
    private MibManager mibManager;

    @Autowired
    public StartupManager(MibManager mibManager) {
        this.mibManager = mibManager;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        log.info("-----------------------------------------");
        log.info("App Name: " + Properties.applicationName);
        log.info("App Data Source: " + Properties.datasourceUrl);
        log.info("Mib Source: " + Properties.mibFilesPath);
        log.info("Walk Retries: " + Properties.snmpWalkRetries);
        log.info("Walk Timeout: " + Properties.snmpWalkTimeout);
        log.info("-----------------------------------------");
        mibManager.createMibFilesIndexes();
    }
}
