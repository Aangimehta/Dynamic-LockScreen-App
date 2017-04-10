package com.example.dell.ver_3;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    public DevicePolicyManager devicePolicyManager;
    public ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final TextView title = (TextView) findViewById(R.id.splash_text);
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 0.1f);

        devicePolicyManager = (DevicePolicyManager) getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, Device_administration.class);


        Thread timerThread = new Thread() {
            public void run() {
                try {
                    title.startAnimation(fadeIn);
                    fadeIn.setDuration(200);
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    SharedPreferences pref = getSharedPreferences("ActivityPREF", 0);
                    if (pref.getBoolean("activity_executed", false)) {
                        if (permissionCheck()) {
                            Intent i = new Intent(Splash.this, Master_password.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(Splash.this, Permission.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(Splash.this, Tutorial.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timerThread.start();
    }

    public boolean permissionCheck() {
        return isReadPhoneAllowed() && (devicePolicyManager != null && devicePolicyManager.isAdminActive(componentName));
    }

    protected boolean isReadPhoneAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        return (result == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
