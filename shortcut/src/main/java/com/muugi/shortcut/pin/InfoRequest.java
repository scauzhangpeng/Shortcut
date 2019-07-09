package com.muugi.shortcut.pin;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.IconCompat;

/**
 * Created by ZP on 2019-06-16.
 */
public interface InfoRequest extends BaseRequest {

    InfoRequest setShortLabel(CharSequence label);

    InfoRequest setLongLabel(CharSequence label);

    InfoRequest setDisabledMessage(CharSequence disabledMessage);

    InfoRequest setIcon(Bitmap bitmap);

    InfoRequest setIcon(Drawable drawable);

    InfoRequest setIcon(IconCompat icon);

    InfoRequest setActivity(ComponentName activity);

    InfoRequest setAlwaysBadge();

    IntentRequest setIntent(Class<?> clz);

    IntentRequest setIntent(Intent intent);

    IntentRequest setIntent(Intent[] intents);

    InfoRequest updateIfExist(boolean updateIfExist);

    InfoRequest createWithSameName(boolean isAutoCreate);

    InfoRequest iconShapeWithLauncher(boolean isIconAutoShape);



}
