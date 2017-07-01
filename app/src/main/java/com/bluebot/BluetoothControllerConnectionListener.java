package com.bluebot;

/**
 * Created by Clifton Craig on 6/30/17.
 * Copyright GE 6/30/17
 */

public interface BluetoothControllerConnectionListener {

    void onDeviceConnected(String name, String address);

    void onDeviceDisconnected();

    void onDeviceConnectionFailed();
}
