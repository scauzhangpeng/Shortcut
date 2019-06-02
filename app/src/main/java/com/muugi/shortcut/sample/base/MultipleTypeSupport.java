package com.muugi.shortcut.sample.base;

import java.util.List;

/**
 * Created by ZP on 2017/8/8.
 */

public interface MultipleTypeSupport<T> {

    int getLayoutId(List<T> list, T t, int position);
}
