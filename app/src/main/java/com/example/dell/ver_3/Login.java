package com.example.dell.ver_3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button enter_password = (Button) findViewById(R.id.button);
        final EditText get_password = (EditText) findViewById(R.id.enter_mp);
        Button forget_pass_btn=(Button)findViewById(R.id.button_frgt);


            //UI part
        final TextView hint=(TextView) findViewById(R.id.hint_text);

        get_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    hint.setVisibility(View.VISIBLE);
              

            }
        });
        //UI ends


        enter_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);
                Integer master_password = sp.getInt("password", 1);

                try {
                    if (Integer.parseInt(get_password.getText().toString()) != master_password) {
                        Toast.makeText(getApplicationContext(), "Password is Wrong", Toast.LENGTH_LONG).show();
                    } else {
                        Intent i = new Intent(Login.this, Navigation.class);
                        startActivity(i);
                        finish();
                    }
                } catch (NumberFormatException ignore) {
                }
            }
        });

        forget_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,Forget_password.class);
                startActivity(i);
            }
        });
    }
}
