package com.bluebot;

import android.content.Intent;

/**
 * Created by Clifton Craig on 6/30/17.
 * Copyright GE 6/30/17
 */

public interface BluetoothControl {
    void stopService();

    boolean isBluetoothEnabled();

    void enable();

    boolean isServiceAvailable();

    void setupService();

    void startService(boolean deviceOther);

    void connect(Intent data);

    int getServiceState();

    void disconnect();


    void setBluetoothConnectionListener(BluetoothControllerConnectionListener bluetoothControllerConnectionListener);

    void send(String command, boolean crLF);

    boolean isBluetoothAvailable();
}
