package com.neml.receivesocketdatafromactivemqserver;

import android.content.Intent;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import androidx.annotation.Nullable;

public class IntentServiceDemo extends android.app.IntentService {
    static Socket clientSocket = null;
    static DataInputStream din = null;
    private static IntentServiceDemo intentServiceDemo;

    public IntentServiceDemo() {
        super("Service name");
    }

    public static IntentServiceDemo getInstance(){
        if (intentServiceDemo == null){
            intentServiceDemo = new IntentServiceDemo();
        }
        return intentServiceDemo;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            clientSocket = new Socket("192.168.43.125", 3000);
            clientSocket.setKeepAlive(true);
            clientSocket.setSoTimeout(0);
            din = new DataInputStream(clientSocket.getInputStream());
            int i = 0;
            while(true) {
                String  str= din.readUTF();
                System.out.println("message received = "+ Integer.valueOf(i) + " = "+str);
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

    }
}
