package com.larl.trackforme.model_package;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.larl.trackforme.R;

import static java.lang.Integer.valueOf;

public class ViewHolderModel extends RecyclerView.ViewHolder {

    public View view;

    public TextView titleTV , locTV , salaryTV , workdaysTV , contactTV , attendedTV , paydayTV;
    ImageButton plusBTN;

    public ViewHolderModel(@NonNull View itemView) {
        super(itemView);

        view = itemView;
    }


    public void setDataToView(Context context , String title , String loc , String salary , final String workdays , String contact){

        titleTV = view.findViewById(R.id.tv_title);
        locTV = view.findViewById(R.id.tv_loc);
        salaryTV = view.findViewById(R.id.tv_salary);
        workdaysTV = view.findViewById(R.id.tv_workdays);
        contactTV = view.findViewById(R.id.tv_contact);
        attendedTV = view.findViewById(R.id.tv_attended);
        plusBTN = view.findViewById(R.id.img_btn_plus);
        paydayTV = view.findViewById(R.id.tv_payday);

        titleTV.setText(title);
        locTV.setText(loc);
        salaryTV.setText(salary);
        workdaysTV.setText(workdays);
        contactTV.setText(contact);

        plusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paydayTV.setVisibility(View.INVISIBLE);
                Integer temp = valueOf(workdays);
                Integer attended = valueOf(attendedTV.getText().toString().trim());
                attended++;
                if(attended.equals(temp)){
                    paydayTV.setVisibility(View.VISIBLE);
                    attended = 0;
                }
                attendedTV.setText(attended.toString());
            }
        });
    }

}
