package com.zadanie1.mw;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity {
    TextView response;
    Button button;
    Button restartpassword;
    private EditText textPassword;
    private View viewPassword;
    private static final String filename = "passwd.txt";
    private static String input;

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

    private boolean isFileExists(File file) {
        return file.exists() && !file.isDirectory();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String unSHA512(String passwordToHash)
    {
        String generatedPassword = null;
        String salt = "cnsakn";
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


    private Handler handler = new Handler();

    private Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String filename =  getFilesDir() + "/passwd.txt";
        File file = new File(filename);

        if (!isFileExists(file)){
            Intent myIntent3 = new Intent(MainActivity.this,
                    FirstPassword.class);
            startActivity(myIntent3);
        }
        else { showBiometricPrompt();
        }


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

            @Override
            public void onClick(View view) {
                File file = new File(filename);

                try {
                    input = textPassword.getText().toString();
                    String SHAinput = unSHA512(input);
                    textPassword.setText("");
                    String password = readFile(filename).toString();

                    if (isFileExists(file)) {

                        if(SHAinput.equals(password)){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Password is correct!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            Intent myIntent2 = new Intent(MainActivity.this,
                                    Noto.class);
                            startActivity(myIntent2);

                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Password is NOT correct",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        Intent myIntent3 = new Intent(MainActivity.this,
                                FirstPassword.class);
                        startActivity(myIntent3);
                    }

                }  catch(IOException e){
                    e.printStackTrace();
                }
                textPassword.getText().clear();
            }
        });

    }
    public static String getInput() {
        return input;
    }


    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo =
                new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                BiometricPrompt.CryptoObject authenticatedCryptoObject =
                        result.getCryptoObject();
                // User has verified the signature, cipher, or message
                // authentication code (MAC) associated with the crypto object,
                // so you can use it in your app's crypto-driven workflows.
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
                try {
                    Thread.sleep(1000);
                    System.exit(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        // Displays the "log in" prompt.
        biometricPrompt.authenticate(promptInfo);
    }


}