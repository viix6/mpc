package com.example.viix.mpc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.support.annotation.NonNull;
import android.widget.Toast;


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
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                String msg = "Selected date Day: " + i2 + " Month : " + (i1 + 1) + " Year " + i;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();




            }
        });
        //TODO: retrieve pet name once date and time r chosen
        //TODO: set reminder for selected time and date
        //TODO: display reminder on chosen dates and times





    }
}
