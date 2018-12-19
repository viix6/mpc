package com.example.viix.mpc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AppCompatActivityWithNav extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.hme:
                intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.prfl:
                intent = new Intent(this, Profiles.class);
                startActivity(intent);
                break;
            case R.id.rmnd:
                intent = new Intent(this, ReminderView.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
