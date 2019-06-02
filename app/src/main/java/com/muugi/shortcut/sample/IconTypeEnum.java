package com.muugi.shortcut.sample;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by ZP on 2019/3/24.
 */
public class IconTypeEnum {


    public static final int NORMAL = 0;
    public static final int CropCircle = 1;
    public static final int CORNER = 2;

    @IntDef({NORMAL, CropCircle, CORNER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IconType {

    }
}
