package com.muugi.shortcut.utils;

/**
 * Created by ZP on 2020-01-23.
 */
public class Logger implements Printer {

    private Printer mPrinter;

    public static Logger get() {
        return Inner.INSTANCE;
    }

    private Logger() {
    }

    @Override
    public void log(String tag, String message) {
        if (mPrinter != null) {
            mPrinter.log(tag, message);
        }
    }

    @Override
    public void log(String tag, String message, Exception e) {
        if (mPrinter != null) {
            mPrinter.log(tag, message, e);
        }
    }

    private static final class Inner {
        private static final Logger INSTANCE = new Logger();
    }

    public void setLogger(Printer printer) {
        mPrinter = printer;
    }

}
