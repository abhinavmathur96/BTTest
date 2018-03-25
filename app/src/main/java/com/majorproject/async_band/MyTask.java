package com.majorproject.async_band;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

public class MyTask extends AsyncTask<BluetoothSocket, Void, Boolean> {

    @Override
    protected Boolean doInBackground(BluetoothSocket... myDevice) {
        boolean fail = false;

        try {
            myDevice[0].connect();
        } catch (IOException e) {
            try {
                fail = true;
                myDevice[0].close();
            } catch (IOException e1) {
//                    Toast.makeText(getBaseContext(), "Connecting failed from socket", Toast.LENGTH_SHORT).show();
            }
        }

        System.out.println("Inside asynctask");
        return fail;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (!aBoolean) {
//            Toast.makeText(MainActivity.this, "Failed in connection", Toast.LENGTH_SHORT).show();
        }
    }
}