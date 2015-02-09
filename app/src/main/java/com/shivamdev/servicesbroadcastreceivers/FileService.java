package com.shivamdev.servicesbroadcastreceivers;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shivam.chopra on 10-02-2015.
 */
public class FileService extends IntentService {

    public static final String TRANSACTION_DONE = "com.shivamdev.TRANSACTION_DONE";
    public static final String TAG = FileService.class.getSimpleName();
    public FileService() {
        super(FileService.class.getName());
    }

    public FileService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        L.l(TAG, "Service Started");

        String passedURL = intent.getStringExtra("url");

        downloadFile(passedURL);

        L.l(TAG, "Service Stopped");

        Intent myIntent = new Intent(TRANSACTION_DONE);

        FileService.this.sendBroadcast(myIntent);
    }

    protected void downloadFile(String passedURL) {

        String fileName = "myFile";
        FileOutputStream os = null;

        try{
            os = openFileOutput(fileName, Context.MODE_PRIVATE);

            URL fileURL = new URL(passedURL);

            HttpURLConnection urlConn = (HttpURLConnection) fileURL.openConnection();

            urlConn.setRequestMethod("GET");

            urlConn.setDoOutput(true);

            urlConn.connect();

            InputStream is = urlConn.getInputStream();

            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while((bufferLength = is.read(buffer)) > 0) {
                os.write(buffer, 0, bufferLength);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.flush();
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
