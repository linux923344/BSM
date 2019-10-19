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
import android.widget.Toast;

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
            }
        });

        button.setOnClickListener(new OnClickListener() {
            private final String filename =  getFilesDir() + "/passwd.txt";

            private boolean isFileExists(File file) {
                return file.exists() && !file.isDirectory();

            }

            @Override
            public void onClick(View view) {
                /*response.setText(textPassword.getText());*/

                File file = new File(filename);

                 try {
                     if (isFileExists(file)) {
                         if (textPassword.getText().toString().equals(readFile(filename).toString())) {
                             Toast toast = Toast.makeText(getApplicationContext(),
                                     "Password is correct!",
                                     Toast.LENGTH_SHORT);
                             toast.show();
                             Intent myIntent2 = new Intent(MainActivity.this,
                                     Noto.class);
                             startActivity(myIntent2);

                         }
                     }  else if (textPassword.getText().toString().equals("FirstPassword!")) {
                         Intent myIntent2 = new Intent(MainActivity.this,
                                 Noto.class);
                         startActivity(myIntent2);
                     }  else {
                         Toast toast = Toast.makeText(getApplicationContext(),
                                 "Password is NOT correct",
                                 Toast.LENGTH_SHORT);
                         toast.show();
                     }

                 }  catch(IOException e){
                     e.printStackTrace();
                 }
            }
        });
    }
}