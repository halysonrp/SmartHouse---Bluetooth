package com.example.scanbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BluetoothController mBluetoothController;

    private ProgressBar mLoading;
    private RecyclerView mListDevices;
    private Button btnLigaDesliga;

    private DeviceItemAdpater mDeviceItemAdpater;

    private Boolean statesLight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoading = findViewById(R.id.progressBar);
        mListDevices = findViewById(R.id.list_devices);
        btnLigaDesliga = findViewById(R.id.btn_Liga_Desliga);
        initializeListDevices();

        findViewById(R.id.btn_scan_devices).setOnClickListener((view) -> {
            //String enderecoMac = "20:16:12:05:70:36";
            String enderecoMac = "00:21:13:04:A8:6B";
            mBluetoothController.scanDevices(enderecoMac,this, new OnScanDeviceListener() {
                @Override
                public void onStart() {
                    showLoading();
                }

                @Override
                public void onCompleted(List<BluetoothDevice> devices) {
                    hideLoading();
                    if(devices != null && devices.size() > 0){
                        btnLigaDesliga.setEnabled(true);
                        Toast.makeText(MainActivity.this, "Conectou com device"+devices.get(0).getName()+"Endress Mac"+devices.get(0).getAddress(),Toast.LENGTH_LONG).show();
                        mBluetoothController.sendMsg(CommunicationArduinoEnum.STATUS.getCodeStates());
                        verifyMsg();
                    }

//                    showDevices(devices);
                }
            });
        });

        mBluetoothController = new BluetoothController();
        if (!mBluetoothController.isBluetoothEnabled()) {
            requestBluetooth();
        }
    }

    public void ligaDesliga(View view){

        if(statesLight){
            btnLigaDesliga.setText("Ligar");
            statesLight = Boolean.FALSE;
            mBluetoothController.sendMsg(CommunicationArduinoEnum.DESLIGAR.getCodeStates());
        }else {
            btnLigaDesliga.setText("Desligar");
            statesLight = Boolean.TRUE;
            mBluetoothController.sendMsg(CommunicationArduinoEnum.LIGAR.getCodeStates());
        }

    }

    public void verifyMsg() {
        Handler uiHandler = new Handler();
        new Thread(() -> {
            try {
                while (true) {
                    if (mBluetoothController.socket == null) continue;
                        InputStream bluetoothIn = mBluetoothController.socket.getInputStream();
                        byte[] buffer = new byte[256];

                        try {
                            int bytesAvailable = bluetoothIn.available();
                            if (bytesAvailable > 0) {
                                bluetoothIn.read(buffer, 0, bytesAvailable);
                                String result = new String(buffer);

                                Log.d("TESTEe", result);
                                uiHandler.post(() -> {
                                    checkStatus(result.substring(0, bytesAvailable));


                                });
                            }
                        } catch (IOException e) { /* IO error */ }

                }
            } catch (Exception e) {

            }

        }).start();
    }

    public void checkStatus(String valor){
        switch(valor) {
            case "0":
                statesLight = false;
                btnLigaDesliga.setText("Ligar");
                break;
            case "1":
               statesLight = true;
                btnLigaDesliga.setText("Desligar");
                break;
            default:
                break;
        }
    }





    private void initializeListDevices() {
        mListDevices.setLayoutManager(new LinearLayoutManager(this));
        mDeviceItemAdpater = new DeviceItemAdpater();
        mListDevices.setAdapter(mDeviceItemAdpater);
    }

    private void showDevices(List<BluetoothDevice> devices) {
        mDeviceItemAdpater.replaceData(devices);
    }

    private void hideLoading() {
        mLoading.setVisibility(View.GONE);
    }

    private void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    private void showWarningNoBluetooth() {
        Toast.makeText(
                this,
                "O bluetooth deve está ligado!",
                Toast.LENGTH_LONG
        ).show();
    }

    private void requestBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        this.startActivityForResult(intent, 1000);
    }

    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {
            if (!mBluetoothController.isBluetoothEnabled()) showWarningNoBluetooth();
        }
    }






}
