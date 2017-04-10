package com.example.dell.ver_3;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;



public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DevicePolicyManager mDevicePolicyManager;
    public ComponentName mComponentName;
    TextView default_text, pinDisplayed;
    public Switch mainSwitch, timeSwitch, dateSwitch, batterySwitch;
    LinearLayout contents;
    static String curTime, curDate, curBattery;

    RadioGroup t_grp, d_grp, b_grp;

    SharedPreferences.Editor editor;

    static String serviceReturnString;
    static int code;
    static int groupCode;
    static boolean flag = false;

   public static String master_password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);
        Integer mp = sp.getInt("password", 1);
        master_password = Integer.toString(mp);

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, Device_administration.class);

        pinDisplayed = (TextView) findViewById(R.id.pin);
        mainSwitch = (Switch) findViewById(R.id.main_switch);
        default_text = (TextView) findViewById(R.id.Switch_text);
        contents = (LinearLayout) findViewById(R.id.content);
        timeSwitch = (Switch) findViewById(R.id.time_switch);
        dateSwitch = (Switch) findViewById(R.id.date_switch);
        batterySwitch = (Switch) findViewById(R.id.battery_switch);

        t_grp = (RadioGroup) findViewById(R.id.time_grp);
        d_grp = (RadioGroup) findViewById(R.id.date_grp);
        b_grp = (RadioGroup) findViewById(R.id.battery_grp);

        timeSwitch.setEnabled(false);
        dateSwitch.setEnabled(false);
        batterySwitch.setEnabled(false);


        final RadioButton t_r = (RadioButton) findViewById(R.id.t_r1);
        final RadioButton d_r = (RadioButton) findViewById(R.id.d_r1);
        final RadioButton b_r = (RadioButton) findViewById(R.id.b_r1);


        final TextView modify = (TextView) findViewById(R.id.text_modify);

        default_text.setText(R.string.Enable_Main_switch_above);

         editor = getSharedPreferences("Data", MODE_PRIVATE).edit();

        mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    default_text.setVisibility(View.GONE);
                    timeSwitch.setEnabled(true);
                    dateSwitch.setEnabled(true);
                    batterySwitch.setEnabled(true);
                    pinDisplayed.setText(master_password);
                    setPass(master_password);


                    editor.putBoolean("main_check", true);
                    editor.commit();


                } else {
                    timeSwitch.setEnabled(false);
                    dateSwitch.setEnabled(false);
                    batterySwitch.setEnabled(false);


                    timeSwitch.setChecked(false);
                    dateSwitch.setChecked(false);
                    batterySwitch.setChecked(false);


                    default_text.setVisibility(View.VISIBLE);
                    stopService();
                    setPasstoNone();


                    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    editor.putBoolean("main_check", false);
                    editor.commit();
                }
            }
        });

        timeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dateSwitch.setChecked(false);
                    batterySwitch.setChecked(false);
                    code = 1;
                    modify.setVisibility(View.VISIBLE);
                    t_grp.setVisibility(View.VISIBLE);
                    t_r.setChecked(true);
                    startService(code);

                    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    editor.putBoolean("time_check", true);
                    editor.commit();

                } else {
                    stopService();

                    t_grp.setVisibility(View.GONE);
                    modify.setVisibility(View.GONE);
                    pinDisplayed.setText(master_password);
                    setPass(master_password);

                    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    editor.putBoolean("time_check", false);
                    editor.commit();

                }
            }
        });


        t_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.t_r1:
                        groupCode = 1;
                        // do operations specific to this selection
                        break;
                    case R.id.t_r2:
                        groupCode = 2;
                        break;
                    case R.id.t_r3:
                        groupCode = 3;
                        break;
                    case R.id.t_r4:
                        groupCode = 4;
                        break;
                    case R.id.t_r5:
                        groupCode = 5;
                        break;
                    case R.id.t_r6:
                        groupCode = 6;
                        break;
                }
            }
        });

        dateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeSwitch.setChecked(false);
                    batterySwitch.setChecked(false);
                    code = 2;
                    modify.setVisibility(View.VISIBLE);
                    d_grp.setVisibility(View.VISIBLE);
                    d_r.setChecked(true);
                    startService(code);

                    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    editor.putBoolean("date_check", true);
                    editor.commit();


                } else {
                    d_grp.setVisibility(View.GONE);
                    modify.setVisibility(View.GONE);
                    stopService();
                    pinDisplayed.setText(master_password);
                    setPass(master_password);

                    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    editor.putBoolean("date_check", false);
                    editor.commit();

                }
            }
        });

        d_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.d_r1:
                        groupCode = 7;
                        break;
                    case R.id.d_r2:
                        groupCode = 8;
                        break;
                    case R.id.d_r3:
                        groupCode = 9;
                        break;
                    case R.id.d_r4:
                        groupCode = 10;
                        break;
                    case R.id.d_r5:
                        groupCode = 11;
                        break;
                    case R.id.d_r6:
                        groupCode = 12;
                        break;
                }
            }
        });

        batterySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timeSwitch.setChecked(false);
                    dateSwitch.setChecked(false);
                    code = 3;
                    startService(code);
                    modify.setVisibility(View.VISIBLE);
                    b_grp.setVisibility(View.VISIBLE);
                    b_r.setChecked(true);

                    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    editor.putBoolean("battery_check", true);
                    editor.commit();

                } else {
                    stopService();
                    modify.setVisibility(View.GONE);
                    b_grp.setVisibility(View.GONE);
                    pinDisplayed.setText(master_password);
                    setPass(master_password);

                    SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    editor.putBoolean("battery_check", false);
                    editor.commit();

                }
            }
        });

        b_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.b_r1:
                        groupCode = 13;
                        break;
                    case R.id.b_r2:
                        groupCode = 14;
                        break;
                    case R.id.b_r3:
                        groupCode = 15;
                    case R.id.b_r4:
                        groupCode = 16;
                }
            }
        });

        SharedPreferences sharedPrefs = getSharedPreferences("Data", MODE_PRIVATE);
        mainSwitch.setChecked(sharedPrefs.getBoolean("main_check",false));
        timeSwitch.setChecked(sharedPrefs.getBoolean("time_check", false));
        dateSwitch.setChecked(sharedPrefs.getBoolean("date_check", false));
        batterySwitch.setChecked(sharedPrefs.getBoolean("battery_check",false));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void startService(int x) {

        Intent intent = new Intent(Navigation.this, Background_service.class);
        intent.putExtra("code", x);
        startService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(Navigation.this, Background_service.class);
        stopService(intent);
    }

    public void setPass(String pass) {
        pinDisplayed.setText(pass);
        mDevicePolicyManager.setPasswordQuality(mComponentName, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        mDevicePolicyManager.setPasswordMinimumLength(mComponentName, 4);
        mDevicePolicyManager.resetPassword(pass,
                DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
    }

    public void setPasstoNone() {
        pinDisplayed.setText("");
        mDevicePolicyManager.setPasswordQuality(mComponentName, DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED);
        mDevicePolicyManager.setPasswordMinimumLength(mComponentName, 0);
        mDevicePolicyManager.resetPassword("",
                DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
    }

    // Default system generated methods below

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.tutorial:
                Intent i = new Intent(this, Tutorial.class);
                i.putExtra("tutorialTAG", true);
                startActivity(i);
                break;
            case R.id.about_us:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setView(R.layout.about_us);
                alertDialogBuilder.setTitle("About Us");
                alertDialogBuilder
                        .setCancelable(false)
                        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.change_mp:
                Intent i1 = new Intent(this, Master_password.class);
                flag = true;
                startActivity(i1);
                break;

            case R.id.uninstall:
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:"+this.getPackageName()));
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Broadcast receiver and its methods below this point

    private BroadcastReceiver BReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //put here whatever you want your activity to do with the intent received
            serviceReturnString = intent.getStringExtra("serviceMessage");
            setPass(serviceReturnString);
        }
    };

    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(BReceiver, new IntentFilter("message"));
    }

    /* @Override
    protected void onPause() {
        super.onPause();

        if(isFinishing())
        {
            if(mainSwitch.isChecked())

            {
                SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                editor.putBoolean("main_check", true);
                editor.apply();
                if(timeSwitch.isChecked())
                {
                    SharedPreferences.Editor e1 = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    e1.putBoolean("time_check", true);
                    e1.apply();
                }
                else if(dateSwitch.isChecked())
                {
                    SharedPreferences.Editor e1 = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    e1.putBoolean("date_check", true);
                    e1.apply();
                }
                else if(batterySwitch.isChecked())
                {
                    SharedPreferences.Editor e1 = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    e1.putBoolean("battery_check", true);
                    e1.apply();
                }
                else
                {
                    SharedPreferences.Editor e1 = getSharedPreferences("Data", MODE_PRIVATE).edit();
                    e1.putBoolean("time_check", false);
                    e1.putBoolean("date_check", false);
                    e1.putBoolean("battery_check", false);
                    e1.apply();
                }

            }

            else {
                SharedPreferences.Editor editor = getSharedPreferences("Data", MODE_PRIVATE).edit();
                editor.putBoolean("main_check", false);
                editor.apply();
            }


        }



    }*/
}
