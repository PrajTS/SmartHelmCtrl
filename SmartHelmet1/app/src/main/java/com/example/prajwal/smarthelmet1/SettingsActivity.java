package com.example.prajwal.smarthelmet1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseDatabase FirebaseDatabase;
    private DatabaseReference MessagesDatabaseReference;
    private ChildEventListener mChildEventListener;

    EditText phone1,phone2,phone3,phone4;

    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        FirebaseDatabase = FirebaseDatabase.getInstance();
        MessagesDatabaseReference = FirebaseDatabase.getReference();

        phone1=(EditText)findViewById(R.id.phone1);
        phone2=(EditText)findViewById(R.id.phone2);
        phone3=(EditText)findViewById(R.id.phone3);
        phone4=(EditText)findViewById(R.id.phone4);

        save = (Button)findViewById(R.id.button1);
    }
    @Override
    protected void onStart() {
        super.onStart();


        MessagesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                EmergencyContacts v = dataSnapshot.getValue(EmergencyContacts.class);
                phone1.setText(v.phone1);
                phone2.setText(v.phone2);
                phone3.setText(v.phone3);
                phone4.setText(v.phone4);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());

            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MessagesDatabaseReference.child("phone1").setValue(phone1.getText().toString());
                MessagesDatabaseReference.child("phone2").setValue(phone2.getText().toString());
                MessagesDatabaseReference.child("phone3").setValue(phone3.getText().toString());
                MessagesDatabaseReference.child("phone4").setValue(phone4.getText().toString());
                Toast.makeText(SettingsActivity.this,"Saved",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
