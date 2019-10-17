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
import java.io.FileReader;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    TextView response;
    Button button;
    Button restartpassword;
    private EditText textPassword;
    private View viewPassword;
    private static final String filename = "passwd.txt";

    public String readFile(String filename) throws IOException {
        String content = null;
        File file = new File(filename); // For example, foo.txt
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                reader.close();
            }
        }
        return content;
    }




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
            private final String filename =  getFilesDir() + "/passwd.txt";
            @Override
            public void onClick(View view) {
                response.setText(textPassword.getText());
                try {
                    if (textPassword.getText().toString().equals(readFile(filename).toString())) {
                        Intent myIntent2 = new Intent(MainActivity.this,
                                Noto.class);
                        startActivity(myIntent2);
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}