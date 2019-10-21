package com.zadanie1.mw;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FormPassword extends Activity {
    private static final String FILE_NAME = "passwd.txt";


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
            if (reader != null) {
                reader.close();
            }
        }
        return content;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_password);

        final EditText currentpassword;
        final EditText newpassword;
        final EditText renewpassword;
        Button confirm;

        currentpassword = (EditText) findViewById(R.id.currentpassword);
        newpassword = (EditText) findViewById(R.id.newpassword);
        renewpassword = (EditText) findViewById(R.id.renewpassword);
        confirm = (Button) findViewById(R.id.button);

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final String filename =  getFilesDir() + "/passwd.txt";
                File file = new File(filename);

                try {
                    if (currentpassword.getText().toString().equals(readFile(filename).toString())) {
                        if (newpassword.getText().toString().equals(renewpassword.getText().toString())) {

                            String text = newpassword.getText().toString();
                            FileOutputStream fos = null;

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Password has been changed",
                                    Toast.LENGTH_SHORT);
                            toast.show();

                            try {
                                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                                fos.write(text.getBytes());

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            onBackPressed();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Password has NOT been changed!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Current password is WRONG!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentpassword.getText().clear();
                newpassword.getText().clear();
                renewpassword.getText().clear();
            }
        });
    }
}