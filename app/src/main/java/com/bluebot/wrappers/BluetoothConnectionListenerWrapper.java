package com.bluebot.wrappers;

import com.bluebot.BluetoothControllerConnectionListener;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by Clifton Craig on 7/6/17.
 * Copyright GE 7/6/17
 */
class BluetoothConnectionListenerWrapper implements BluetoothSPP.BluetoothConnectionListener {
    private final BluetoothControllerConnectionListener bluetoothControllerConnectionListener;

    public BluetoothConnectionListenerWrapper(BluetoothControllerConnectionListener bluetoothControllerConnectionListener) {
        this.bluetoothControllerConnectionListener = bluetoothControllerConnectionListener;
    }

    @Override
    public void onDeviceConnected(String name, String address) {
        bluetoothControllerConnectionListener.onDeviceConnected(name, address);
    }

    @Override
    public void onDeviceDisconnected() {
        bluetoothControllerConnectionListener.onDeviceDisconnected();
    }

    @Override
    public void onDeviceConnectionFailed() {
        bluetoothControllerConnectionListener.onDeviceConnectionFailed();
    }
}
