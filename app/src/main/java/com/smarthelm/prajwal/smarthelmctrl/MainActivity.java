package com.smarthelm.prajwal.smarthelmctrl;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import data.HelmDBHelper;

public class MainActivity extends AppCompatActivity {

    // private DeviceListFragment mDeviceListFragment;
    private BluetoothAdapter BTAdapter;

    public static int REQUEST_BLUETOOTH = 1;

    private HelmDBHelper mDbHelper;

    boolean accident;
    LinearLayout root;
    TextView system_stat;
    TextView accident_stats;
    TextView accident_label;
    static long id=7892530771L;
    String reportStatus;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference("Users/"+id);

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

        helm.setVisibility(View.INVISIBLE);

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
                if(accident)
                    accident_stats.setVisibility(View.VISIBLE);
                else
                    accident_stats.setVisibility(View.INVISIBLE);

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
                    final DatabaseReference a = mFirebaseDatabase.getReference().child("adetected").child(""+id);
                    accident_stats.setVisibility(View.INVISIBLE);
                    a.child("reportStatus").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            reportStatus = dataSnapshot.getValue().toString();
                            Log.e("ghf",dataSnapshot.getValue().toString());
                            if(reportStatus.equals("Accident Detected")) {
                                a.child("report").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final long num = dataSnapshot.getChildrenCount();
                                        Date date = new Date();
                                        String [] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
                                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
                                        final String today = dateFormatter.format(date);
                                        final String now = timeFormatter.format(date);
                                        /*String today = date.getDate()+" "+months[date.getMonth()]+", "+ date.ge();
                                        String now = date.getHours()+" : "+date.getMinutes()+" : "+date.getSeconds();*/
                                        final String remarks = "Cancelled by User";
                                        final String value = "False Alarm";
                                        a.child("report").child(""+(num+1)).setValue(new DbModel(today,remarks, now, value)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                a.child("reportStatus").setValue("Cancelled").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mMessagesDatabaseReference.child("sendNotification").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                mMessagesDatabaseReference.child("accident").setValue(accident).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        a.child("falseAlarm").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });


                                                    }
                                                });
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            else {
                                mMessagesDatabaseReference.child("sendNotification").setValue(false);
                                final long num = dataSnapshot.getChildrenCount();
                                Date date = new Date();
                                String [] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
                                final String today = dateFormatter.format(date);
                                final String now = timeFormatter.format(date);
                                        /*String today = date.getDate()+" "+months[date.getMonth()]+", "+ date.ge();
                                        String now = date.getHours()+" : "+date.getMinutes()+" : "+date.getSeconds();*/
                                final String remarks = "Updates stopped by the user";
                                final String value = "Cancelled by User";
                                a.child("report").child(""+(num+1)).setValue(new DbModel(today,remarks, now, value));
                                a.child("reportStatus").setValue("Cancelled");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //accident_stats.setText("Send SOS");
                }
                else
                {
                    Toast.makeText(MainActivity.this,"SOS sent",Toast.LENGTH_SHORT).show();
                    accident= true;
                    mMessagesDatabaseReference.child("accident").setValue(accident);
                }

            }
        });
    }



}
