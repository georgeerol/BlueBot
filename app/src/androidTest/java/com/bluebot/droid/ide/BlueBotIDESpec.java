package com.bluebot.droid.ide;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Clifton Craig on 4/8/17.
 * Copyright GE 4/8/17
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BlueBotIDESpec {
    @Rule
    public ActivityTestRule<BlueBotIDEActivity> activityTestRule = new ActivityTestRule(BlueBotIDEActivity.class);

    @Test
    public void shouldIncludeWindowTitleOnScreen() {
        onView(withText("BlueBot IDE")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldHaveAPlaceToEnterCode() throws Exception {
        onView(withContentDescription("Code Entry")).check(matches(isDisplayed()));
    }

    @Test
    public void shouldHaveAButtonToRunCode() throws Exception {
        onView(withContentDescription("Run")).check(matches(isDisplayed()));
    }
}
