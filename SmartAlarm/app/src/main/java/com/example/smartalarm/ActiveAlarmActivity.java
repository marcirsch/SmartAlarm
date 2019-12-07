package com.example.smartalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActiveAlarmActivity extends AppCompatActivity {
    private Button disableButton;
    Ringtone ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_alarm);

        //This activity is only displayed when alarm is firing

        //Start coffee making
        CoffeeHelper.startCoffeeMaker(getApplicationContext());

        //Play ringtone
        try {
            Uri ringToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(this, ringToneUri);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //find disable button
        disableButton = (findViewById(R.id.disable_button));
        disableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Stop ringtone
                try{
                    ringtone.stop();
                }catch (Exception e){
                    e.printStackTrace();
                }
                //Return
                finish();
            }
        });
    }
}
