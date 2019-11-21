package com.zadanie1.mw;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FirstPassword extends Activity {
    private static final String FILE_NAME = "passwd.txt";

    public static String SHA512(String s)
    {
        MessageDigest digest;
        String generatedPassword = null;
        String salt = "Z7F4nGZEYa2AZQ3TVTrgYXJkezWPzDuGh54zVrjWtkGQLQNPFN5edJWvzh5rVgXbY5fv489JHourDWHMTKEumk24e6MzmwC8rCTPCHgKvWCnCjPG4HN3mLuWjWG3yabF";
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));

            byte[] bytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_password);

        final EditText newpassword;
        final EditText renewpassword;
        Button confirm;

        newpassword = (EditText) findViewById(R.id.newpassword);
        renewpassword = (EditText) findViewById(R.id.renewpassword);
        confirm = (Button) findViewById(R.id.button);

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (newpassword.getText().toString().equals(renewpassword.getText().toString())) {

                    String text = SHA512(newpassword.getText().toString());
                    FileOutputStream fos = null;

                    try {
                        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                        fos.write(text.getBytes());
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Password has been changed",
                                Toast.LENGTH_SHORT);
                        toast.show();

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
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Password has NOT been changed",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}