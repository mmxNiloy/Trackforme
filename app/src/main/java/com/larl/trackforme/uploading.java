package com.larl.trackforme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import com.larl.trackforme.model_package.Upload_Model;

public class uploading extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String UID;

    EditText ET_Tuition_Name , ET_Location , ET_Contact , ET_Salary , ET_Workdays;
    Button BTN_Create;
    Upload_Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading);

        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("tracks").child(UID);

        Intent intentReceiver = getIntent();
        String pass = intentReceiver.getStringExtra("pass");

        // Init
        ET_Tuition_Name = findViewById(R.id.et_tuition_name);
        ET_Location = findViewById(R.id.et_location);
        ET_Contact = findViewById(R.id.et_contact_number);
        ET_Salary = findViewById(R.id.et_salary);
        ET_Workdays = findViewById(R.id.et_workday_number);
        BTN_Create = findViewById(R.id.btn_create_record);

        BTN_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                model = new Upload_Model(ET_Tuition_Name , ET_Location , ET_Contact , ET_Salary , ET_Workdays);

                // Uploading
                // if valid
                if(model.isValid()){
                    model.uploadDataToFireBase(databaseReference);

                    // AlertBox on success
                    AlertDialog alertDialog = new AlertDialog.Builder(uploading.this).create();
                    alertDialog.setTitle("Success!");
                    alertDialog.setIcon(R.drawable.task_complete);
                    alertDialog.setMessage("Successfully uploaded your data.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Done",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext() , data_activity.class);
                                    uploading.this.startActivity(intent);
                                    finish();
                                }
                            });
                    alertDialog.show();
                }

                else{
                    // AlertBox on failure
                    AlertDialog alertDialog = new AlertDialog.Builder(uploading.this).create();
                    alertDialog.setTitle("Oops!");
                    alertDialog.setIcon(R.drawable.close_icon);
                    alertDialog.setMessage("Failed to upload data. Please recheck your entries.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }
}
