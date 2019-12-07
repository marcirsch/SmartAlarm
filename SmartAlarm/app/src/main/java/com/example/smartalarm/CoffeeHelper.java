package com.example.smartalarm;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class CoffeeHelper {
    private static Timer timer;

    public static void startCoffeeMaker(final Context context){
        //1. Call socket api to turn it on

        //2. Start scheduled task in 10 minutes
    }

    private static void stopCoffeeMaker(){
        //Stop coffee socket
    }
}
