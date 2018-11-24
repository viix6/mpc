package com.example.viix.mpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;

public class Profiles extends AppCompatActivity {
    private Button btnAdd;
    private LinearLayout linearLayout;
    private FirebaseFirestore db;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        btnAdd = findViewById(R.id.btnAdd);
        linearLayout = findViewById(R.id.profileLayout);
        this.db = FirebaseFirestore.getInstance();
        this.userUid = FirebaseAuth.getInstance().getUid();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profiles.this, PetProfile.class));
            }
        });


    }
    @Override
    protected void onStart(){
        super.onStart();
        addPetButtons();
    }



    private void addPetButtons(){
        this.linearLayout.removeAllViews();
        db.collection(this.userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if("PetData".equals((String)document.get("DataType"))) {
                                    String petName = (String) document.get("Name");
                                    String petID = document.getId();
                                    addPetButton(petName, petID);
                                }

                            }
                        } else {
                            //fail
                        }
                    }
                });
    }

    private void addPetButton(String petName, final String petID){
        Button petButton = new Button(this);
        petButton.setText(petName);
        //add a listener to the button to start the activity where the pet will be display
        petButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profiles.this, PetDisplay.class);
                intent.putExtra("petID",petID);
                startActivity(intent);

            }
        });

        this.linearLayout.addView(petButton);





    }


}
