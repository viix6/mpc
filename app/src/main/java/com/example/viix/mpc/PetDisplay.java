package com.example.viix.mpc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.style.ScaleXSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetDisplay.this, PetEdit.class);
                intent.putExtra("petID",uniqueId);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    protected void onStart(){
        super.onStart();
        addPetInfos();

    }

    

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
        this.linearLayout.removeAllViews();

        ImageView imgPet = new ImageView(this);
        TextView nameText = new TextView(this);
        TextView typeText = new TextView(this);
        TextView breedText = new TextView(this);
        TextView wghtText = new TextView(this);
        TextView ageText = new TextView(this);

        //TODO: display image from firebase
        ImageView myImageView = (ImageView)findViewById(R.id.imgView);
        myImageView.setImageResource(R.drawable.baseline_pets_24);

        nameText.setText("Name: " + petName);
        nameText.setTextColor(Color.parseColor("#421d5b"));
        nameText.setTextScaleX(Float.parseFloat("0.8"));
        nameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        typeText.setText("Type: " + petType);
        typeText.setTextColor(Color.parseColor("#421d5b"));
        typeText.setTextScaleX(Float.parseFloat("0.8"));
        typeText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        breedText.setText("Breed: " + petBreed);
        breedText.setTextColor(Color.parseColor("#421d5b"));
        breedText.setTextScaleX(Float.parseFloat("0.8"));
        breedText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        wghtText.setText("Weight: " + petWeight);
        wghtText.setTextColor(Color.parseColor("#421d5b"));
        wghtText.setTextScaleX(Float.parseFloat("0.8"));
        wghtText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ageText.setText("Date of Birth: " + petAge);
        ageText.setTextColor(Color.parseColor("#421d5b"));
        ageText.setTextScaleX(Float.parseFloat("0.8"));
        ageText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);



        this.linearLayout.addView(imgPet);
        this.linearLayout.addView(nameText);
        this.linearLayout.addView(typeText);
        this.linearLayout.addView(breedText);
        this.linearLayout.addView(wghtText);
        this.linearLayout.addView(ageText);
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
