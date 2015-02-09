package com.shivamdev.servicesbroadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class MainActivity extends ActionBarActivity {

    static final String URL = "https://www.newthinktank.com/wordpress/lotr.txt";
    static final String TAG = MainActivity.class.getSimpleName();

    EditText etDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etDownload = (EditText) findViewById(R.id.downloadedEditText);
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(FileService.TRANSACTION_DONE);

        registerReceiver(downloadReceiver, intentFilter);
    }

    public void startFileService(View view) {

        Intent intent = new Intent(this, FileService.class);
        intent.putExtra("url", URL);
        this.startService(intent);
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            L.l(TAG, "Service Received");

            showFileContents();
        }
    };

    public void showFileContents() {
        StringBuilder sb;

        FileInputStream fis = null;

        try {
            fis = this.openFileInput("myFile");

            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");

            BufferedReader br = new BufferedReader(isr);

            sb = new StringBuilder();

            String line;

            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            etDownload.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
