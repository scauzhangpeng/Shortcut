package com.muugi.shortcut.pin;

/**
 * Created by ZP on 2019-06-19.
 */
public interface IntentRequest extends BaseRequest {

    IntentRequest putExtra(String name, boolean value);

    IntentRequest putExtra(String name, String value);

    IntentRequest putExtra(String name, int value);

    IntentRequest putExtra(String name, double value);

    IntentRequest putExtra(String name, long value);

    IntentRequest putExtra(String name, boolean[] value);

    IntentRequest putExtra(String name, String[] value);

    IntentRequest putExtra(String name, int[] value);

    IntentRequest putExtra(String name, double[] value);

    IntentRequest putExtra(String name, long[] value);

}
