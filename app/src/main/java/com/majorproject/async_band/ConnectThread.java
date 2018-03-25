package com.majorproject.async_band;

import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInstream;
    private final OutputStream mmOutStream;

    public ConnectThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Error getting streams from socket");
        }

        mmInstream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer;
        int bytes;

        while (true) {
            try {
                bytes = mmInstream.available();
                if (bytes != 0) {
                    buffer = new byte[1024];
                    // TODO: Increase sleep time after experimentation
                    SystemClock.sleep(100);
                    bytes = mmInstream.available();
                    bytes = mmInstream.read(buffer, 0, bytes);
                    // TODO: Monitoring activity with received data
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void write(String inp) {
        byte[] bytes = inp.getBytes();
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
