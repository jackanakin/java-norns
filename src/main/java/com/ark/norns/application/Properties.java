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

    public static void updatePropertie(String propertie, String value) {
        try {
            String filePath = Properties.resourcePath + "application.properties";

            PropertiesConfiguration config = new PropertiesConfiguration(filePath);
            config.setProperty(propertie, value);
            config.save();

            Logging.admin.info("Config Property Updated by " + "@Usuário x");
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
}
