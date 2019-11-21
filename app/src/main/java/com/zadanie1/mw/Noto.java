package com.zadanie1.mw;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Noto extends AppCompatActivity {
    EditText ChangeNote;
    Button SaveNote;
    private static final String FILE_NAME = "notes.txt";
    private static String keyStreach;
    private static String key;
    private static String initVector;
    protected static String password;

    public String keyStreaching(String pass) throws NoSuchAlgorithmException {
        String salt = "NkAeUcRvdBwnrLVC4kbSwzTCeLBzZjxA2puRCvq47i5Le3Qanmxv2C2Q3REn77YHGpCqt8yjgFiiyV8HHRPSaLzzZBC8WHVwas8RTiau79wKnk5Q4zkp2EVq5jNwh3bd";

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] byteData;
        StringBuilder sb = new StringBuilder(64);
        for (int a = 0; a < 100000; a++) {
            md.update((pass + salt).getBytes());
            byteData = md.digest();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            key = sb.toString();
            sb.delete(0, sb.length());
        }

        return key;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noto);

        ChangeNote = (EditText) findViewById(R.id.changeNote);
        SaveNote = (Button) findViewById(R.id.SaveNote);
        FileInputStream fis = null;
        password = ((password = MainActivity.getInput()) != null) ? password : FormPassword.getInput();

        try {
            FileInputStream fileInputStream = null;
            File file = new File(getFilesDir() + "/" + FILE_NAME);
            byte[] ciphertext = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(ciphertext);

            ChangeNote.setText(ciphertext.toString());

            keyStreach = keyStreaching(password);
            key = keyStreach.substring(4, 20);
            initVector = keyStreach.substring(7, 23);

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
            password = ((password =  FormPassword.getInputNew()) != null) ? password : MainActivity.getInput();
            keyStreach = keyStreaching(password);
            key = keyStreach.substring(4, 20);
            initVector = keyStreach.substring(7, 23);

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
