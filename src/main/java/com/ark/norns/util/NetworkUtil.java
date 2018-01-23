package com.ark.norns.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NetworkUtil {
    private static final Pattern IPv4PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validateIPv4(String ip) {
        return IPv4PATTERN.matcher(ip).matches();
    }
}