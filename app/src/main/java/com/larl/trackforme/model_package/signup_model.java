package com.larl.trackforme.model_package;

import android.widget.EditText;


public class signup_model {
    private String email , password , repeatPassword , name;

    public signup_model(EditText na , EditText em , EditText pa , EditText rpa){
        name = na.getText().toString();
        email = em.getText().toString();
        password = pa.getText().toString();
        repeatPassword = rpa.getText().toString();
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getRepeatPassword(){
        return repeatPassword;
    }

    public String getName(){
        return name;
    }


}
