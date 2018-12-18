package com.example.viix.mpc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReminderView extends AppCompatActivity {
    private Button btnSReminders, btnVReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_view);

        btnSReminders=findViewById(R.id.btnSReminders);
        btnVReminders=findViewById(R.id.btnVReminders);


        btnVReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReminderView.this, ReminderDisplay.class));
            }
        });


        btnSReminders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReminderView.this, Reminder2.class));
            }
        });

    }
}
