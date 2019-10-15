package com.zadanie1.mw;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    TextView response;
    Button button;
    Button restartpassword;
    private EditText textPassword;
    private View viewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        response = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        restartpassword = (Button) findViewById(R.id.restartpassword);
        textPassword = findViewById(R.id.editText);

        restartpassword.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this,
                        FormPassword.class);
                startActivity(myIntent);
                finish();
            }
        });

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                response.setText(textPassword.getText());
                if (textPassword.getText().toString().equals("123")) {
                    Intent myIntent2 = new Intent(MainActivity.this,
                            Noto.class);
                    startActivity(myIntent2);
                    finish();
                }
            }
        });
    }
}