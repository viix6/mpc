package com.example.viix.mpc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class PetEdit extends AppCompatActivity {
    private Button btnSaveE, btnCancelE;
    private EditText nameTextE, breedTextE, wghtTextE, ageTextE;
    private RadioGroup typeRE;
    private FirebaseFirestore db;
    private String userUid,uniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_edit);

        Intent intent = getIntent();
        if (intent != null){
            uniqueId = intent.getStringExtra("petID");
        }

        btnCancelE = findViewById(R.id.btnCancelE);
        btnSaveE = findViewById(R.id.btnSaveE);
        nameTextE = findViewById(R.id.nameTextE);
        breedTextE = findViewById(R.id.breedTextE);
        wghtTextE = findViewById(R.id.wghtTextE);
        ageTextE = findViewById(R.id.ageTextE);
        typeRE = findViewById(R.id.typeRE);
        this.db = FirebaseFirestore.getInstance();
        this.userUid = FirebaseAuth.getInstance().getUid();
        fillFieldsRequest();

        btnSaveE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        btnCancelE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetEdit.this, PetDisplay.class);
                intent.putExtra("petID",uniqueId);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fillFieldsRequest(){
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

                                    fillFields(petName, petType, petBreed, petWeight, petAge);
                                }

                            }
                        } else {
                            //fail
                        }
                    }
                });
    }

    private void fillFields(String petName, String petType, String petBreed, String petWeight, String petAge){
        this.nameTextE.setText(petName);
        if(petType.equals("Dog")){
            typeRE.check(R.id.rbtnDogE);
        }
        else if(petType.equals("Cat")){
            typeRE.check(R.id.rbtnCatE);
        }
        else if(petType.equals("Bird")){
            typeRE.check(R.id.rbtnBirdE);
        }
        else if(petType.equals("Rabbit")){
            typeRE.check(R.id.rbtnRabbitE);
        }
        else if(petType.equals("Others")){
            typeRE.check(R.id.rbtnOthersE);
        }
        this.breedTextE.setText(petBreed);
        this.wghtTextE.setText(petWeight);
        this.ageTextE.setText(petAge);
    }

    private void save(){
        String name = nameTextE.getText().toString();
        String breed = breedTextE.getText().toString();
        String weight = wghtTextE.getText().toString();
        String age = ageTextE.getText().toString();

        String type = "";
        if(typeRE.getCheckedRadioButtonId()!=-1){
            int id= typeRE.getCheckedRadioButtonId();
            View radioButton = typeRE.findViewById(id);
            int radioId = typeRE.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) typeRE.getChildAt(radioId);
            type = (String) btn.getText();
        }


        Map<String, Object> user = new HashMap<>();
        user.put("Name",name);
        user.put("Type",type);
        user.put("Breed",breed);
        user.put("Weight",weight);
        user.put("Age",age);
        db.collection(this.userUid).document(this.uniqueId)
                .set(user);
        Intent intent = new Intent(PetEdit.this, PetDisplay.class);
        intent.putExtra("petID",uniqueId);
        startActivity(intent);
        finish();

    }


}
