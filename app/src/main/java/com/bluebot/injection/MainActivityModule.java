package com.bluebot.injection;

import android.content.Context;

import com.bluebot.MainActivity;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by Clifton Craig on 4/13/17.
 * Copyright GE 4/13/17
 */

class MainActivityModule implements InjectionModule{
    private SharedComponents sharedComponents;

    @Override
    public void inject(Object object) {
        injectActivity((MainActivity)object);
    }

    private void injectActivity(MainActivity mainActivity) {
        mainActivity.setBluetooth(lazyGetBluetoothSPP());
    }

    private BluetoothSPP lazyGetBluetoothSPP() {
        if(! sharedComponents.has(BluetoothSPP.class))
            sharedComponents.put(BluetoothSPP.class, new BluetoothSPP(sharedComponents.get(Context.class)));
        return sharedComponents.get(BluetoothSPP.class);
    }

    @Override
    public void setSharedComponents(SharedComponents sharedComponents) {
        this.sharedComponents = sharedComponents;
    }
}
