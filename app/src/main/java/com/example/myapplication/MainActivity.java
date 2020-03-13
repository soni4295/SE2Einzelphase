package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private EditText editMatrikelNr;
    private TextView serverResponse;
    private TextView calculateResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editMatrikelNr = (EditText) findViewById(R.id.MatikelNrTxt);
        serverResponse = (TextView) findViewById(R.id.AntwortServerTxt);
        calculateResult = (TextView) findViewById(R.id.BerechnungTxt);
    }

    public void send(View v) {
        Message messageSender = new Message();
        messageSender.execute(editMatrikelNr.getText().toString());
    }

    class Message extends AsyncTask<String, Void, String> {
        private Socket s;
        private DataOutputStream dos;
        private BufferedReader br;
        private String receivedMessage;

        @Override
        protected String doInBackground(String... voids) {
            String message = voids[0];
            try {
                s = new Socket("se2-isys.aau.at", 53212);
                //s = new Socket("192.168.8.122", 7800);
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeBytes(message + '\n');
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                receivedMessage = br.readLine();
                System.out.println("receivedMessage " + receivedMessage);
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receivedMessage;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            serverResponse.setText(result);
        }
    }
    public void berechnen (View v) {
        //01653947 mod 7 = Aufgabe 1 --> 0 1 6 5 3 9 4 7 --> 0 a 6 e 3 i 4 g
        String matNr = editMatrikelNr.getText().toString();
        char [] numbers = matNr.toCharArray();
        int asciitable = 97;
        for (int i = 1; i < numbers.length; i++) {
            if (i % 2 != 0) {
                switch (numbers[i]) {
                    case '0': numbers[i] = (char) (asciitable + 10); break;
                    case '1': numbers[i] = (char) asciitable; break; //97 = a
                    case '2': numbers[i] = (char) (asciitable + 1) ; break; //98 = b
                    case '3': numbers[i] = (char) (asciitable + 2) ; break; //99 = c
                    case '4': numbers[i] = (char) (asciitable + 3) ; break; //100 = d
                    case '5': numbers[i] = (char) (asciitable + 4) ; break; //101 = e
                    case '6': numbers[i] = (char) (asciitable + 5) ; break; //102 = f
                    case '7': numbers[i] = (char) (asciitable + 6) ; break; //103 = g
                    case '8': numbers[i] = (char) (asciitable + 7) ; break; //104 = h
                    case '9': numbers[i] = (char) (asciitable + 8) ; break; //106 = i
                }
            }
        }
        String result = Arrays.toString(numbers);
        calculateResult.setText(result.replace("[","").replace("]",""));
    }

}
