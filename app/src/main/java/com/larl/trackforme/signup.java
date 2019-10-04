package com.larl.trackforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import com.larl.trackforme.model_package.signup_model;

public class signup extends AppCompatActivity {

    TextView TV_Warning;
    ImageButton Go_Back;
    ImageView IV_Show_Pass , IV_Show_Pass_Repeat , IV_Warning_Email , IV_Warning_Pass , IV_Warning_Repeat_Pass;
    EditText ET_Email , ET_Password , ET_Password_Repeat , ET_Name;
    Button BTN_Sign_Up;
    signup_model model;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intentReceiver = getIntent();
        String pass = intentReceiver.getStringExtra("pass");

        Go_Back = findViewById(R.id.ib_go_back);
        ET_Email = findViewById(R.id.et_sign_up_email);
        ET_Password = findViewById(R.id.et_sign_up_pass);
        ET_Password_Repeat = findViewById(R.id.et_sign_up_repeat_pass);
        ET_Name = findViewById(R.id.et_sign_up_name);
        BTN_Sign_Up = findViewById(R.id.id_btn_sign_up);
        IV_Show_Pass = findViewById(R.id.iv_show_pass);
        IV_Show_Pass_Repeat = findViewById(R.id.iv_show_pass_repeat);
        TV_Warning = findViewById(R.id.tv_warning_text);
        IV_Warning_Email = findViewById(R.id.iv_warning_email);
        IV_Warning_Pass = findViewById(R.id.iv_warning_password);
        IV_Warning_Repeat_Pass = findViewById(R.id.iv_warning_password_repeat);
        progressBar = findViewById(R.id.pb_signup_page);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("userinfo").child("users");


        IV_Show_Pass.setImageResource(R.drawable.open_eye);
        IV_Show_Pass_Repeat.setImageResource(R.drawable.open_eye);

        Go_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentObj = new Intent(getApplicationContext() , MainActivity.class);
                intentObj.putExtra("pass" , "signup");
                signup.this.startActivity(intentObj);
                finish();
            }
        });


        // Password eye section
        IV_Show_Pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IV_Show_Pass.getContentDescription().toString().equals("show")) {
                    IV_Show_Pass.setContentDescription("hide");
                    IV_Show_Pass.setImageResource(R.drawable.closed_eye);
                    ET_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else{
                    IV_Show_Pass.setContentDescription("show");
                    IV_Show_Pass.setImageResource(R.drawable.open_eye);
                    ET_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        IV_Show_Pass_Repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IV_Show_Pass_Repeat.getContentDescription().toString().equals("show")) {
                    IV_Show_Pass_Repeat.setContentDescription("hide");
                    IV_Show_Pass_Repeat.setImageResource(R.drawable.closed_eye);
                    ET_Password_Repeat.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else{
                    IV_Show_Pass_Repeat.setContentDescription("show");
                    IV_Show_Pass_Repeat.setImageResource(R.drawable.open_eye);
                    ET_Password_Repeat.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        BTN_Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TV_Warning.setVisibility(View.INVISIBLE);
                IV_Warning_Pass.setVisibility(View.INVISIBLE);
                IV_Warning_Email.setVisibility(View.INVISIBLE);
                IV_Warning_Repeat_Pass.setVisibility(View.INVISIBLE);

                model = new signup_model(ET_Name , ET_Email , ET_Password , ET_Password_Repeat);

                signupVerification(model);
            }
        });

    }

    private void signupVerification(final signup_model model) {
        if(TextUtils.isEmpty(model.getEmail()))
        {
            TV_Warning.setVisibility(View.VISIBLE);
            TV_Warning.setText("Enter a valid email address.");
            IV_Warning_Email.setVisibility(View.VISIBLE);
        }
        else if(TextUtils.isEmpty(model.getPassword()) || model.getPassword().length() < 8){
            TV_Warning.setVisibility(View.VISIBLE);
            TV_Warning.setText("This password is too short.");
            IV_Warning_Pass.setVisibility(View.VISIBLE);
        }
        else if(!model.getPassword().equals(model.getRepeatPassword())){
            TV_Warning.setVisibility(View.VISIBLE);
            TV_Warning.setText("The password did not match.");
            IV_Warning_Repeat_Pass.setVisibility(View.VISIBLE);
        }
        else{
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(model.getEmail() , model.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()) {
                       // Successful sign up

                       progressBar.setVisibility(View.GONE);

                       // AlertBox on success
                       AlertDialog alertDialog = new AlertDialog.Builder(signup.this).create();
                       alertDialog.setTitle("Success!");
                       alertDialog.setIcon(R.drawable.task_complete);
                       alertDialog.setMessage("Welcome " + model.getName() + ", thank you for registering.");
                       alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                               new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int which) {
                                       dialog.dismiss();

                                       // Activity navigation
                                       Intent intentObj = new Intent(getApplicationContext(), data_activity.class);
                                       intentObj.putExtra("username", model.getName());
                                       signup.this.startActivity(intentObj);
                                       finish();
                                   }
                               });
                       alertDialog.show();

                       HashMap map = new HashMap();
                       map.put("username" , model.getName());
                       map.put("password" , model.getPassword());
                       map.put("email" , model.getEmail());

                       UID = mAuth.getUid();

                       databaseReference.child(UID).child("profile_info").setValue(map);



                   } else {
                       // Failed sign up

                       progressBar.setVisibility(View.GONE);
                       String exc = task.getException().toString() + " ";


                       TV_Warning.setVisibility(View.VISIBLE);
                       TV_Warning.setText(exc);
                       IV_Warning_Pass.setVisibility(View.VISIBLE);
                       IV_Warning_Email.setVisibility(View.VISIBLE);
                   }

               }
           }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);

                    TV_Warning.setVisibility(View.VISIBLE);
                    TV_Warning.setText(e.getMessage());
                    IV_Warning_Pass.setVisibility(View.VISIBLE);
                    IV_Warning_Email.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
