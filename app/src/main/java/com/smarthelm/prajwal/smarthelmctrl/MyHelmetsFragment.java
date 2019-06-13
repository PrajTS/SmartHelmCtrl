package com.smarthelm.prajwal.smarthelmctrl;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class MyHelmetsFragment extends Fragment{

    private ArrayList<Helmets> helmets;
    private static BluetoothAdapter bTAdapter = BluetoothAdapter.getDefaultAdapter();;
    private HelmetAdapter mAdapter;

    public MyHelmetsFragment()    {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        helmets = new ArrayList<Helmets>();

//        Set<BluetoothDevice> pairedDevices = bTAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                Helmets pairedDevice= new Helmets(device.getName(),device.getAddress(),false);
//                helmets.add(pairedDevice);
//            }
//        }
//
//        if(helmets.size() == 0) {
//            helmets.add(new Helmets("No Devices", "", false));
//        }

        View rootView = inflater.inflate(R.layout.helmet_list, container, false);

        mAdapter = new HelmetAdapter(getActivity(), helmets, false);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Helmets helmet = helmets.get(position);
                //Database code to be added
            }

        });

        return rootView;
    }

}