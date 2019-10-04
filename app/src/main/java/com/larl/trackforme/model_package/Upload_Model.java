package com.larl.trackforme.model_package;

import android.text.TextUtils;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class Upload_Model {
    private String name , loc , contact , salary , workday;
    private HashMap map = new HashMap();

    public Upload_Model(EditText Name , EditText Loc , EditText Contact , EditText Salary , EditText Workday){
        name = Name.getText().toString().trim();
        loc = Loc.getText().toString().trim();
        contact = Contact.getText().toString();
        salary = Salary.getText().toString();
        workday = Workday.getText().toString();

        // Storing the data to a hash map so that we can upload the data to a RTDB.
        // RTDB stores data in a <key char sequence , value char sequence> format.
        // Thus the procedure of mapping.
        map.put("name" , name);
        map.put("loc" , loc);
        map.put("contact" , contact);
        map.put("salary" , salary);
        map.put("workday" , workday);
    }

    public boolean isValid(){
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(loc) || TextUtils.isEmpty(contact)
                || TextUtils.isEmpty(salary) || TextUtils.isEmpty(workday) || Integer.valueOf(workday) > 30 || contact.length() > 15)
            return false;
        else
            return true;
    }

    public void uploadDataToFireBase(DatabaseReference databaseReference){
        String pushID = databaseReference.push().getKey();
        assert pushID != null;
        databaseReference.child("data").child(pushID).setValue(map);
    }
}
