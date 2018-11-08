package com.example.viix.mpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;


public class Reminder extends AppCompatActivity {

   private CalendarView calendarView;
   private Button btnSaveC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);


        calendarView = findViewById(R.id.calendarView);
        btnSaveC = findViewById(R.id.btnSaveC);

        //TODO: select date and time
        //TODO: retrieve pet name once date and time r chosen
        //TODO: set reminder for selected time and date
        //TODO: display reminder on chosen dates and times





    }
}
