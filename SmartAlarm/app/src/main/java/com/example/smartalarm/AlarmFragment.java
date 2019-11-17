package com.example.smartalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Time;

public class AlarmFragment extends Fragment {

    private Switch alarmSwitch;
    private TimePicker alarmTimePicker;
    private CheckBox coffeeCheckBox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        //Setup Alarmswitch holder and onChange listener to interface preferences
        alarmSwitch = view.findViewById(R.id.alarm_switch);
        alarmSwitch.setChecked(PreferencesHelper.getAlarmSwitch(getContext()));
        alarmSwitch.setOnCheckedChangeListener(switchListener);

        //Setup Alarm time picker
        alarmTimePicker = view.findViewById(R.id.alarm_time_picker);
        alarmTimePicker.setOnTimeChangedListener(timePickerListener);
        PreferencesHelper.Time alarmTime = PreferencesHelper.getAlarmTime(getContext());
        alarmTimePicker.setHour(alarmTime.hours);
        alarmTimePicker.setMinute(alarmTime.minutes);

        //Make coffee checkbox
        coffeeCheckBox = view.findViewById(R.id.make_coffee_cb);
        coffeeCheckBox.setOnCheckedChangeListener(coffeeCheckBoxListener);
        coffeeCheckBox.setChecked(PreferencesHelper.getAlarmCoffeeSwitch(getContext()));



        return view;
    }

    CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PreferencesHelper.saveAlarmSwitch(getContext(), isChecked);
            //Toast.makeText(getContext(), "Stisch state is: " + isChecked, Toast.LENGTH_SHORT).show();
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
}
