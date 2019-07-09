package com.muugi.shortcut.pin;

import com.muugi.shortcut.core.Action;

/**
 * Created by ZP on 2019-06-19.
 */
public interface BaseRequest {

    void start();

    BaseRequest onCreated(Action<Boolean> action);

    BaseRequest onUpdated(Action<Boolean> action);

    BaseRequest onAutoCreate(Action<Boolean> action);

    BaseRequest onAsyncCreate(Action<Boolean> action);

    BaseRequest onAsyncAutoCreate(Action<Boolean> action);


}
