package com.ark.norns.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("norns")
public class NornsProperties {
    public static String applicationName = null;
    public static String datasourceUrl = null;
    public static String datasourceUsername = null;
    public static String loggingFile = null;
    public static Boolean datasourcePasswordEncryption = null;
    public static String corsMapping = null;
    public static String allowedOrigin = null;

    public static void setApplicationName(String applicationName) {
        NornsProperties.applicationName = applicationName;
    }

    public static void setDatasourceUrl(String datasourceUrl) {
        NornsProperties.datasourceUrl = datasourceUrl;
    }

    public static void setDatasourceUsername(String datasourceUsername) {
        NornsProperties.datasourceUsername = datasourceUsername;
    }

    public static void setDatasourcePasswordEncryption(Boolean datasourcePasswordEncryption) {
        NornsProperties.datasourcePasswordEncryption = datasourcePasswordEncryption;
    }

    public static void setLoggingFile(String loggingFile) {
        NornsProperties.loggingFile = loggingFile;
    }

    public static void setCorsMapping(String corsMapping) {
        NornsProperties.corsMapping = corsMapping;
    }

    public static void setAllowedOrigin(String allowedOrigin) {
        NornsProperties.allowedOrigin = allowedOrigin;
    }
}
