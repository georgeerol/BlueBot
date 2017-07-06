package com.bluebot.wrappers;

import android.content.Intent;

import com.bluebot.BluetoothControl;
import com.bluebot.BluetoothControllerConnectionListener;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by Clifton Craig on 6/30/17.
 * Copyright GE 6/30/17
 */

public class BluetoothControlWrapper implements BluetoothControl {
    private final BluetoothSPP bluetoothSPP;

    public BluetoothControlWrapper(BluetoothSPP bluetoothSPP) {
        this.bluetoothSPP = bluetoothSPP;
    }

    @Override
    public boolean isBluetoothAvailable() {
        return bluetoothSPP.isBluetoothAvailable();
    }

    @Override
    public boolean isBluetoothEnabled() {
        return bluetoothSPP.isBluetoothEnabled();
    }

    @Override
    public boolean isServiceAvailable() {
        return bluetoothSPP.isServiceAvailable();
    }

    @Override
    public void setupService() {
        bluetoothSPP.setupService();
    }

    @Override
    public int getServiceState() {
        return bluetoothSPP.getServiceState();
    }

    @Override
    public void startService(boolean isAndroid) {
        bluetoothSPP.startService(isAndroid);
    }

    @Override
    public void stopService() {
        bluetoothSPP.stopService();
    }

    @Override
    public void connect(Intent data) {
        bluetoothSPP.connect(data);
    }

    @Override
    public void disconnect() {
        bluetoothSPP.disconnect();
    }

    @Override
    public void setBluetoothConnectionListener(final BluetoothControllerConnectionListener bluetoothControllerConnectionListener) {
        bluetoothSPP.setBluetoothConnectionListener(new BluetoothConnectionListenerWrapper(bluetoothControllerConnectionListener));
    }

    @Override
    public void enable() {
        bluetoothSPP.enable();
    }

    @Override
    public void send(String data, boolean CRLF) {
        bluetoothSPP.send(data, CRLF);
    }

}
