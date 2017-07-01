package com.bluebot.injection;

import android.content.Context;

import com.bluebot.BluetoothControl;
import com.bluebot.MainActivity;
import com.bluebot.wrappers.BluetoothControlWrapper;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by Clifton Craig on 4/13/17.
 */

class MainActivityModule implements InjectionModule{
    private SharedComponents sharedComponents;

    @Override
    public void inject(Object object) {
        injectActivity((MainActivity)object);
    }

    private void injectActivity(MainActivity mainActivity) {
        mainActivity.setBluetooth(lazyGetBluetoothControl());
    }

    private BluetoothControl lazyGetBluetoothControl() {
        if(sharedComponents.doesNotHave(BluetoothSPP.class)) {
            final BluetoothSPP bluetoothSPP = new BluetoothSPP(sharedComponents.get(Context.class));
            sharedComponents.put(BluetoothControl.class, new BluetoothControlWrapper(bluetoothSPP));
        }
        return sharedComponents.get(BluetoothControl.class);
    }

    @Override
    public void setSharedComponents(SharedComponents sharedComponents) {
        this.sharedComponents = sharedComponents;
    }
}
