package com.example.reminders.app.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.reminders.app.R;
import com.example.reminders.app.boradcasts.AlarmReceiver;
import com.example.reminders.app.dal.ReminderNoteDal;
import roboguice.fragment.RoboFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class SettingsFragment extends RoboFragment {

    private ReminderNoteDal reminderNoteDal;
    private ArrayAdapter<String> arrayAdapter;
    private View rootView;
    private ListView listView;
    private List<String> comments;
    private Set<String> commentsSet;
    private EditText commentEditText;

    // Store contacts values in these arraylist
    public static ArrayList<String> phoneValueArr = new ArrayList<String>();
    public static ArrayList<String> nameValueArr = new ArrayList<String>();
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> acPhoneAdapter;
    private String acChosenNumber;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SettingsFragment() {

    }

    public void init(Context applicationContext) {
        this.reminderNoteDal = new ReminderNoteDal(applicationContext);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initViews(inflater, container);
        initPendingIntent();
        return rootView;
    }

    private void initPendingIntent() {
    /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
    }

    private PendingIntent pendingIntent;


    private void initViews(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        rootView.findViewById(R.id.startAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        rootView.findViewById(R.id.stopAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        rootView.findViewById(R.id.stopAlarmAt10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAt10();
            }
        });
    }

    public void start() {
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        int interval = 2000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(getActivity(), "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(getActivity(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startAt10() {
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        int interval = 1000* 2; //1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, pendingIntent);
    }

}
