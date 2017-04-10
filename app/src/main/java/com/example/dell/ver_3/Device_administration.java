package com.example.dell.ver_3;

import android.annotation.TargetApi;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static android.content.Context.VIBRATOR_SERVICE;

public class Device_administration extends DeviceAdminReceiver {

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: enabled");
    }

   /* @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "This is an optional message to warn the user about disabling.";
    }*/

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: disabled");
        Log.d(TAG, "Sample Device Admin: disabled");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        Toast.makeText(context, "Device password is now changed",
                Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Device password is now changed");

        DevicePolicyManager localDPM = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName localComponent = new ComponentName(context,
                Device_administration.class);
        localDPM.setPasswordExpirationTimeout(localComponent, 0L);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPasswordExpiring(Context context, Intent intent) {
        // This would require API 11 an above
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: pw failed");
        Vibrator v = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        // Vibrate for 1 seconds
        v.vibrate(1000);

        DevicePolicyManager dm = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);
       ComponentName cm = new ComponentName(context, Device_administration.class);
        int no=dm.getCurrentFailedPasswordAttempts();
        if(no>=3)
        {
           /* Intent i=new Intent(context,Background_service.class);
            context.stopService(i);

            SharedPreferences sp = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
            Integer mp = sp.getInt("password", 1);
            String master_pass = Integer.toString(mp);

            dm.setPasswordQuality(cm, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
            dm.setPasswordMinimumLength(cm, 4);
            dm.resetPassword(master_pass,
                    DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);*/
            //do_things first stop the service tht running and get mp and set mp
        }


    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        showToast(context, "Welcome Device Admin");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("log_tag",
                "MyDevicePolicyReciever Received: " + intent.getAction());
        super.onReceive(context, intent);
    }
}
