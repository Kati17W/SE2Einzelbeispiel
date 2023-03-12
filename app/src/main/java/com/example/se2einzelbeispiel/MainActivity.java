package com.example.se2einzelbeispiel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    EditText editTextNumber;
    TextView textView2;
    Button button2;

    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        textView2 = (TextView) findViewById(R.id.textView2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Server server = new Server();
                new Thread(server).start();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Index index = new Index();
                new Thread(index).start();
            }
        });
    }

    class Server extends Thread {
        private String in = editTextNumber.getText().toString();
        private String out = "";

        public void run() {

            try {
                Socket s = new Socket("se2-isys.aau.at", 53212);

                DataOutputStream output = new DataOutputStream(s.getOutputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));

                if (in != null) {
                    output.writeBytes(in + '\n');
                } else {
                    Log.e("INPUT ERROR", "ERROR");
                }

                out = bufferedReader.readLine();
                s.close();

                handler.post(()->{
                    textView2.setText(out);
                });

            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }

    }

    class Index extends Thread{

    }

}
