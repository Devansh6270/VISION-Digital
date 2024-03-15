package com.vision_classes.profile;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MultipartAsyncTask extends AsyncTask<Void, Void, String> {

    private ProgressDialog progressDialog;

    private final String apiUrl;
    private final Map<String, String> params;
    private final File imageFile;

    Context context;

    public MultipartAsyncTask(String apiUrl, Map<String, String> params, File imageFile , Context context) {
        this.apiUrl = apiUrl;
        this.params = params;
        this.imageFile = imageFile;
        this.context=context;
    }
    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressDialog != null) {
            progressDialog.show();
        }


    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            MultipartFormData multipartFormData = new MultipartFormData(apiUrl,context);
            multipartFormData.addParams(params);
            multipartFormData.addFile("image", imageFile);
            return multipartFormData.send();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
        if (response != null) {
            // Handle request success
        } else {
            // Handle request failure
        }
    }

    private static class MultipartFormData {
        private final String apiUrl;
        private final Map<String, String> params;
        private final Map<String, File> files;
        Context context;



        public MultipartFormData(String apiUrl , Context context) {
            this.apiUrl = apiUrl;
            this.params = new HashMap<>();
            this.files = new HashMap<>();
            this.context=context;
        }

        public void addParams(Map<String, String> params) {
            this.params.putAll(params);
        }

        public void addFile(String key, File file) {
            files.put(key, file);
        }

        public String send() throws IOException {
            OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

// Add parameters
            for (Map.Entry<String, String> entry : params.entrySet()) {
                requestBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }

// Add files
            for (Map.Entry<String, File> entry : files.entrySet()) {
                File file = entry.getValue();
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
                requestBodyBuilder.addFormDataPart(entry.getKey(), file.getName(), fileBody);
            }

            RequestBody requestBody = requestBodyBuilder.build();

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .addHeader("Authorization", "44b48f2305bf26234") // Add authorization header
                    .post(requestBody)
                    .build();


            Response response = client.newCall(request).execute();
           // String responseBody = null;

            if (response.isSuccessful()){
                //Toast.makeText(context, "Profile Image is Uploading!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "send: "+ response.body().string());
            }else {
                Log.e(TAG, "send: "+ response.body().string());
            }



            // you can commit out for checking response
//            if (response.isSuccessful()) {
//                Log.e(TAG, "send: "+ response.body().string());
//                responseBody = response.body().string();
//
//              //  return response.body().string()+"";
//                response.close();
//                return responseBody;
//            } else {
//                Log.e(TAG, "send: "+ response.body().string());
//                return null;
//           }

            return null;
        }
    }
}
