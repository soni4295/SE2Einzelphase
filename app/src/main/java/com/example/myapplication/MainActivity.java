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
}
