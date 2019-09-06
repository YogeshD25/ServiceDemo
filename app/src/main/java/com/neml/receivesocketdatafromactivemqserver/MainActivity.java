package com.neml.receivesocketdatafromactivemqserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    Button button;
    static Socket s = null;
    static Socket clientSocket = null;
    static DataInputStream din = null;
    static String globalString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToServer();
              //  Intent intent = new Intent(MainActivity.this, IntentServiceDemo.class);
               // startService(intent);
                       }
    });
    }

    private void connectToServer() {
        new Network().execute();
        Toast.makeText(this, "Message From ActiveMQ Server: "+globalString, Toast.LENGTH_SHORT).show();
    }
    public  class Network extends AsyncTask<Object,Object,Object> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected void onPostExecute(String o) {
            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                clientSocket = new Socket("172.22.22.64", 3000);
                clientSocket.setKeepAlive(true);
                clientSocket.setSoTimeout(0);
                din = new DataInputStream(clientSocket.getInputStream());
                int i = 0;
                while(true) {
                    String  str= din.readUTF();
                    System.out.println("message received = "+ Integer.valueOf(i) + " = "+str);
                     globalString = str;
                    i++;
                }
            } catch (UnknownHostException e) {
                try {
                    din.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
