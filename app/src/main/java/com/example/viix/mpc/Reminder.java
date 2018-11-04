package com.example.viix.mpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;


public class Reminder extends AppCompatActivity {

    CalendarView calendarView;
    Button btnSaveC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);


        calendarView = findViewById(R.id.calendarView);
        btnSaveC = findViewById(R.id.btnSaveC);







    }
}
