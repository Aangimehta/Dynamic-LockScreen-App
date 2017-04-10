package com.example.dell.ver_3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static com.example.dell.ver_3.Navigation.flag;

public class Master_password extends AppCompatActivity {

    public static String maspasscfrm;
    public static String maspass;
    public static String que, ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!flag) {
            SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);
            Integer master_password = sp.getInt("password", 1);
            if (master_password != 1) {
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText get_ans = (EditText) findViewById(R.id.get_ans);

        EditText a = (EditText) findViewById(R.id.mp_get);
        EditText b = (EditText) findViewById(R.id.mp_confirm);

        //UI part
        a.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView mp_hint = (TextView) findViewById(R.id.epass);
                if (hasFocus)
                    mp_hint.setVisibility(View.VISIBLE);

            }
        });

        b.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView cfrm_hint = (TextView) findViewById(R.id.cfrpass);
                if (hasFocus)
                    cfrm_hint.setVisibility(View.VISIBLE);

            }
        });

        get_ans.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            TextView hint = (TextView) findViewById(R.id.ans_hint);

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    hint.setVisibility(View.VISIBLE);

            }
        });

        //UI Ends

        final Spinner spinner = (Spinner) findViewById(R.id.security_que);

        ArrayAdapter<String> questions = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.questions));
        spinner.setAdapter(questions);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                que = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                get_ans.setError("Atleast choose one question");

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ans = String.valueOf(get_ans.getText());

                EditText master_password = (EditText) findViewById(R.id.mp_get);
                EditText confirm_mp = (EditText) findViewById(R.id.mp_confirm);

                maspass = master_password.getText().toString();
                maspasscfrm = confirm_mp.getText().toString();

                try {
                    if (maspass.isEmpty() || maspass.length() < 4 || maspass.equals(" ") || maspass.matches("")) {
                        master_password.setError("Password should be between 4-8 digits long");
                    } else {
                        if (maspasscfrm.isEmpty() || maspasscfrm.length() < 4 || maspasscfrm.equals("") || maspass.matches("")) {
                            confirm_mp.setError("Password should be between 4-8 digits long");
                        }
                    }

                    if (Integer.parseInt(maspass) != Integer.parseInt(maspasscfrm)) {

                        Snackbar.make(view, "Both password not matched!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("password", Integer.parseInt(maspass));
                        editor.putString("que", que);
                        editor.putString("ans", ans);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Master Password Saved!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Master_password.this, Navigation.class);
                        startActivity(intent);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        });
    }
}
