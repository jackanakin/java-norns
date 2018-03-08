package com.ark.norns.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {
    public static final Logger audit = LoggerFactory.getLogger("audit");
    private static final Logger admin = LoggerFactory.getLogger("admin");
    public static final Logger error = LoggerFactory.getLogger("error");

    public static void adminLog(Class sourceClass, String message){
        Logging.audit.info(sourceClass.toString() + "@Usuário X#" + message);
    }

    public static void errorLog(Class sourceClass, String message){
        Logging.error.info(sourceClass.toString() + "@Usuário X#" + message);
    }
}
