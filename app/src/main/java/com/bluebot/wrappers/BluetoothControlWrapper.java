package com.bluebot.wrappers;

import android.bluetooth.BluetoothAdapter;
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

    public boolean isAutoConnecting() {
        return bluetoothSPP.isAutoConnecting();
    }

    public boolean startDiscovery() {
        return bluetoothSPP.startDiscovery();
    }

    public boolean isDiscovery() {
        return bluetoothSPP.isDiscovery();
    }

    public boolean cancelDiscovery() {
        return bluetoothSPP.cancelDiscovery();
    }

    @Override
    public void setupService() {
        bluetoothSPP.setupService();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothSPP.getBluetoothAdapter();
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

    public void setDeviceTarget(boolean isAndroid) {
        bluetoothSPP.setDeviceTarget(isAndroid);
    }

    public void stopAutoConnect() {
        bluetoothSPP.stopAutoConnect();
    }

    @Override
    public void connect(Intent data) {
        bluetoothSPP.connect(data);
    }

    public void connect(String address) {
        bluetoothSPP.connect(address);
    }

    @Override
    public void disconnect() {
        bluetoothSPP.disconnect();
    }

    @Override
    public void setBluetoothConnectionListener(BluetoothControllerConnectionListener bluetoothControllerConnectionListener) {
        bluetoothSPP.setBluetoothStateListener(bluetoothControllerConnectionListener);
    }

    public void setBluetoothStateListener(BluetoothSPP.BluetoothStateListener listener) {
    }

    public void setOnDataReceivedListener(BluetoothSPP.OnDataReceivedListener listener) {
        bluetoothSPP.setOnDataReceivedListener(listener);
    }

    public void setBluetoothConnectionListener(BluetoothSPP.BluetoothConnectionListener listener) {
        bluetoothSPP.setBluetoothConnectionListener(listener);
    }

    public void setAutoConnectionListener(BluetoothSPP.AutoConnectionListener listener) {
        bluetoothSPP.setAutoConnectionListener(listener);
    }

    @Override
    public void enable() {
        bluetoothSPP.enable();
    }

    public void send(byte[] data, boolean CRLF) {
        bluetoothSPP.send(data, CRLF);
    }

    @Override
    public void send(String data, boolean CRLF) {
        bluetoothSPP.send(data, CRLF);
    }

    public String getConnectedDeviceName() {
        return bluetoothSPP.getConnectedDeviceName();
    }

    public String getConnectedDeviceAddress() {
        return bluetoothSPP.getConnectedDeviceAddress();
    }

    public String[] getPairedDeviceName() {
        return bluetoothSPP.getPairedDeviceName();
    }

    public String[] getPairedDeviceAddress() {
        return bluetoothSPP.getPairedDeviceAddress();
    }

    public void autoConnect(String keywordName) {
        bluetoothSPP.autoConnect(keywordName);
    }
}
