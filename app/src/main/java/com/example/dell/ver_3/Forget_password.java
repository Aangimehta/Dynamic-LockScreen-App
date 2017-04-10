package com.example.dell.ver_3;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class Forget_password extends AppCompatActivity {

    String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        TextView get_question=(TextView)findViewById(R.id.get_question1);
        final EditText get_answer=(EditText)findViewById(R.id.write_ans);
        final Button show_pass=(Button)findViewById(R.id.button_show);
        final TextView password=(TextView)findViewById(R.id.show_pass);

        get_answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            TextView hint_get_ans=(TextView)findViewById(R.id.write_ans_hint);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    hint_get_ans.setVisibility(View.VISIBLE);
                else
                    hint_get_ans.setVisibility(View.GONE);

            }
        });

        SharedPreferences sp = getSharedPreferences("Data", Context.MODE_PRIVATE);
        String question=sp.getString("que","abc");
        final Integer master_password=sp.getInt("password",1);
        answer=sp.getString("ans","xyz");
        get_question.setText(question);

        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(get_answer.getText().toString().equals(answer))
                    password.setText(String.valueOf(master_password));
                else
                    password.setText("Your answer is incorrect");
            }
        });



    }
}
