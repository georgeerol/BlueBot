package com.bluebot;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bluebot.droid.ide.BlueBotIDEActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Clifton Craig on 4/12/17.
 * Copyright GE 4/12/17
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivitySpec {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule(MainActivity.class);
    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        this.activity = activityTestRule.getActivity();
        Intents.init();
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();

    }

    @Test
    public void shouldHaveAnIDEButton() throws Exception {
        onView(withText("IDE")).check(matches(isClickable()));
    }

    @Test
    public void clickingTheIDEButtonShouldLaunchTheIDEActivity() throws Exception {
        onView(withText("IDE")).perform(click());
        intended(hasComponent(BlueBotIDEActivity.class.getName().toString()));
    }
}
