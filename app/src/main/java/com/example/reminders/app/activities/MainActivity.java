package com.example.reminders.app.activities;

import android.content.Intent;
import android.os.Bundle;
import com.crashlytics.android.Crashlytics;
import com.example.reminders.app.dal.DalFactory;
import roboguice.activity.RoboFragmentActivity;

public class MainActivity extends RoboFragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        //setContentView(R.layout.activity_main);

        init();
        Intent intent = new Intent(this, MainActivity_with_Fragment.class);
        startActivity(intent);
        finish();
    }

    private void init() {
        new DalFactory(this.getApplicationContext());
    }
}
