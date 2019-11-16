package com.zadanie1.mw;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Noto extends AppCompatActivity {
    EditText ChangeNote;
    Button SaveNote;
    private static final String FILE_NAME = "notes.txt";

    static String text;
    private EditText textInputNotes;
    private static String keyMultiply;
    private static String key;
    private static String initVector;
    protected static String password;
    protected static String soil= "yWaDPPsFMXxXwEJsTcf42";
    int pom=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noto);

        ChangeNote = (EditText) findViewById(R.id.changeNote);
        SaveNote = (Button) findViewById(R.id.SaveNote);
        FileInputStream fis = null;
        password = MainActivity.getInput();

        try {
            FileInputStream fileInputStream = null;
            File file = new File(getFilesDir() + "/" + FILE_NAME);
            byte[] ciphertext = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(ciphertext);

            ChangeNote.setText(ciphertext.toString());

            keyMultiply = password + soil + password + soil + soil + password + soil;
            key = keyMultiply.substring(4, 20);
            initVector = keyMultiply.substring(7, 23);

            byte[] bytesKey = key.getBytes("UTF-8");
            byte[] bytesIV = initVector.getBytes("UTF-8");
            IvParameterSpec iv = new IvParameterSpec(bytesIV);
            SecretKeySpec sKeySpec = new SecretKeySpec(bytesKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
            String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
            ChangeNote.setText(plaintext);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void save (View v) throws IOException {

        try {
        String text = ChangeNote.getText().toString();
        FileOutputStream fos = null;

        keyMultiply = password + soil + password + soil + soil + password + soil;
        key = keyMultiply.substring(4, 20);
        initVector = keyMultiply.substring(7, 23);
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(text.getBytes());

        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
        fos.write(encrypted);

        Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
}
