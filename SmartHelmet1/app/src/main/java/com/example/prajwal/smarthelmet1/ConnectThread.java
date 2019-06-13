package com.example.prajwal.smarthelmet1;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.UUID;

class ConnectThread extends Thread {
    private BluetoothSocket mmSocket;

    public ConnectThread(BluetoothDevice device, UUID uuid) {

    }
}