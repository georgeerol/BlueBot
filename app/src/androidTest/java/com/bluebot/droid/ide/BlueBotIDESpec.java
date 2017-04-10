package com.bluebot.droid.ide;

import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.bluebot.droid.ide.runtime.RequestProcessor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Clifton Craig on 4/8/17.
 * Copyright GE 4/8/17
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BlueBotIDESpec implements RequestProcessor {
    @Rule
    public ActivityTestRule<BlueBotIDEActivity> activityTestRule = new ActivityTestRule(BlueBotIDEActivity.class);
    private String dataPassedToRequestProcessor;
    private String requestTypePassedToRequestProcessor;
    private ResponseHandler responseHandlerPassedToBind;

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
    @Test
    public void theIDEShouldDependOnARequestProcessor() throws Exception {
        activityTestRule.getActivity().setRequestProcessor(this);
    }

    @Override
    public void bind(ResponseHandler responseHandler){
        this.responseHandlerPassedToBind = responseHandler;
    }

    @Test
    public void theIDEActivityShouldBindTheRequestProcessorWhenSet() throws Exception {
        activityTestRule.getActivity().setRequestProcessor(this);
        assertNotNull(responseHandlerPassedToBind);
    }

    @Test
    public void boundResponseHandlerShouldChangeTheUI() throws Exception {
        //Given
        theIDEActivityShouldBindTheRequestProcessorWhenSet();
        //When we have an error response
        Map<String,String> response = new HashMap<String,String>(){{ put(UIResponseType.ERROR,"Stupidity Error!"); }};
        //And we send it to the responseHandler...
        responseHandlerPassedToBind.responseForRequest(UIRequestTypes.EXECUTE_CODE, response);
        //Then the UI should light up with the error
        onView(withContentDescription("Error message")).check(matches(isDisplayed()));
    }

    @Test
    public void theIDEActivityShouldNotShowErrorMessagesByDefault() throws Exception {
        onView(withContentDescription("Error message")).check(matches(not(isDisplayed())));
    }

    @Override
    public void processRequest(String requestType, Object data) {
        this.requestTypePassedToRequestProcessor = requestType;
        this.dataPassedToRequestProcessor = String.valueOf(data);
    }

    @Test
    public void enteringCodeAndPressingRunShouldInvokeCodeExecutor() throws Exception {
        //Given this test case pretends to be a code executor
        activityTestRule.getActivity().setRequestProcessor(this);
        //And some BlueBot code...
        String someBlueBotCode = "wake up bot\n" +
                "spin around\n" +
                "say hello";

        //When we type the code in...
        onView(withContentDescription("Code Entry"))
                .perform(ViewActions.typeText(someBlueBotCode));

        //And click the run button
        onView(withContentDescription("Run")).perform(ViewActions.click());

        //Then our Code Executor should be asked to execute someBlueBotCode
        assertEquals("An EXECUTE_CODE request should be given.", UIRequestTypes.EXECUTE_CODE,requestTypePassedToRequestProcessor);
        //And our Code Executor should be asked to execute someBlueBotCode
        assertEquals("The given code should be executed.", someBlueBotCode, dataPassedToRequestProcessor);
    }
}
