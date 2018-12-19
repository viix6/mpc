package com.example.viix.mpc;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ReminderDisplay extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userUid;
    private LinearLayout linearLayout;
    private String stringPetNames = "";
    private HashMap<String,String> petData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_display);

        this.db = FirebaseFirestore.getInstance();
        this.userUid = FirebaseAuth.getInstance().getUid();
        linearLayout = findViewById(R.id.RDLayout);
        getAllPetNames();
        display();

    }

    public void getAllPetNames(){
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
                                    petData.put(petID,petName);
                                }

                            }
                        } else {
                            //fail
                        }
                    }
                });
    }



    public void display(){
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        this.linearLayout.removeAllViews();
        db.collection(this.userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if("ReminderData".equals((String)document.get("DataType"))) {
                                    long dYear = (long) document.get("Year");
                                    long dMonth = (long) document.get("Month");
                                    long dDay = (long) document.get("Day");
                                   if (dYear == mYear && dMonth == mMonth && dDay == mDay ){
                                       long dHour = (long) document.get("Hour");
                                       long dMinute = (long) document.get("Minute");
                                       String note = (String) document.get("Notes");
                                       List<String>pets = (List<String>) document.get("Pets");
                                       showReminder(dHour, dMinute, note, pets);
                                   }
                                }

                            }
                        } else {
                            //fail
                        }
                    }
                });

    }

    private void showReminder(long dHour,long dMinute,String note,List<String> pets) {
        stringPetNames = "";


        TextView petsText = new TextView(this);
        TextView hourText = new TextView(this);
        TextView noteText = new TextView(this);
        TextView lineText = new TextView(this);

        for(String pet:pets){
            stringPetNames = stringPetNames + petData.get(pet)+ ", ";
        }

        petsText.setText("Pets: " + stringPetNames);
        petsText.setTextColor(Color.parseColor("#421d5b"));
        petsText.setTextScaleX(Float.parseFloat("0.8"));
        petsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        hourText.setText("Time: " + dHour +":" + dMinute);
        hourText.setTextColor(Color.parseColor("#905ea1"));
        hourText.setTextScaleX(Float.parseFloat("0.8"));
        hourText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        noteText.setText("Note: " + note);
        noteText.setTextColor(Color.parseColor("#905ea1"));
        noteText.setTextScaleX(Float.parseFloat("0.8"));
        noteText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        lineText.setText(" _____________________________ ");
        lineText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        this.linearLayout.addView(petsText);
        this.linearLayout.addView(hourText);
        this.linearLayout.addView(noteText);
        this.linearLayout.addView(lineText);

    }
}
