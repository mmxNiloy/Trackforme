package com.larl.trackforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    EditText ET_Email , ET_Pass;
    ImageView IV_Show_Pass , IV_Warning_Email , IV_Warning_Pass;
    Button BTN_Login , BTN_Signup;
    TextView TV_Warning;
    String email , password;

    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intentReceiver = getIntent();
        String pass = intentReceiver.getStringExtra("pass");

        ET_Email = findViewById(R.id.et_log_in_email);
        ET_Pass = findViewById(R.id.ev_log_in_pass);
        IV_Show_Pass = findViewById(R.id.iv_show_pass);
        IV_Warning_Email = findViewById(R.id.iv_warning_email);
        IV_Warning_Pass = findViewById(R.id.iv_warning_password);
        BTN_Login = findViewById(R.id.id_btn_log_in);
        TV_Warning = findViewById(R.id.tv_warning_text);
        BTN_Signup = findViewById(R.id.id_btn_sign_up);
        progressBar = findViewById(R.id.pb_login_page);

        //Init Firebase auth
        mAuth = FirebaseAuth.getInstance();


        IV_Show_Pass.setImageResource(R.drawable.open_eye);
        ET_Pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        ET_Pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        IV_Show_Pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IV_Show_Pass.getContentDescription().toString().equals("show")) {
                    IV_Show_Pass.setContentDescription("hide");
                    IV_Show_Pass.setImageResource(R.drawable.closed_eye);
                    ET_Pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else{
                    IV_Show_Pass.setContentDescription("show");
                    IV_Show_Pass.setImageResource(R.drawable.open_eye);
                    ET_Pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        BTN_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TV_Warning.setVisibility(View.INVISIBLE);
                IV_Warning_Pass.setVisibility(View.INVISIBLE);
                IV_Warning_Email.setVisibility(View.INVISIBLE);

                // Requesting the Google server for authentication by calling in a user defined method
                email = ET_Email.getText().toString().trim();
                password = ET_Pass.getText().toString().trim();

                signinVerification(email , password);
            }
        });

        BTN_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentObj = new Intent(getApplicationContext() , signup.class);
                intentObj.putExtra("pass" , "login");
                MainActivity.this.startActivity(intentObj);
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user;

        user = mAuth.getCurrentUser();

        if(user != null){
            // This ensures that a session or a user is already logged in

            Intent intentObj = new Intent(getApplicationContext() , data_activity.class);
            intentObj.putExtra("login" , "success");
            MainActivity.this.startActivity(intentObj);
            finish();

        }
    }

    private void signinVerification(String email, String password){
        if(TextUtils.isEmpty(email))
        {
            TV_Warning.setVisibility(View.VISIBLE);
            TV_Warning.setText("Enter a valid email address.");
            IV_Warning_Email.setVisibility(View.VISIBLE);
        }

        else if(TextUtils.isEmpty(password) || password.length() < 8){
            TV_Warning.setVisibility(View.VISIBLE);
            TV_Warning.setText("This password is too short.");
            IV_Warning_Pass.setVisibility(View.VISIBLE);
        }

        else {

            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        // Successful log-in


                        progressBar.setVisibility(View.GONE);
                        Intent intentObj = new Intent(getApplicationContext(), data_activity.class);
                        intentObj.putExtra("login", "success");
                        MainActivity.this.startActivity(intentObj);
                        finish();

                    } else {
                        // Failed log-in

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

