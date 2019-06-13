package com.smarthelm.prajwal.smarthelmctrl;

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
import com.google.firebase.database.ValueEventListener;
import com.smarthelm.prajwal.smarthelmctrl.MainActivity;

import static com.smarthelm.prajwal.smarthelmctrl.MainActivity.id;


public class SettingsActivity extends AppCompatActivity {

    private com.google.firebase.database.FirebaseDatabase FirebaseDatabase;
    private DatabaseReference ContactsDatabaseReference, UserDatabaseReference;

    EditText phone1,phone2,phone3,phone4, Id;
    EditText name, age, gender;

    Button save, check, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        FirebaseDatabase = FirebaseDatabase.getInstance();
        ContactsDatabaseReference = FirebaseDatabase.getReference("Users/"+id+"/emC");
        UserDatabaseReference = FirebaseDatabase.getReference("Users/"+id);

        phone1=(EditText)findViewById(R.id.phone1);
        phone2=(EditText)findViewById(R.id.phone2);
        phone3=(EditText)findViewById(R.id.phone3);
        phone4=(EditText)findViewById(R.id.phone4);

        save = (Button)findViewById(R.id.button1);
        check = (Button)findViewById(R.id.buttonCheck);
        update = (Button)findViewById(R.id.buttonUpdate);

        Id = (EditText)findViewById(R.id.editTextId);
        Id.setText(""+id);
        name = (EditText)findViewById(R.id.editTextName);
        age = (EditText)findViewById(R.id.editTextAge);
        gender = (EditText)findViewById(R.id.editTextGender);

        check();
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(""+Id.getText()).exists()){
                            id = Long.parseLong(Id.getText().toString());
                            Toast.makeText(getApplicationContext(),"Valid Id",Toast.LENGTH_SHORT).show();}
                        else
                            Toast.makeText(getApplicationContext(),"Invalid id",Toast.LENGTH_SHORT).show();
                        Id.setText(""+id);
                        check();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty())
                    UserDatabaseReference.child("Name").setValue(name.getText().toString());
                if(!age.getText().toString().isEmpty())
                    UserDatabaseReference.child("Age").setValue(age.getText().toString());
                if(!gender.getText().toString().isEmpty())
                    UserDatabaseReference.child("Gender").setValue(gender.getText().toString());
                Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
            }
        });

    }

    void check()
    {
        UserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Name").getValue().toString());
                age.setText(dataSnapshot.child("Age").getValue().toString());
                gender.setText(dataSnapshot.child("Gender").getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        ContactsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    EmergencyContacts v = dataSnapshot.getValue(EmergencyContacts.class);
                    if (v.phone1 != 0)
                        phone1.setText(String.valueOf(v.phone1));
                    if (v.phone2 != 0)
                        phone2.setText(String.valueOf(v.phone2));
                    if (v.phone3 != 0)
                        phone3.setText(String.valueOf(v.phone3));
                    if (v.phone4 != 0)
                        phone4.setText(String.valueOf(v.phone4));
                }
                catch (Exception e)
                {}
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
                if(phone1.getText().toString().trim().length()==10 || phone1.getText().toString().trim().length()==12)
                    ContactsDatabaseReference.child("phone1").setValue(Long.parseLong(phone1.getText().toString()));
                else {
                    ContactsDatabaseReference.child("phone1").removeValue();
                    phone1.setText("");
                }
                if(phone2.getText().toString().trim().length()==10 || phone2.getText().toString().trim().length()==12)
                    ContactsDatabaseReference.child("phone2").setValue(Long.parseLong(phone2.getText().toString()));
                else{phone2.setText("");
                    ContactsDatabaseReference.child("phone2").removeValue();}
                if(phone3.getText().toString().trim().length()==10 || phone3.getText().toString().trim().length()==12)
                    ContactsDatabaseReference.child("phone3").setValue(Long.parseLong(phone3.getText().toString()));
                else{phone3.setText("");
                    ContactsDatabaseReference.child("phone3").removeValue();}
                if(phone4.getText().toString().trim().length()==10 || phone4.getText().toString().trim().length()==12)
                    ContactsDatabaseReference.child("phone4").setValue(Long.parseLong(phone4.getText().toString()));
                else{phone4.setText("");
                    ContactsDatabaseReference.child("phone4").removeValue();}
                Toast.makeText(SettingsActivity.this,"Saved",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
