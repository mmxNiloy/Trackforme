package com.larl.trackforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.larl.trackforme.model_package.Download_Model;
import com.larl.trackforme.model_package.ViewHolderModel;

public class data_activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference databaseReference;
    String UID;

    FirebaseRecyclerAdapter <Download_Model , ViewHolderModel> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions <Download_Model> firebaseRecyclerOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_activity);
        getSupportActionBar().setTitle("Track List");

        fab = findViewById(R.id.floating_action_button);
        recyclerView = findViewById(R.id.id_recycler_view);
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("tracks").child(UID);

        // Init linear layout manager
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(false);
        linearLayoutManager.setReverseLayout(false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , uploading.class);
                intent.putExtra("login", "success");
                data_activity.this.startActivity(intent);

            }
        });
        
        loadDataFromFireBase();

    }

    private void loadDataFromFireBase() {

        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Download_Model>().setQuery(databaseReference.child("data") , Download_Model.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Download_Model, ViewHolderModel>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderModel viewHolderModel, int position, @NonNull Download_Model Download_Model) {

                viewHolderModel.setDataToView(getApplicationContext() , Download_Model.getName() , Download_Model.getLoc() ,
                        Download_Model.getSalary() , Download_Model.getWorkday() , Download_Model.getContact());
            }

            @NonNull
            @Override
            public ViewHolderModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_row,parent,false);

                final ViewHolderModel viewHolderModel= new ViewHolderModel(view);

                return viewHolderModel;
            }
        };

        recyclerView.setLayoutManager(linearLayoutManager);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.res_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_log_out:
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                intent.putExtra("login", "success");
                data_activity.this.startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
