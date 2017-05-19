package com.bluebot.runtime.bluetooth;

/**
 * Created by Clifton Craig on 4/9/17.
 * Copyright GE 4/9/17
 */

public interface BluetoothCommandSink {
    void send(String command);
}
