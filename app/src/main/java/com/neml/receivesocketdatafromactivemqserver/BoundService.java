package com.neml.receivesocketdatafromactivemqserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import androidx.annotation.Nullable;

public class BoundService  extends Service {
    static Socket clientSocket = null;
    static DataInputStream din = null;

    private class DisplayOnUiThread extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(getString(R.string.service_demo_tag),"Message intercepted");
            switch (msg.what){

            }
            super.handleMessage(msg);
        }
    }

    class MyServiceBinder extends Binder {
        public BoundService getService(){
            return BoundService.this;
        }
    }

    private IBinder mBinder=new MyServiceBinder();

    private Messenger suceesMessenger=new Messenger(new DisplayOnUiThread());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(getString(R.string.service_demo_tag),"In OnBind");
        if(intent.getPackage()== getApplicationContext().getPackageName()){
            return suceesMessenger.getBinder();
        }else{
            return mBinder;
        }
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i(getString(R.string.service_demo_tag),"In OnReBind");
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSocketConnection();
        Log.i(getString(R.string.service_demo_tag),"Service Destroyed");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(getString(R.string.service_demo_tag),"In onStartCommend, thread id: "+Thread.currentThread().getId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                startSocketConnection();
            }
        }).start();
        return START_STICKY;
    }

    private void startSocketConnection(){
        while (true){
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

    private void stopSocketConnection(){
        try{
            clientSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(getString(R.string.service_demo_tag),"In onUnbind");
        return super.onUnbind(intent);
    }
}
