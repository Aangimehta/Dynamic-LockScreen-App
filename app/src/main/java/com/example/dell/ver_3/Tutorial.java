package com.example.dell.ver_3;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import static com.example.dell.ver_3.Permission.componentName;
import static com.example.dell.ver_3.Permission.devicePolicyManager;

public class Tutorial extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();
        if(!intent.getBooleanExtra("tutorialTAG",false)){
            SharedPreferences pref = getSharedPreferences("ActivityPREF", 0);
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.apply();
        }

        addSlide(AppIntroFragment.newInstance("Permission", "It Require to enable both the switches", R.drawable.per_tutorial, Color.parseColor("#f64c73")));

        addSlide(AppIntroFragment.newInstance("Setup Master Password", "Long Description", R.drawable.setup_mass, Color.parseColor("#20d2bb")));

        addSlide(AppIntroFragment.newInstance("Enable master switch", "Long Description", R.drawable.mass_default, Color.parseColor("#3395ff")));

        addSlide(AppIntroFragment.newInstance("Time Modifiers", "Long Description", R.drawable.time_m, Color.parseColor("#c873f4")));

        addSlide(AppIntroFragment.newInstance("Date Modifiers", "Long Description", R.drawable.date_m, Color.parseColor("#c873f4")));

        addSlide(AppIntroFragment.newInstance("Battery Modifiers", "Long Description", R.drawable.battery_m, Color.parseColor("#c873f4")));



        // setBarColor(Color.parseColor("#3F51B5"));
        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        if (permissionCheck()) {
            Intent intent = new Intent(Tutorial.this, Master_password.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(Tutorial.this, Permission.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        try {
            if (permissionCheck()) {
                Intent intent = new Intent(Tutorial.this, Master_password.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Tutorial.this, Permission.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception ignore) {
        }
    }

    public boolean permissionCheck() {
        return isReadPhoneAllowed() && (devicePolicyManager != null && devicePolicyManager.isAdminActive(componentName));
    }

    protected boolean isReadPhoneAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        return (result == PackageManager.PERMISSION_GRANTED);
    }

}

