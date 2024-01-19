package com.vision_digital.TestSeries;

import static android.content.ContentValues.TAG;
import static com.vision_digital.activities.DashboardActivity.sid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vision_digital.R;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SubjectivePaperDetailActivity extends AppCompatActivity {

    ImageView btnBack;
    String uploadAnswerSheet;
    String subjectivePaperDetailUrl, submitTestDataSubjective;
    ProgressDialog dialog;
    TextView tvNameOfTest, btnViewTestPaper, btnUploadAnswer, btnResult, btnSolution;
    String testId, testSeriesId;
    File pdfFile;

    private static final int PICK_PDF_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjective_paper_detail);

        subjectivePaperDetailUrl = getApplicationContext().getString(R.string.apiURL) + "getTestSubjectiveTestDetails";
        submitTestDataSubjective = getApplicationContext().getString(R.string.apiURL) + "submitTestDataSubjective";
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");

        testId = getIntent().getStringExtra("testId");
        testSeriesId = getIntent().getStringExtra("testSeriesId");


        btnBack = findViewById(R.id.backBtn);
        tvNameOfTest = findViewById(R.id.tvNameOfTest);
        btnViewTestPaper = findViewById(R.id.btnViewTestPaper);
        btnUploadAnswer = findViewById(R.id.btnUploadAnswer);
        btnResult = findViewById(R.id.btnResult);
        btnSolution = findViewById(R.id.btnSoln);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnUploadAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYesNoDialog();
            }
        });

        new GetTestSubjectiveTestDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void showYesNoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to upload answer sheet?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pickPDFFile();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openPdfInBrowser(String pdfUrl) {
        // Create an Intent to open the PDF in a browser
        Intent intentPdf = new Intent(Intent.ACTION_VIEW);
        intentPdf.setData(Uri.parse(pdfUrl));

        try {
            startActivity(intentPdf);
        } catch (Exception e) {
            // Handle exceptions, such as the browser not being available or the URL being invalid
            Toast.makeText(SubjectivePaperDetailActivity.this, "Document will be uploaded soon", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    class GetTestSubjectiveTestDetails extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser(SubjectivePaperDetailActivity.this);


            Log.e(TAG, "SubjectivePaperDetail-----: " + "do in background");

            String param = "sid=" + sid + "&test_id=" + testId + "&testseries_id=" + testSeriesId;

            Log.e(TAG, "param:------ " + param);


            JSONObject jsonObject = jsonParser.makeHttpRequest(subjectivePaperDetailUrl, "POST", param);
            if (jsonObject != null) {
                Log.e("SubjectiveDetail------", jsonObject.toString());
                return jsonObject.toString();

            } else {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("json", s);
            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    Log.e("Subjective Detail : ", s);

                    //Do work-----------------------------
                    String status = jsonObject.getString("status");
//
                    switch (status) {
                        case "success":
                            dialog.dismiss();

                            JSONObject dataObject = jsonObject.getJSONObject("data");

                            String id = String.valueOf(dataObject.getInt("id"));
                            String title = dataObject.getString("title");
                            String statusActive = dataObject.getString("status");
                            String test_paper_type = dataObject.getString("test_paper_type");
                            String question_pdf = dataObject.getString("question_pdf");
                            String solution = dataObject.getString("solution");
                            String result = dataObject.getString("result");
                            String submission = dataObject.getString("submission");

                            tvNameOfTest.setText(title);
                            btnViewTestPaper.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openPdfInBrowser(question_pdf);
                                }
                            });

                            btnResult.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openPdfInBrowser(result);
                                }
                            });

                            btnSolution.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openPdfInBrowser(solution);
                                }
                            });



                            break;
                        case "maintainance":
                            Toast.makeText(SubjectivePaperDetailActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        case "failure":
                            Toast.makeText(SubjectivePaperDetailActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void pickPDFFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri pdfUri = data.getData();
                if (pdfUri != null) {
                    // Process the selected PDF file
                    try {
                        processPdf(pdfUri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(this, "Failed to pick PDF file", Toast.LENGTH_SHORT).show();
                }



                  //  String param = "sid=" + sid + "&test_id=" + testId + "&testseries_id=" + testId;

                Map<String, String> params = new HashMap<>();
                params.put("sid", String.valueOf(sid));
                params.put("test_id", String.valueOf(testId));
                params.put("testseries_id", String.valueOf(testSeriesId));

                    Log.e(TAG, "param:------ " + params);
                    PdfUploadTask pdfUploadTask = new PdfUploadTask(submitTestDataSubjective, params, pdfFile, SubjectivePaperDetailActivity.this);
                    pdfUploadTask.execute();
                  //  new UploadTestSubjective().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        }


    private void processPdf(Uri pdfUri) throws IOException {
        // Load PDF file and handle it
         pdfFile = createPdfFile();
        copyUriToFile(pdfUri, pdfFile);
        // Now you have the PDF file (pdfFile) to work with
        // ...
    }

    private File createPdfFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        return File.createTempFile("PDF_" + timeStamp + "_", ".pdf", storageDir);
    }

    private void copyUriToFile(Uri uri, File destinationFile) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    class UploadTestSubjective extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
//            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser(SubjectivePaperDetailActivity.this);


            Log.e(TAG, "SubjectivePaperDetail-----: " + "do in background");

            String param = "sid=" + sid + "&test_id=" + testId + "&testseries_id=" + testSeriesId + "&test_upload=" + uploadAnswerSheet;

            Log.e(TAG, "param:------ " + param);


            JSONObject jsonObject = jsonParser.makeHttpRequest(submitTestDataSubjective, "POST", param);
            if (jsonObject != null) {
                Log.e("SubjectiveDetail------", jsonObject.toString());
                Log.e("RESPONSE", jsonObject.toString());
                return jsonObject.toString();

            } else {

                Log.e("RESPONSE", "EMPTY VALUE IS PASSING");
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("json", s);
            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    Log.e("Upload Detail : ", s);

                    //Do work-----------------------------
                    String status = jsonObject.getString("status");
//
                    switch (status) {
                        case "success":
//                            dialog.dismiss();

                            String message = jsonObject.getString("message");
                            Log.e("TAG", "SUCCESS");

                            Toast.makeText(SubjectivePaperDetailActivity.this, message, Toast.LENGTH_SHORT).show();
//                            btnUploadAnswer.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    pickPDFFile();
//                                }
//                            });
                            break;
                        case "maintainance":
                            Log.e("TAG", "MAINTENANCE");
                            Toast.makeText(SubjectivePaperDetailActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        case "failure":
                            Log.e("TAG", "FAILURE");
                            Toast.makeText(SubjectivePaperDetailActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

//    class UploadTestSubjective extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            // dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            JSONParser jsonParser = new JSONParser(SubjectivePaperDetailActivity.this);
//
//            Log.e(TAG, "SubjectivePaperDetail-----: " + "do in background");
//
//            // Create a new instance of DataOutputStream to write the file data
//            DataOutputStream dos = null;
//
//            try {
//                String param = "sid=" + sid + "&test_id=" + testId + "&testseries_id=" + testSeriesId;
//
//                Log.e(TAG, "param:------ " + param);
//
//                // Create a new HttpURLConnection
////                HttpURLConnection connection = jsonParser.openConnection(submitTestDataSubjective, "POST");
//                URL url = new URL(submitTestDataSubjective);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("POST");
//
//                // Enable input and output streams
//                connection.setDoInput(true);
//                connection.setDoOutput(true);
//
//                // Create a multipart form data boundary
//                String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
//                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//
//                // Create a new DataOutputStream for the connection output stream
//                dos = new DataOutputStream(connection.getOutputStream());
//
//                // Write parameters to the output stream
//                dos.writeBytes(param);
//
//                // Write file data to the output stream
//                dos.writeBytes("--" + boundary + "\r\n");
//                dos.writeBytes("Content-Disposition: form-data; name=\"test_upload\"; filename=\"" + uploadAnswerSheet + "\"\r\n");
//                dos.writeBytes("Content-Type: application/pdf\r\n\r\n");
//
//                // Read the file and write it to the output stream
//                FileInputStream fileInputStream = new FileInputStream(uploadAnswerSheet);
//                int bytesRead;
//                byte[] buffer = new byte[1024];
//                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
//                    dos.write(buffer, 0, bytesRead);
//                }
//                fileInputStream.close();
//
//                // End the multipart request
//                dos.writeBytes("\r\n--" + boundary + "--\r\n");
//
//                // Flush and close the output stream
//                dos.flush();
//                dos.close();
//
//                // Get the response code
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    // Read the response from the input stream
//                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    String inputLine;
//                    StringBuilder response = new StringBuilder();
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    in.close();
//
//                    // Return the response
//                    Log.e("RESPONSE UPLOAD", response.toString());
//                    return response.toString();
//                } else {
//                    // Handle the error case
//                    Log.e("RESPONSE UPLOAD", "EMPTY DATA IS PASSING");
//                    return "";
//                }
//            } catch (Exception e) {
//                Log.e("RESPONSE UPLOAD", "EMPTY DATA IS PASSING");
//                e.printStackTrace();
//                return "";
//            } finally {
//                // Close the DataOutputStream
//                if (dos != null) {
//                    try {
//                        dos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.i("json", s);
//            if (!s.equals("")) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(s);
//
//                    Log.e("Upload Detail : ", s);
//
//                    // Do work-----------------------------
//                    String status = jsonObject.getString("status");
//                    switch (status) {
//                        case "success":
//                            // dialog.dismiss();
//                            String message = jsonObject.getString("message");
//                            Log.e("TAG", "SUCCESS");
//                            Toast.makeText(SubjectivePaperDetailActivity.this, message, Toast.LENGTH_SHORT).show();
//                            break;
//                        case "maintainance":
//                            Log.e("TAG", "MAINTENANCE");
//                            Toast.makeText(SubjectivePaperDetailActivity.this, "Something went wrong. Contact support over the website", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "failure":
//                            Log.e("TAG", "FAILURE");
//                            Toast.makeText(SubjectivePaperDetailActivity.this, "Something went wrong. Contact support over the website", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                } catch (JSONException e) {
//                    Log.e("Upload Detail Catch : ", s);
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}






