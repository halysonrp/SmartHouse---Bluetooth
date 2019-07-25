package com.example.scanbluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DeviceItemAdpater
        extends RecyclerView.Adapter<DeviceItemAdpater.DeviceItemViewHolder> {
    private List<BluetoothDevice> mDevices;

    @NonNull
    @Override
    public DeviceItemViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int i) {
         View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_device, parent);
         return new DeviceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceItemViewHolder deviceItemViewHolder, int i) {
        deviceItemViewHolder.updateValues(getItem(i));
    }

    @Override
    public int getItemCount() {
        return mDevices != null ? mDevices.size() : 0;
    }

    public void replaceData(List<BluetoothDevice> devices) {
        mDevices = devices;
        notifyDataSetChanged();
    }

    private BluetoothDevice getItem(int position) {
        return mDevices != null ? mDevices.get(position) : null;
    }

    public class DeviceItemViewHolder
            extends  RecyclerView.ViewHolder {

        TextView mDeviceNameTextView;

        public DeviceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mDeviceNameTextView = itemView.findViewById(R.id.text_device_name);
        }

        public void updateValues(BluetoothDevice device) {
            if (device == null) return;

            mDeviceNameTextView.setText(device.getName() + ": " + device.getAddress());
        }
    }

}
