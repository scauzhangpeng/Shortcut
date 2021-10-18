package com.muugi.shortcut;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.muugi.shortcut.setting.ShortcutPermission;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.muugi.shortcut", appContext.getPackageName());
    }

    @Test
    public void check() {
        Context applicationContext = ApplicationProvider.getApplicationContext();
        int check = ShortcutPermission.check(applicationContext);
        Assert.assertEquals("if fail" ,0, check);
    }
}
