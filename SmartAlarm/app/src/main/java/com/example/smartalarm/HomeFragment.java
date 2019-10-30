package com.example.smartalarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    TextView timeTV;
    Timer timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //First inflate fragment, only then find time textView
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Find time on main screen and start update thread
        timeTV = view.findViewById(R.id.time);
        setTime();

        timer = new Timer();

        startClockThread();

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void startClockThread() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        }, 0, 1000);
    }

    private void setTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String currentTime = sdf.format(new Date());
        timeTV.setText(currentTime);
    }
}
