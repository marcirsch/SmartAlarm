package com.example.smartalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class SettingsFragment extends Fragment {

    private TextInputEditText ipAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //Setup IP Address holder and onChange listener to interface preferences
        ipAddress = view.findViewById(R.id.mqtt_ip_input);
        ipAddress.setText(PreferencesHelper.getIpAddress(getContext()));
        ipAddress.setOnFocusChangeListener(ipAddressListener);

        return view;
    }


    TextInputEditText.OnFocusChangeListener ipAddressListener = new TextInputEditText.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                PreferencesHelper.saveIpAddress(getContext(), ipAddress.getText().toString());
            }
        }
    };
}
