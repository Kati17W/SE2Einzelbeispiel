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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    Button sendButton;
    EditText matrikleNrText;
    TextView serverResponse;
    TextView indexPrint;


    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = (Button) findViewById(R.id.button);
        matrikleNrText = (EditText) findViewById(R.id.editTextNumber);
        serverResponse = (TextView) findViewById(R.id.textView2);
        indexPrint = (TextView) findViewById(R.id.textView3);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printGGTIndex();
                Server server = new Server();
                new Thread(server).start();
            }
        });

    }

    private void printGGTIndex() {
        final String matrikelNr = matrikleNrText.getText().toString();
        final List<Integer> matrikelNrList = new ArrayList<>();

        for (String nummer : matrikelNr.split("")) {
            matrikelNrList.add(Integer.parseInt(nummer));
        }

        for (int i = 0; i < matrikelNrList.size() - 1; i++) {
            for (int j = i + 1; j < matrikelNrList.size(); j++) {
                List<Integer> sublist = matrikelNrList.subList(i, j+1);
                int gcd = gcdList(sublist); if (gcd > 1) {
                    indexPrint.setText("Numbers at indexes " + i + ", " + j);
                }
            }
        }

    }
    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }    else {
            return gcd(b, a % b);
        }
    }

    public static int gcdList(List<Integer> list) {
        int result = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            result = gcd(result, list.get(i));
        }
        return result;
    }

    class Server extends Thread {
        private String in = matrikleNrText.getText().toString();
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
                    serverResponse.setText(out);
                });

            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }

    }

}
