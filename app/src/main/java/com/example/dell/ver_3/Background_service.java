package com.example.dell.ver_3;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import static com.example.dell.ver_3.Navigation.curTime;
import static com.example.dell.ver_3.Navigation.curBattery;
import static com.example.dell.ver_3.Navigation.curDate;
import static com.example.dell.ver_3.Navigation.groupCode;

public class Background_service extends Service {

    private static final String TAG = "Background_service";
    private final Handler handler = new Handler();
    static boolean isServiceRunning;
    Intent intent;
    int code;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service onCreate");
        intent = new Intent("message");
        isServiceRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service onStartCommand");
        code = intent.getIntExtra("code", 0);
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
        return Service.START_STICKY;
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            if (code == 1) {
                currentTime();
            } else if (code == 2) {
                currentDate();
            } else if (code == 3) {
                currentBattery();
            } else {
                onDestroy();
            }
            handler.postDelayed(this, 1000);
        }
    };

    private void currentTime() {

        Date dt = new Date();
        if (groupCode == 1) {
            DateFormat format = new SimpleDateFormat("hhmm", Locale.getDefault());
            curTime = format.format(dt);

        } else if (groupCode == 2) {
            DateFormat format = new SimpleDateFormat("HHmm", Locale.getDefault());
            curTime = format.format(dt);

        } else if (groupCode == 3) {
            Date ddt = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
            DateFormat format = new SimpleDateFormat("hhmm", Locale.getDefault());
            curTime = format.format(ddt);

        } else if (groupCode == 4) {
            Date ddt = new Date(System.currentTimeMillis() - 5 * 60 * 1000);
            DateFormat format = new SimpleDateFormat("hhmm", Locale.getDefault());
            curTime = format.format(ddt);

        } else if (groupCode == 5) {
            DateFormat format = new SimpleDateFormat("mm", Locale.getDefault());
            String mm = format.format(dt);
            curTime = mm + mm;

        } else if (groupCode == 6) {
            DateFormat format = new SimpleDateFormat("hhmm", Locale.getDefault());
            curTime = new StringBuilder(format.format(dt)).reverse().toString();

        } else {
            groupCode = 1;
        }
        intent.putExtra("serviceMessage", curTime);
        LocalBroadcastManager.getInstance(Background_service.this).sendBroadcast(intent);
        displayNotification();
    }

    private void currentDate() {

        Date d = new Date();
        if (groupCode == 7) {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMM", Locale.getDefault());
            curDate = sdf.format(d);
        } else if (groupCode == 8) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMyy", Locale.getDefault());
            curDate = sdf.format(d);
        } else if (groupCode == 9) {
            SimpleDateFormat sdf = new SimpleDateFormat("ddyy", Locale.getDefault());
            curDate = sdf.format(d);
        } else if (groupCode == 10) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
            curDate = sdf.format(d);
        } else if (groupCode == 11) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
            String dd = sdf.format(d);
            curDate = dd + dd;
        } else if (groupCode == 12) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMdd", Locale.getDefault());
            curDate = new StringBuilder(sdf.format(d)).reverse().toString();
        } else {
            groupCode = 7;
        }
        intent.putExtra("serviceMessage", curDate);
        LocalBroadcastManager.getInstance(Background_service.this).sendBroadcast(intent);
        displayNotification();
    }

    private void currentBattery() {
        curBattery = getBattery();
        intent.putExtra("serviceMessage", curBattery);
        LocalBroadcastManager.getInstance(Background_service.this).sendBroadcast(intent);
        displayNotification();
    }

    private String getBattery() {
        String temp="1234";
        String calculatedBattery;

        // getting original current battery percentage

        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryIntent != null;
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        float scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPer = (level / scale) * 100;

        // checking for modifiers
        if (groupCode == 13) {
            temp = String.valueOf((int) batteryPer);
        } else if (groupCode == 14) {
            temp = String.valueOf(((int) batteryPer + 5) % 100);
        } else if (groupCode == 15) {
            temp = String.valueOf(((int) batteryPer - 5) % 100);
        } else if (groupCode == 16) {
            String def = String.valueOf((int) batteryPer);
            temp = new StringBuilder(def).reverse().toString();
        }
        calculatedBattery = temp;
        if (calculatedBattery.length() == 1) {
            calculatedBattery = calculatedBattery + calculatedBattery + calculatedBattery + calculatedBattery;
        } else if (calculatedBattery.length() == 2) {
            calculatedBattery = calculatedBattery + calculatedBattery;
        } else if (calculatedBattery.length() == 3) {
            calculatedBattery = calculatedBattery + "0";
        }
        return calculatedBattery;
    }

    private void displayNotification() {
        Intent notificationIntent = new Intent(Background_service.this, Splash.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Background_service.this, 0,
                notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(Background_service.this)
                .setContentTitle("DLS")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Tap to Open the app")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1337, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {  // send notification that service was destroyed
        Log.d(TAG, "OnDestroy called");
        handler.removeCallbacks(sendUpdatesToUI);
        isServiceRunning = false;
        super.onDestroy();
    }
}