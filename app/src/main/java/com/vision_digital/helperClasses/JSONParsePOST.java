package com.vision_digital.helperClasses;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParsePOST {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    HttpURLConnection urlConnection = null;
    // variable to hold context
    private Context context;

    // constructor
    public JSONParsePOST(Context context) {
        this.context = context;
    }

    public JSONObject makeHttpRequest(String url, String params) {
        try {
            String retSrc = "";
            URL url1 = new URL(url);
            // Check for request method
            HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setRequestProperty("Authorization", "44b48f2305bf26234");

            // Request method is POST
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setFixedLengthStreamingMode(params.getBytes().length);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Authorization", "44b48f2305bf26234");

            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(params);
            out.close();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            byte[] bytes = new byte[10000];
            StringBuilder x = new StringBuilder();
            int numRead = 0;
            while ((numRead = in.read(bytes)) >= 0) {
                x.append(new String(bytes, 0, numRead));
            }
            retSrc = x.toString();
            Log.e("json parse ", " the value is " + retSrc);
            jObj = new JSONObject(retSrc);
        } catch (Exception e) {
            e.printStackTrace();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    // Toast.makeText(context, "Connectivity issue. Please try again later.", Toast.LENGTH_LONG).show();
                }
            });
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jObj;
    }
}

