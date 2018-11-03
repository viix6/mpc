package com.example.viix.mpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;



public class Main2Activity extends AppCompatActivity {

    Button btnProfiles;
    Button btnReminders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnProfiles = findViewById(R.id.btnProfiles);
        btnReminders = findViewById(R.id.btnReminders);

        btnProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, Profiles.class));
            }
        });

        btnReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, Reminder.class));
            }
        });
    }
}
