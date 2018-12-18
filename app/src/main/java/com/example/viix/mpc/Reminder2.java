package com.example.viix.mpc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class  Reminder2 extends AppCompatActivity implements View.OnClickListener {

    private Button btnSDate, btnSTime, btnSPet, btnSsave;
    private EditText estimatedTimeText;
    private TextView textSDate, textSTime, textSPet, textNote;
    private CheckBox repeatCheckBox;
    private FirebaseFirestore db;
    private String userUid,uniqueId;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private LinearLayout checkboxLayout;
    private HashMap<String,CheckBox> checkboxHashmap;
    private ArrayList<String> petArray;
    private ArrayList<String> petNamesArray;
    private View checkBoxView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder2);


        btnSDate=findViewById(R.id.btnSDate);
        btnSTime=findViewById(R.id.btnSTime);
        btnSPet=findViewById(R.id.btnSPet);
        btnSsave=findViewById(R.id.btnSsave);
        textNote=findViewById(R.id.textNote);
        repeatCheckBox=findViewById(R.id.repeatCheckBox);

        textSDate=findViewById(R.id.textSDate);
        textSTime=findViewById(R.id.textSTime);
        textSPet=findViewById(R.id.textSPet);

        btnSDate.setOnClickListener(this);
        btnSTime.setOnClickListener(this);
        btnSPet.setOnClickListener(this);
        btnSsave.setOnClickListener(this);
        this.db = FirebaseFirestore.getInstance();
        this.userUid = FirebaseAuth.getInstance().getUid();


        this.checkboxHashmap = new HashMap<>();
        this.petArray = new ArrayList<>();
        this.petNamesArray = new ArrayList<>();



    }

    @Override
    protected void onStart(){
        super.onStart();
        checkBoxView = View.inflate(this,R.layout.pick_pet,null);
        checkboxLayout = checkBoxView.findViewById(R.id.layoutCheckBoxes);
    }

    @Override
    public void onClick(View v) {

        if (v == btnSDate) {
            pickDate();
        }
        else if (v == btnSTime) {
            pickTime();
        }
        else if (v == btnSPet) {
            pickPet();
        }
        else if (v == btnSsave){
            saveWish();
        }
    }


    void pickDate(){
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
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
    }

    void pickTime(){
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
                        mHour = hourOfDay;
                        mMinute = minute;

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    void pickPet(){
        this.checkboxHashmap.clear();
        this.petArray.clear();
        this.checkboxLayout.removeAllViews();
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
                                    addCheckbox(petName, petID);
                                }
                            }
                        } else {
                            //fail
                        }
                    }
                });

        displayPopup();
    }

    void addCheckbox(String petName,String petID){
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(petName);
        this.checkboxLayout.addView(checkBox);
        this.checkboxHashmap.put(petID,checkBox);

    }

    void displayPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pets");
        if(checkBoxView.getParent() != null){
            ((ViewGroup)checkBoxView.getParent()).removeView(checkBoxView);
        }
        builder.setMessage("Pick your pets")
                .setView(checkBoxView)
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        petNamesArray.clear();
                        fillPetArray();
                        fillPetText();
                    }


                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    void fillPetText(){
        String results = "";
        for(String result : this.petNamesArray){
            results = results + " " + result + ", ";
        }
        textSPet.setText(results);

    }
    void fillPetArray(){
        for(HashMap.Entry<String,CheckBox> entry : this.checkboxHashmap.entrySet()){
            if(entry.getValue().isChecked()){
                this.petArray.add(entry.getKey());
                this.petNamesArray.add(entry.getValue().getText().toString());
            }
        }
    }


    private void saveWish(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Set Reminders")
                .setMessage("Are you sure you want to add this to your calendar?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addToCalenndar();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void addToCalenndar() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, textNote.getText().toString());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, textSPet.getText().toString());

        //TODO: get time and date from the textView so the user doesnt have to input it x2


        GregorianCalendar calDate = new GregorianCalendar(mYear, mMonth, mDay, mHour,mMinute);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
       // long estimatedTimeInMs = Integer.parseInt(estimatedTimeText.getText().toString())*60000;
        //intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
          //      calDate.getTimeInMillis()+estimatedTimeInMs);
        if(repeatCheckBox.isChecked()){
            //TODO: Get day of the week, add it to string, pass it as argument
            //intent.putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");
        }

        intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        startActivity(intent);
        save();
    }



    private void save() {
        Map<String, Object> user = new HashMap<>();
        user.put("DataType", "ReminderData");
        user.put("Year", mYear);
        user.put("Month", mMonth);
        user.put("Day", mDay);
        user.put("Hour", mHour);
        user.put("Minute", mMinute);
        user.put("Pets", petArray);
        user.put("Notes", textNote.getText().toString());
        //we need an unique ID for each reminders
        Long uniqueId = System.currentTimeMillis() / 1000;

        db.collection(this.userUid).document(uniqueId + "")
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

         finish();
    }



}
