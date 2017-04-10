package com.example.dell.ver_3;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Permission extends AppCompatActivity {

    public static DevicePolicyManager devicePolicyManager;
    public static ComponentName componentName;
    private static final int ADMIN_INTENT = 1;
   Switch device_admin, device_phone;
    //Permission code that will be checked in the method onRequestPermissionsResult
    private int PHONE_PERMISSION_CODE = 23;
    //String phone = "Manifest.permission.READ_PHONE_STATE";
    String a;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        a=this.getPackageName();

       // if version>marshmallow recommend to uninstall
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.M)
        {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Recommend UNINSTALL");
            alertDialogBuilder.setCancelable(false)
                    .setMessage("This app is not compatible for NOUGAT due to new security features it leads to force stop")
                    .setNegativeButton("UNINSTALL",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Intent intent = new Intent(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:"+a));
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= 21) {
            TransitionInflater transitionInflater = TransitionInflater.from(this);
            Transition transition = transitionInflater.inflateTransition(R.transition.tranisition_set);
            getWindow().setExitTransition(transition);
        }

        TextView text_device_admin = (TextView) findViewById(R.id.device_admin_text);
        text_device_admin.setText("Device Administration Rights\n\nNeeded to change the screen unlock password,as per options selected.");

        TextView text_call = (TextView) findViewById(R.id.phone_text);
        text_call.setText("Phone Permissions\n\nThis Permission enable app to set accurate screen unlock password on call disconnect.This ensures that the user able to unlock device with the accurate screen unlock password.");

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle("Permissions");

        device_admin = (Switch) findViewById(R.id.device_admin_switch);
        device_phone = (Switch) findViewById(R.id.phone_permission_switch);

        devicePolicyManager = (DevicePolicyManager) getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, Device_administration.class);


        if (devicePolicyManager != null && devicePolicyManager.isAdminActive(componentName)) {
            // check whether admin is active or not
            device_admin.setChecked(true);
        }
        if (isReadPhoneAllowed()) device_phone.setChecked(true);

        // on clicked switch 1
        device_admin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((Switch) v).isChecked()) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Administrator description");
                    startActivityForResult(intent, ADMIN_INTENT);
                } else {
                    devicePolicyManager.removeActiveAdmin(componentName);
                    device_admin.setChecked(false);
                }
            }
        });

        device_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReadPhoneAllowed()) {
                    device_phone.setChecked(true);
                    Snackbar.make(v, "You already have permission", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    device_phone.setEnabled(false);
                    return;
                }
                askpermission();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadPhoneAllowed() && (devicePolicyManager != null && devicePolicyManager.isAdminActive(componentName))) {
                    Intent i = new Intent(Permission.this, Master_password.class);
                    startActivity(i);
                    finish();
                } else
                    Snackbar.make(view, "Don't have necessary permission to continue ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }

        });
    }

    private boolean isReadPhoneAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        return (result == PackageManager.PERMISSION_GRANTED);
    }

    public void askpermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_PERMISSION_CODE);
    }


    // Result for runtime time permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == PHONE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission has been granted ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                if (!isReadPhoneAllowed()) device_phone.setChecked(false);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADMIN_INTENT) {
            if (!(devicePolicyManager != null && devicePolicyManager.isAdminActive(componentName)))
                device_admin.setChecked(false);
        }
    }

}
