package com.example.prajwal.smarthelmet1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;

import data.HelmDBHelper;

import static com.example.prajwal.smarthelmet1.R.color.accident;

public class MainActivity extends AppCompatActivity{// implements DeviceListFragment.OnFragmentInteractionListener {

   // private DeviceListFragment mDeviceListFragment;
    private BluetoothAdapter BTAdapter;

    public static int REQUEST_BLUETOOTH = 1;

    private HelmDBHelper mDbHelper;

    boolean accident;
    boolean bluetooth;

    LinearLayout root;
    TextView system_stat;
    TextView accident_stats;
    TextView accident_label;
    Button save;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    //private ChildEventListener mChildEventListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BTAdapter = BluetoothAdapter.getDefaultAdapter();


        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }


        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
        //FragmentManager fragmentManager = getSupportFragmentManager();

        //mDeviceListFragment = DeviceListFragment.newInstance(BTAdapter);
        //fragmentManager.beginTransaction().replace(R.id.container, mDeviceListFragment).commit();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference();

        root = (LinearLayout) findViewById(R.id.root_view);
        system_stat=(TextView)findViewById(R.id.system_status);
        accident_stats=(TextView)findViewById(R.id.sos);
        accident_label=(TextView)findViewById(R.id.accident_detect);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton helm = (FloatingActionButton) findViewById(R.id.helm);
        helm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectHelmActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new HelmDBHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mMessagesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Values v = dataSnapshot.getValue(Values.class);

                accident=v.accident;
                Log.e("accident"," "+accident);

                if(v.status)
                {
                    system_stat.setText("ACTIVATED");
                }
                else
                {
                    system_stat.setText("DEACTIVATED");
                }

                if(v.accident)
                {
                    root.setBackgroundColor(getResources().getColor(R.color.accident));
                    accident_label.setVisibility(View.VISIBLE);
                    accident_stats.setText("Cancel Alert");
                }
                else
                {
                    root.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    accident_label.setVisibility(View.INVISIBLE);
                    accident_stats.setText("Send SOS");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());

            }
        });

        accident_stats.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(accident) {
                    Toast.makeText(MainActivity.this, "Alert Cancelled", Toast.LENGTH_SHORT).show();
                    accident = false;

                    //accident_stats.setText("Send SOS");
                }
                else
                {
                    Toast.makeText(MainActivity.this,"SOS sent",Toast.LENGTH_SHORT).show();
                    accident= true;
                }
                mMessagesDatabaseReference.child("accident").setValue(accident);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
