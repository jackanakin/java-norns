package com.ark.norns.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {
    public static final Logger audit = LoggerFactory.getLogger("audit");
    public static final Logger admin = LoggerFactory.getLogger("admin");
    public static final Logger error = LoggerFactory.getLogger("error");
}
