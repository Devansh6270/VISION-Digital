package com.vision_digital.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.vision_digital.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PdfRenderActivity extends AppCompatActivity {
    PDFView pdfView;
    String pdfurl;
    ImageView backBtn;
    public static final float DEFAULT_MAX_SCALE = 3.0f;
    public static final float DEFAULT_MID_SCALE = 1.75f;
    public static final float DEFAULT_MIN_SCALE = 1.0f;

    private float minZoom = DEFAULT_MIN_SCALE;
    private float midZoom = DEFAULT_MID_SCALE;
    private float maxZoom = DEFAULT_MAX_SCALE;
    ProgressDialog progressDialog;

    private long timeLimit = 7;
    final Handler timer = new Handler(Looper.getMainLooper());

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_pdf_render);

        progressDialog = new ProgressDialog(PdfRenderActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        pdfurl = getIntent().getStringExtra("pdfLink");

        pdfView = findViewById(R.id.idPDFView);
        backBtn = findViewById(R.id.backBtn);
        pdfView.enableDoubletap(true);
        pdfView.enableAntialiasing(true);
        new RetrivePDFfromUrl().execute(pdfurl);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfView.setMaxZoom(maxZoom);
            }
        });

        timer();
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void timer() {
        final Runnable runnableTimer = new Runnable() {
            @Override
            public void run() {
                timeLimit--;
                Log.e("timmer", "" + timeLimit);

                if (timeLimit != 0) {
                    timer.postDelayed(this, 1000);
                }
                if (timeLimit == 0) {
                    // Submit test result
                    progressDialog.dismiss();
                    timer.removeCallbacksAndMessages(null);

                }
            }
        };
        timer.postDelayed(runnableTimer, 1000);
    }
}