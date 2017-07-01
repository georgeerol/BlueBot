package com.bluebot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bluebot.droid.ide.BlueBotIDEActivity;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static com.bluebot.runtime.bluetooth.BluetoothCodes.AUTONOMOUS;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.BACKWARD;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.FORWARD;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.LEFT;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.LEFTBACK;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.LOOKLEFT;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.LOOKRIGHT;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.LOOKSTRAIGHT;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.RIGHT;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.RIGHTBACK;
import static com.bluebot.runtime.bluetooth.BluetoothCodes.STOP;

/**
 * Created by George Fouche on 4/5/2017.
 */
public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private boolean forward = false;
    private boolean backward = false;
    private boolean left = false;
    private boolean right = false;
    private boolean stop = false;

    //Servo
    private boolean lookRight = false;
    private boolean lookLeft = false;
    private boolean lookStraight = false;


    private BluetoothControl bluetooth;
    private Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect = (Button) findViewById(R.id.connect);

        findViewById(R.id.ideButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), BlueBotIDEActivity.class);
                startActivity(intent);
            }
        });
        setConnectListener();
        setButtonsOnTouchListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                connect.setText(R.string.connecting);
                connect.setEnabled(false);
                bluetooth.connect(data);
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getApplicationContext(), "Bluetooth was not enabled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setButtonsOnTouchListener() {
        findViewById(R.id.forward).setOnTouchListener(this);
        findViewById(R.id.backward).setOnTouchListener(this);
        findViewById(R.id.left).setOnTouchListener(this);
        findViewById(R.id.right).setOnTouchListener(this);
        findViewById(R.id.autonomous).setOnTouchListener(this);
        //Servo
        findViewById(R.id.sensroRight).setOnTouchListener(this);
        findViewById(R.id.sensorLeft).setOnTouchListener(this);
        findViewById(R.id.sensorCenter).setOnTouchListener(this);

    }

    private void setConnectListener() {
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetooth.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }

    private void setBluetoothListener() {
        bluetooth.setBluetoothConnectionListener(new BluetoothControllerConnectionListener() {
            @Override
            public void onDeviceConnected(String name, String address) {
                connect.setEnabled(true);
                connect.setText(getString(R.string.connected_to, name));
            }

            @Override
            public void onDeviceDisconnected() {
                connect.setEnabled(true);
                connect.setText(R.string.connection_lost);
            }

            @Override
            public void onDeviceConnectionFailed() {
                connect.setEnabled(true);
                connect.setText(R.string.unable_to_connect);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.forward:
                if (event.getAction() == MotionEvent.ACTION_DOWN) forward = true;
                else if (event.getAction() == MotionEvent.ACTION_UP) forward = false;
                break;
            case R.id.backward:
                if (event.getAction() == MotionEvent.ACTION_DOWN) backward = true;
                else if (event.getAction() == MotionEvent.ACTION_UP) backward = false;
                break;
            case R.id.left:
                if (event.getAction() == MotionEvent.ACTION_DOWN) left = true;
                else if (event.getAction() == MotionEvent.ACTION_UP) left = false;
                break;
            case R.id.right:
                if (event.getAction() == MotionEvent.ACTION_DOWN) right = true;
                else if (event.getAction() == MotionEvent.ACTION_UP) right = false;
                break;
            case R.id.sensroRight:
                if (event.getAction() == MotionEvent.ACTION_DOWN) lookRight = true;
                else if (event.getAction() == MotionEvent.ACTION_UP) lookRight = false;
                break;
            case R.id.sensorLeft:
                if (event.getAction() == MotionEvent.ACTION_DOWN) lookLeft = true;
                else if (event.getAction() == MotionEvent.ACTION_UP) lookLeft = false;
                break;
            case R.id.sensorCenter:
                if (event.getAction() == MotionEvent.ACTION_DOWN)lookStraight = true;
                else if (event.getAction() == MotionEvent.ACTION_UP) lookStraight = false;
                break;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP)
            command();

        return false;
    }

    private void command() {
        if (stop) {
            send(STOP);
        } else if (forward) {
            if (left && !right) send(LEFT);
            else if (right) send(RIGHT);
            else if (!backward) send(FORWARD);
            else send(STOP);
        } else if (backward) {
            if (left && !right) send(LEFTBACK);
            else if (right) send(RIGHTBACK);
            else if (!forward) send(BACKWARD);
            else send(STOP);
        } else if (left && !right) send(LEFT);
        else if (right) send(RIGHT);
        else if (lookRight) send(LOOKRIGHT);
        else if (lookLeft) send(LOOKLEFT);
        else if (lookStraight)send(LOOKSTRAIGHT);
        else send(STOP);

    }

    private void send(String command) {
        bluetooth.send(command, true);
    }

    public void setBluetooth(BluetoothControl bluetooth) {
        this.bluetooth = bluetooth;
        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
        }
        setBluetoothListener();
    }
}
