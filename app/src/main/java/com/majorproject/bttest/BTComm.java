package com.majorproject.bttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class BTComm extends AppCompatActivity {

    EditText sendText;
    Button send;
    private ConnectedThread mConnectedThread;
    static Handler mHandler;
    BluetoothSocket mBTSocket;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int CONNECTING_STATUS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btcomm);

        sendText = findViewById(R.id.sendText);
        send = findViewById(R.id.send);

        final BluetoothAdapter mBTAdapter= BluetoothAdapter.getDefaultAdapter();

        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == CONNECTING_STATUS){
                    if(msg.arg1 == 1)
                        makeToast("Connected to Device: " + (msg.obj), "s");
                    else
                        makeToast("Connection Failed", "l");
                }
            }
        };

        if (mBTAdapter == null) {
            makeToast("BT not found", "l");
        } else {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeToast("Button clicked", "s");
                }
            });

            if (!mBTAdapter.isEnabled()) {
                bluetoothOn();
            }

            for (final BluetoothDevice device: mBTAdapter.getBondedDevices()) {
                if (device.getName().equalsIgnoreCase("hc-05")) {
                    makeToast("Found HC-05. Connecting...", "l");
                    final String address = device.getAddress().substring(
                            device.getAddress().length() - 17
                    );

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            boolean fail = false;
                            BluetoothDevice dev = mBTAdapter.getRemoteDevice(address);

                            // try to create secure connection object
                            try {
                                mBTSocket = createBluetoothSocket(dev);
                            } catch (IOException e) {
                                fail = true;
                                makeToast("Socket creation failed", "s");
                            }

                            // try transfer of data through secure connection
                            try {
                                mBTSocket.connect();
                            } catch (IOException e) {
                                try {
                                    fail = true;
                                    mBTSocket.close();
                                    mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                            .sendToTarget();
                                } catch (IOException e2) {
                                    makeToast("Socket creation failed", "s");
                                }
                            }

                            if(!fail) {
                                // pass to connection thread
                                mConnectedThread = new ConnectedThread(mBTSocket, mHandler);
                                mConnectedThread.start();

                                mHandler.obtainMessage(
                                        CONNECTING_STATUS,
                                        1,
                                        -1,
                                        device.getName()
                                ).sendToTarget();
                            }
                        }
                    }.start();
                    break;
                } else {
                    makeToast("Could not find wearable in paired devices", "l");
                }
            }
        }
    }

    public void makeToast(String s, String l) {
        if (l.equalsIgnoreCase("l")) {
            Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
        } else if (l.equalsIgnoreCase("s")) {
            Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    public void bluetoothOn() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        makeToast("Bluetooth turned on", "s");
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }
}
