package com.example.viix.mpc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PetDisplay extends AppCompatActivity {
    private Button btnEdit, btnReminderD, btnDelete;
    private LinearLayout linearLayout;
    private FirebaseFirestore db;
    private String userUid,uniqueId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_display);

        Intent intent = getIntent();
        if (intent != null){
            uniqueId = intent.getStringExtra("petID");
        }
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        btnReminderD = findViewById(R.id.btnReminderD);
        linearLayout = findViewById(R.id.displayLayout);
        this.db = FirebaseFirestore.getInstance();
        this.userUid = FirebaseAuth.getInstance().getUid();
        addPetInfos();
        btnReminderD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PetDisplay.this, Reminder2.class));
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletePetWish();
            }
        });

    }
    // TODO: edit button to edit the information
    

    private void addPetInfos(){
        db.collection(this.userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String petID = document.getId();
                                if(petID.equals(uniqueId)) {
                                    String petName = (String) document.get("Name");
                                    String petType = (String) document.get("Type");
                                    String petBreed = (String) document.get("Breed");
                                    String petWeight = (String) document.get("Weight");
                                    String petAge = (String) document.get("Age");

                                    addPetInfo(petName, petType, petBreed, petWeight, petAge);
                                }

                            }
                        } else {
                            //fail
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void addPetInfo(String petName, String petType, String petBreed, String petWeight, String petAge){
        TextView petText = new TextView(this);
        TextView petText1 = new TextView(this);
        TextView petText2 = new TextView(this);
        TextView petText3 = new TextView(this);
        TextView petText4 = new TextView(this);
        petText.setText("Name: " + petName);
        petText1.setText("Type: " + petType);
        petText2.setText("Breed: " + petBreed);
        petText3.setText("Weight: " + petWeight);
        petText4.setText("Date of Birth: " + petAge);



        //TODO : pass the petID as a parameter to our activity so we can retrieve the data from the firestore
        this.linearLayout.addView(petText);
        this.linearLayout.addView(petText1);
        this.linearLayout.addView(petText2);
        this.linearLayout.addView(petText3);
        this.linearLayout.addView(petText4);
    }

    private void deletePetWish(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete Pet Profile")
                .setMessage("Are you sure you want to delete this profile?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePet();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    private void deletePet(){
        db.collection(this.userUid).document(this.uniqueId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(PetDisplay.this, Profiles.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
