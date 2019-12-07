package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Time;
import java.util.Calendar;

public class AlarmFragment extends Fragment  {

    private Switch alarmSwitch;
    private TimePicker alarmTimePicker;
    private CheckBox coffeeCheckBox;
    private ImageView monday;

    private static final float[] NEGATIVE = {
            -1.0f,     0,     0,    0, 255, // red
            0, -1.0f,     0,    0, 255, // green
            0,     0, -1.0f,    0, 255, // blue
            0,     0,     0, 1.0f,   0  // alpha
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        //Setup Alarmswitch holder and onChange listener to interface preferences
        alarmSwitch = view.findViewById(R.id.alarm_switch);
        alarmSwitch.setOnCheckedChangeListener(switchListener);

        //Setup Alarm time picker instance
        alarmTimePicker = view.findViewById(R.id.alarm_time_picker);
        alarmTimePicker.setOnTimeChangedListener(timePickerListener);

        //Make coffee checkbox
        coffeeCheckBox = view.findViewById(R.id.make_coffee_cb);
        coffeeCheckBox.setOnCheckedChangeListener(coffeeCheckBoxListener);

        //Set default state from preferences
        alarmSwitch.setChecked(PreferencesHelper.getAlarmSwitch(getContext()));
        //Set alarm time from preferences
        PreferencesHelper.Time alarmTime = PreferencesHelper.getAlarmTime(getContext());
        alarmTimePicker.setHour(alarmTime.hours);
        alarmTimePicker.setMinute(alarmTime.minutes);
        //Set checkbox state from preferences
        coffeeCheckBox.setChecked(PreferencesHelper.getAlarmCoffeeSwitch(getContext()));


        return view;
    }

    CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //Save switch state
            PreferencesHelper.saveAlarmSwitch(getContext(), isChecked);

            //Set or cancel alarm
            if(isChecked){
                PreferencesHelper.Time alarmTime = PreferencesHelper.getAlarmTime(getContext());
                createAlarm("SmartAlarm", alarmTime.hours, alarmTime.minutes);

                Toast.makeText(getContext(), "Wakeup alarm set", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Wakeup alarm switched off", Toast.LENGTH_SHORT).show();
                cancelAlarm();
            }

        }
    };

    TimePicker.OnTimeChangedListener timePickerListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            PreferencesHelper.saveAlarmTime(getContext(), hourOfDay, minute);
            //Toast.makeText(getContext(), "Time changed to " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
        }
    };

    CompoundButton.OnCheckedChangeListener coffeeCheckBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PreferencesHelper.saveAlarmCoffeeSwitch(getContext(), isChecked);
        }
    };

    public void createAlarm(String message, int hour, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        startAlarm(calendar);
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);

        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
