package com.ark.norns.application;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("norns")
public class Properties {
    public static String applicationName = null;
    public static String datasourceUrl = null;
    public static String datasourceUsername = null;
    public static Boolean datasourcePasswordEncryption = null;
    public static String loggingFile = null;
    public static String corsMapping = null;
    public static String allowedOrigin = null;
    public static String resourcePath = null;
    public static String mibFilesPath = null;
    public static Integer snmpWalkRetries = null;
    public static Long snmpWalkTimeout = null;
    public static String snmpWalkOidStart = null;
    public static Long snmpWalkVersion = null;
    public static String mibFileRoot = null;
    public static String mandatoryMibFilesPath = null;

    public static void setMibFileRoot(String mibFileRoot) {
        Properties.mibFileRoot = mibFileRoot;
    }

    public static void setSnmpWalkVersion(Long snmpWalkVersion) {
        Properties.snmpWalkVersion = snmpWalkVersion;
    }

    public static void setSnmpWalkOidStart(String snmpWalkOidStart) {
        Properties.snmpWalkOidStart = snmpWalkOidStart;
    }

    public static void setSnmpWalkRetries(Integer snmpWalkRetries) {
        Properties.snmpWalkRetries = snmpWalkRetries;
    }

    public static void setSnmpWalkTimeout(Long snmpWalkTimeout) {
        Properties.snmpWalkTimeout = snmpWalkTimeout;
    }

    public static void setMibFilesPath(String mibFilesPath) {
        Properties.mibFilesPath = mibFilesPath;
    }

    public static void updatePropertie(String propertie, String value) {
        try {
            String filePath = Properties.resourcePath + "application.properties";

            PropertiesConfiguration config = new PropertiesConfiguration(filePath);
            config.setProperty(propertie, value);
            config.save();

            Logging.adminLog(Properties.class, "Config Property Updated by " + "@Usuário x");
        } catch (Exception e) {
            Logging.error.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setResourcePath(String resourcePath) {
        Properties.resourcePath = resourcePath;
    }

    public static void setApplicationName(String applicationName) {
        Properties.applicationName = applicationName;
    }

    public static void setDatasourceUrl(String datasourceUrl) {
        Properties.datasourceUrl = datasourceUrl;
    }

    public static void setDatasourceUsername(String datasourceUsername) {
        Properties.datasourceUsername = datasourceUsername;
    }

    public static void setDatasourcePasswordEncryption(Boolean datasourcePasswordEncryption) {
        Properties.datasourcePasswordEncryption = datasourcePasswordEncryption;
    }

    public static void setLoggingFile(String loggingFile) {
        Properties.loggingFile = loggingFile;
    }

    public static void setCorsMapping(String corsMapping) {
        Properties.corsMapping = corsMapping;
    }

    public static void setAllowedOrigin(String allowedOrigin) {
        Properties.allowedOrigin = allowedOrigin;
    }

    public static void setMandatoryMibFilesPath(String mandatoryMibFilesPath) {
        Properties.mandatoryMibFilesPath = mandatoryMibFilesPath;
    }
}
