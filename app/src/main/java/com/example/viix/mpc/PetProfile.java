package com.example.viix.mpc;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import android.support.annotation.NonNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

public class PetProfile extends AppCompatActivity {
    private Button btnSave, btnCancel, btnimg;
    private EditText nameText, breedText, wghtText, ageText;
    private RadioGroup typeR;
    private FirebaseFirestore db;
    private String userUid;
    private String imageUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);
        this.db = FirebaseFirestore.getInstance();
        this.userUid = FirebaseAuth.getInstance().getUid();

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnimg = findViewById(R.id.btnimg);
        nameText = findViewById(R.id.nameText);
        breedText = findViewById(R.id.breedText);
        wghtText = findViewById(R.id.wghtText);
        ageText = findViewById(R.id.ageText);
        typeR = findViewById(R.id.typeR);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PetProfile.this, Profiles.class));
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PetProfile.this, Profiles.class));
                save();
                finish();

            }
        });

        btnimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PetProfile.this, ImageUpload.class),1);


            }
        });





;}

    private void save() {
        String name = nameText.getText().toString();
        String breed = breedText.getText().toString();
        String weight = wghtText.getText().toString();
        String age = ageText.getText().toString();

        String type = "";
        if(typeR.getCheckedRadioButtonId()!=-1){
            int id= typeR.getCheckedRadioButtonId();
            View radioButton = typeR.findViewById(id);
            int radioId = typeR.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) typeR.getChildAt(radioId);
             type = (String) btn.getText();
        }


        Map<String, Object> user = new HashMap<>();
        user.put("DataType","PetData");
        user.put("Name",name);
        user.put("Type",type);
        user.put("Breed",breed);
        user.put("Weight",weight);
        user.put("Age",age);
        user.put("url",imageUrl);
        //we need an unique ID for each pet
        // we are lazy and the ID needs to be unique only for the user, so we can use the current time as an ID
        // (a same user can't create 2 pets at the same time), p1 p2?
        Long uniqueId = System.currentTimeMillis()/1000;

        db.collection(this.userUid).document(uniqueId+"")
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                imageUrl=data.getStringExtra("url");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
