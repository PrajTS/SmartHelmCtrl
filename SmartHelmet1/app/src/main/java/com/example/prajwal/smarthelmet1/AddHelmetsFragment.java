package com.example.prajwal.smarthelmet1;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.UUID;

public class AddHelmetsFragment extends Fragment {

    private ArrayList<Helmets> helmets;
    private final BluetoothAdapter mBtAdapter;
    private HelmetAdapter mAdapter;

    private ConnectThread mConnectThread;
    private BluetoothDevice mDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    public AddHelmetsFragment()
    {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            Log.e("das","got");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Helmets activeDevice= new Helmets(device.getName(),device.getAddress(),false);
                helmets.add(activeDevice);
            }
        }
    };


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("a","BA");
        helmets = new ArrayList<Helmets>();
        Log.e("a","BAdefault");
        View rootView = inflater.inflate(R.layout.helmet_list, container, false);

        mAdapter = new HelmetAdapter(getActivity(), helmets, true);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Helmets helmet = helmets.get(position);
                //Database code to be added
            }

        });
        helmets.add(new Helmets("yappa","",true));

        /*filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);*/

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(this.mReceiver, filter);
        mBtAdapter.startDiscovery();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }
}
