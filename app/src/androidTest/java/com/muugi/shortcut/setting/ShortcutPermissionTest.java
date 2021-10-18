package com.muugi.shortcut.setting;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.muugi.shortcut.sample.WelcomeActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShortcutPermissionTest {

    @Rule
    public ActivityScenarioRule<WelcomeActivity> rule = new ActivityScenarioRule<>(WelcomeActivity.class);

//    @Rule
//    public ActivityTestRule<WelcomeActivity> rule = new ActivityTestRule<>(WelcomeActivity.class);



    @Test
    public void check() {
        Context applicationContext = ApplicationProvider.getApplicationContext();
        int check = ShortcutPermission.check(applicationContext);
        Assert.assertEquals(0, check);
    }

    @Test
    public void test() {
//        int check = ShortcutPermission.check(rule.getActivity());
//        Assert.assertEquals(0, check);
        ActivityScenario.launch(WelcomeActivity.class);
        rule.getScenario().onActivity(new ActivityScenario.ActivityAction<WelcomeActivity>() {
            @Override
            public void perform(WelcomeActivity activity) {
                int check = ShortcutPermission.check(activity);
                Assert.assertEquals(9, check);
            }
        });
    }
}