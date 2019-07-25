package com.example.scanbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothController {
    private BluetoothAdapter mBluetoothAdapter;

    private List<BluetoothDevice> mFoundDevices;

    public BluetoothSocket socket;

    public BluetoothController() {
        initializeBluetoothAdapter();
    }

    public boolean isBluetoothEnabled() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    private void initializeBluetoothAdapter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void scanDevices(String deviceMac, Context context, OnScanDeviceListener listener) {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    mFoundDevices = new ArrayList<>();
                    listener.onStart();

                } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    //mFoundDevices.add(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (device.getAddress().equals(deviceMac)) {
                        connectDevice(deviceMac);
                    }
                    mFoundDevices.add(device);

                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    context.unregisterReceiver(this);
                    listener.onCompleted(mFoundDevices);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(receiver, filter);

        mBluetoothAdapter.startDiscovery();
    }

    private static final UUID APP_UUID = UUID.fromString(
            "00001101-0000-1000-8000-00805F9B34FB"
    );

    private void connectDevice(String deviceAddress) {
        try {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(deviceAddress);
            socket = device.createInsecureRfcommSocketToServiceRecord(APP_UUID);

            socket.connect();
            Log.d("StatusConnect", "Status Connect:" + socket.isConnected());
            //sendMsg(socket, "Envio de texto para o Arduino");

        } catch (Exception e) {
            Log.d("ASDF", "Ocorreu um erro ao processar essa acao" + e);
        }
    }

    public void sendMsg(String message) {
        try {
            //socket.getOutputStream().write(message.getBytes());
            socket.getOutputStream().write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
