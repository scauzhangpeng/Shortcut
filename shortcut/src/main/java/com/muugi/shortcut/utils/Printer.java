package com.muugi.shortcut.utils;

/**
 * Created by ZP on 2020-01-23.
 */
public interface Printer {

    public void log(String tag, String message);
    public void log(String tag, String message, Exception e);
}
