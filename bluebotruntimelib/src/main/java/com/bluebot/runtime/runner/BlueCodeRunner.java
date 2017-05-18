package com.bluebot.runtime.runner;

import com.bluebot.runtime.bluetooth.BluetoothCodes;
import com.bluebot.runtime.bluetooth.BluetoothCommandSink;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BlueCodeRunner {
    private static final List<String> VALID_COMMANDS = Collections.singletonList("move");
    private final BluetoothCommandSink bluetoothCommandSink;

    public BlueCodeRunner(BluetoothCommandSink bluetoothCommandSink) {
        this.bluetoothCommandSink = bluetoothCommandSink;
    }

    public void run(String blueCode) {
        final String[] sourceLines = blueCode.split("\n");
        Map<Integer, String> sourceCode = parseSourceCode(sourceLines);
        for(String command : sourceCode.values()) {
            bluetoothCommandSink.send(xLate(command));
        }
        bluetoothCommandSink.send(BluetoothCodes.STOP);
    }

    private Map<Integer, String> parseSourceCode(String[] sourceLines) {
        Map<Integer,String> sortedSource= new TreeMap<>();
        for (String eachLine : sourceLines) {
            final String[] tokens = eachLine.split(" ");
            final int lineNumber;
            try { lineNumber = Integer.parseInt(tokens[0]); }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("BlueCode should start with a line number.",e);
            }
            if(!isValidCommand(tokens[1]))
                throw new IllegalArgumentException("A valid command should follow the line number.");
            if(tokens.length > 3)
                throw new IllegalArgumentException("Line " + tokens[0] + ": Unexpected command or text [" + tokens[3] + "] after [" + tokens[2] + "]");
            sortedSource.put(lineNumber, tokens[2]);
        }
        return sortedSource;
    }

    private boolean isValidCommand(String command) {
        return VALID_COMMANDS.contains(command);
    }

    private String xLate(String command) {
        switch (command) {
            case "forward":
                return BluetoothCodes.FORWARD;
            case "backward":
                return BluetoothCodes.BACKWARD;
            case "left":
                return BluetoothCodes.LEFT;
            case "right":
                return BluetoothCodes.RIGHT;
            default:
                return "?";
        }
    }
}
