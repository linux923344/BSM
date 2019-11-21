package com.zadanie1.mw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FormPassword extends Activity {
    private static final String FILE_NAME = "passwd.txt";
    private static String input;
    private static String newpass;

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String unSHA512(String passwordToHash)
    {
        String generatedPassword = null;
        String salt = "Z7F4nGZEYa2AZQ3TVTrgYXJkezWPzDuGh54zVrjWtkGQLQNPFN5edJWvzh5rVgXbY5fv489JHourDWHMTKEumk24e6MzmwC8rCTPCHgKvWCnCjPG4HN3mLuWjWG3yabF";
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
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

                input = currentpassword.getText().toString();
                newpass = newpassword.getText().toString();
                String SHAinput = unSHA512(input);
                currentpassword.setText("");
                String password = null;

                try {
                    password = readFile(filename).toString();
                    if(SHAinput.equals(password)){
                        if (newpassword.getText().toString().equals(renewpassword.getText().toString())) {

                            String text = SHA512(newpassword.getText().toString());
                            FileOutputStream fos = null;

                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Password has been changed",
                                    Toast.LENGTH_SHORT);
                            toast.show();


                            Intent myIntent3 = new Intent(FormPassword.this,
                                    Noto.class);
                            startActivity(myIntent3);
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
    public static String getInput() {
        return input;
    }
    public static String getInputNew() {
        return newpass;
    }
}