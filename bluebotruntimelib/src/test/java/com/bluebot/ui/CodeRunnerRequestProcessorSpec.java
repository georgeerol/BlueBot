package com.bluebot.ui;

import com.bluebot.runtime.runner.BlueCodeRunner;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Created by Clifton Craig on 4/11/17.
 * Copyright GE 4/11/17
 */

public class CodeRunnerRequestProcessorSpec {

    private CodeRunnerRequestProcessor requestProcessor;
    private List<String> programsPassedToRun = new LinkedList<>();

    @Before
    public void setUp() throws Exception {
        //Given a mockBlueCodeRunner that caches all executed code in this test case...
        final BlueCodeRunner mockBlueCodeRunner = new BlueCodeRunner(null) {
            @Override
            public void run(String blueCode) {
                CodeRunnerRequestProcessorSpec.this.programsPassedToRun.add(blueCode);
                synchronized (CodeRunnerRequestProcessorSpec.this)
                { CodeRunnerRequestProcessorSpec.this.notify(); }
            }
        };
        requestProcessor = new CodeRunnerRequestProcessor(mockBlueCodeRunner);
    }

    @Test
    public void requestsShouldNotBeProcessedImmediately() throws Exception {
        requestProcessor.processRequest(UIRequestTypes.EXECUTE_CODE, "some irrelevant bluecode");
        assertTrue("No code should be passed to the code runner.", programsPassedToRun.isEmpty());
    }

    @Test
    public void shouldBeRunnable() throws Exception {
        assertTrue("should be runnable", requestProcessor instanceof Runnable);
    }

    @Test
    public void requestShouldBeProcessedByEachRun() throws Exception {
        requestsShouldNotBeProcessedImmediately();
        requestProcessor.run();
        assertNotNull("Request should be processed by each run.", programsPassedToRun);
    }

    @Test
    public void responsesShouldBeReturned() throws Exception {
        //Given a bound response handler
        requestProcessor.bind(new ResponseHandler() {
            @Override
            public void responseForRequest(String requestType, Map<String, Object> response) {

            }
        });

    }

    public static class CodeRunnerRequestProcessorProgramQueuingFeatureSpec extends CodeRunnerRequestProcessorSpec {

        private List<String> individualPrograms;

        @Before
        public void setUp() throws Exception {
            super.setUp();
            individualPrograms = Arrays.asList("first program", "second program", "third program");
            for(String eachProgram : individualPrograms) {
                super.requestProcessor.processRequest(UIRequestTypes.EXECUTE_CODE, eachProgram);
            }
        }

        @Test
        public void multipleRequestsShouldBeQueuedThenProcessedByEachRun() throws Exception {
            super.requestProcessor.run();
            assertEquals("Should run each program in order.", individualPrograms, super.programsPassedToRun);
        }

        @Test
        public void requestProcessorCanBeSetToRunIndefinitely() throws Exception {
            super.requestProcessor.setRunIndefinitely(true);
            waitForAllProgramsAndClearQueue();
            super.requestProcessor.processRequest(UIRequestTypes.EXECUTE_CODE,"fourth program");
            synchronized (this) { wait(2000); }
            assertEquals("Should run the fourth program.", 1, super.programsPassedToRun.size());
            assertEquals("Should run the fourth program.", "fourth program", super.programsPassedToRun.get(0));
        }

        @Test
        public void shouldStopWhenSetToNotRunIndefinitely() throws Exception {
            super.requestProcessor.setRunIndefinitely(true);
            waitForAllProgramsAndClearQueue();
            super.requestProcessor.setRunIndefinitely(false);
            super.requestProcessor.processRequest(UIRequestTypes.EXECUTE_CODE,"fourth program");
            synchronized (this) { wait(1000); }
            assertEquals("Should run the fourth program.", 0, super.programsPassedToRun.size());
        }

        private void waitForAllProgramsAndClearQueue() throws InterruptedException {
            for (int i = 0; i < 3; i++) { synchronized (this) { wait(100); } }
            super.programsPassedToRun.clear();
        }
    }

    public static class CodeRunnerRequestProcessorBindingSpec {

        private CodeRunnerRequestProcessor requestProcessor;
        private Map<String, Object> responseGivenToBoundResponseHandler;
        private String requestTypeGivenToBoundResponseHandler;
        boolean responseHandlerWasInvoked;
        boolean mockRunnerShouldThrowAnException;
        private IllegalArgumentException testException = new IllegalArgumentException("<Bad code>");

        @Before
        public void setUp() throws Exception {
            //Given a mockBlueCodeRunner that caches all executed code in this test case...
            final BlueCodeRunner mockBlueCodeRunner = new BlueCodeRunner(null) {
                @Override
                public void run(String blueCode) {
                    if(mockRunnerShouldThrowAnException)
                        throw testException;
                }
            };
            requestProcessor = new CodeRunnerRequestProcessor(mockBlueCodeRunner);
            //And given a bound response handler...
            requestProcessor.bind(new ResponseHandler() {
                @Override
                public void responseForRequest(String requestType, Map<String, Object> response) {
                    responseHandlerWasInvoked = true;
                    requestTypeGivenToBoundResponseHandler = requestType;
                    responseGivenToBoundResponseHandler = response;
                }
            });
        }

        @Test
        public void shouldPassResultFromBlueCodeRunnerBackToBoundResponseHandler() throws Exception {
            //When we process a request...
            requestProcessor.processRequest(UIRequestTypes.EXECUTE_CODE,"<code>");
            //and it runs...
            requestProcessor.run();
            assertTrue("Should invoke the response handler.", responseHandlerWasInvoked);
            assertEquals("Request type given to the response handler should match what was given to processRequest.",
                    requestTypeGivenToBoundResponseHandler, UIRequestTypes.EXECUTE_CODE);
            assertEquals(new HashMap<String, String>() {{
                put(UIResponseType.STATUS, UIResponseType.SUCCESS);
            }},responseGivenToBoundResponseHandler);
        }

        @Test
        public void shouldPassErrorStatusBackWhenRunnerThrowsException() throws Exception {
            //When we set the mock runner to throw an exception...
            mockRunnerShouldThrowAnException = true;
            //When we process a request...
            requestProcessor.processRequest(UIRequestTypes.EXECUTE_CODE,"<code>");
            //and it runs...
            requestProcessor.run();
            assertTrue("Should invoke the response handler.", responseHandlerWasInvoked);
            assertEquals("the response handler should receive an error status.", responseGivenToBoundResponseHandler.get(UIResponseType.STATUS), UIResponseType.ERROR);
            assertSame("Exception should be returned as part of the response.", testException, responseGivenToBoundResponseHandler.get(UIResponseType.DETAIL));
        }
    }
}
