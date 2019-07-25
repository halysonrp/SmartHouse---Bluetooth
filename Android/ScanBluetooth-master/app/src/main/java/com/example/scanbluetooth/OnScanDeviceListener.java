package com.example.scanbluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.List;

public interface OnScanDeviceListener {
    void onStart();
    void onCompleted(List<BluetoothDevice> devices);
}

