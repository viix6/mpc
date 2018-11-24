package com.example.viix.mpc;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class  Reminder2 extends AppCompatActivity implements
        View.OnClickListener {

        Button btnSDate, btnSTime, btnSPet;
        EditText textSDate, textSTime, textSPet;
        private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder2);

        btnSDate=(Button)findViewById(R.id.btnSDate);
        btnSTime=(Button)findViewById(R.id.btnSTime);
        btnSPet=(Button)findViewById(R.id.btnSPet);

        textSDate=(EditText)findViewById(R.id.textSDate);
        textSTime=(EditText)findViewById(R.id.textSTime);
        textSPet=(EditText)findViewById(R.id.textSPet);

        btnSDate.setOnClickListener(this);
        btnSTime.setOnClickListener(this);
        btnSPet.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == btnSDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            textSDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnSTime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            textSTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == btnSPet){
            //TODO: get pet list
    }
}

}
