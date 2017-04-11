package com.bluebot.runtime.runner;

import com.bluebot.runtime.bluetooth.BluetoothCommandSink;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.bluebot.runtime.bluetooth.BluetoothCodes.BACKWARD;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.FORWARD;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.LEFT;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.RIGHT;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.STOP;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Clifton Craig on 4/9/17.
 * Copyright GE 4/9/17
 */
public class BlueCodeRunnerSpec {

    private static final Random random = new Random();
    private BlueCodeRunner blueCodeRunner;
    private List<String> actualCommands = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        //All tests are given a BlueCodeRunner that writes commands back to the test's
        //actualCommands collection
        blueCodeRunner = new BlueCodeRunner(new BluetoothCommandSink() {
            @Override
            public void send(String command) {
                actualCommands.add(command);
            }
        });
    }

    @Test
    public void lastCommandShouldAlwaysBeStop() throws Exception {
        //When we pass a single line BlueCode source to the runner...
        blueCodeRunner.run("10 move forward");
        //Then the last command should be STOP
        Assert.assertThat(actualCommands, is(asList(FORWARD, STOP)));

        actualCommands.clear();

        //When we pass a multi-line BlueCode source to the runner...
        blueCodeRunner.run("10 move forward\n20 move left");
        //Then the last command should be STOP
        Assert.assertThat(actualCommands, is(asList(FORWARD, LEFT, STOP)));
    }

    @Test
    public void blueCodeShouldStartWithALineNumber() throws Exception {
        assertValidCodeVsInvalidCode("move forward", "10 move forward", "BlueCode should start with a line number.");
    }

    @Test
    public void aValidCommandShouldFollowTheLineNumber() throws Exception {
        assertValidCodeVsInvalidCode("10 spaghettios", "10 move far", "A valid command should follow the line number.");
    }

    @Test
    public void multipleCommandsShouldBeSeparatedByNewlines() throws Exception {
        assertValidCodeVsInvalidCode(
                "10 move forward 20 move backward", "10 move left\n20 move right","Unexpected command or text [20] after [forward]");
    }

    @Test
    public void validBlueCodeCommandsShouldTranslateToBluetoothCodes() throws Exception {
        //Given some valid BlueCode
        final String validBlueCode = "10 move forward\n" +
                "20 move right\n" +
                "30 move forward\n" +
                "40 move right";
        //When we pass it to the runner...
        blueCodeRunner.run(validBlueCode);
        //Then it should send valid commands to the BlueBot
        Assert.assertThat(actualCommands, is(asList(FORWARD, RIGHT, FORWARD, RIGHT, STOP)));
    }

    @Test
    public void instructionsShouldExecuteInLineNumberOrder() throws Exception {
        //Given some valid, unordered BlueCode...
        final String validBlueCode = "30 move left\n" +
                "20 move backward\n" +
                "40 move forward\n" +
                "10 move right";
        //When we pass it to the runner...
        blueCodeRunner.run(validBlueCode);
        //Then it should send valid commands to the BlueBot
        Assert.assertThat(actualCommands, is(asList(RIGHT, BACKWARD, LEFT, FORWARD, STOP)));
    }

    @Test
    public void randomBlueCodeSendsCorrectBluetoothCodes() throws Exception {
        final List<String> possibleSubCommands = asList("left", "right", "forward", "backward");
        final List<String> matchingBluetoothCodes = asList(LEFT, RIGHT, FORWARD, BACKWARD);

        final List<String> expectedCodes = new ArrayList<>();
        StringBuilder program = new StringBuilder();
        for (int i=0; i < possibleSubCommands.size()*5; i++) {
            final int nextCommand = random.nextInt(4);
            final String lineNumber = (i * 10) + " ";
            expectedCodes.add(matchingBluetoothCodes.get(nextCommand));
            program.append(lineNumber).append("move ")
                    .append(possibleSubCommands.get(nextCommand)).append('\n');
        }
        blueCodeRunner.run(program.toString());
        expectedCodes.add(STOP);
        Assert.assertThat(actualCommands, is(expectedCodes));
    }

    //Auxiliary methods
    private void assertValidCodeVsInvalidCode(String invalidCode, String validCode, String expectedErrorText) {
        final String invalidCodeDescription = "Invalid Code:\n"
                + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"
                + invalidCode
                + "\n<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";
        try {
            blueCodeRunner.run(invalidCode);
            fail("Should throw an error with invalid BlueCode. "
                    + invalidCodeDescription
                    + " Expected error: " + expectedErrorText);
        } catch (IllegalArgumentException e) {
            final String message = e.getMessage();
            assertTrue("Message should describe the failure.\nExpected:\n" + expectedErrorText
                            + "\nbut was:\n" + message + "\n" + invalidCodeDescription,
                    message.contains(expectedErrorText));
        }
        blueCodeRunner.run(validCode);
    }
}