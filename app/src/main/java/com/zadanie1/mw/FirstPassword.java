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

public class FirstPassword extends Activity {
    private static final String FILE_NAME = "passwd.txt";
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