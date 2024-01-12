package com.vision_digital.model.Certificate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.helperClasses.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.ViewHolder> {

    Context context;
    ArrayList<CertificateModel> certificateModelArrayList;

    String courseId = "";

    String courseName="";
    int studId;

    private Activity activity;
    private Fragment fragment;

    private static final int REQUEST_PERMISSION = 1;
    private static String PDF_URL = "https://example.com/sample.pdf";
    private static final String PDF_FILE_NAME = "IRCTC-i-Prepare.pdf";



    public CertificateAdapter(Context context, ArrayList<CertificateModel> certificateModelArrayList) {
        this.context = context;
        this.certificateModelArrayList = certificateModelArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.item_certificate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.subjectName.setText(certificateModelArrayList.get(position).getSubjectName());
        courseName=certificateModelArrayList.get(position).getSubjectName();

        Glide.with(context).load(certificateModelArrayList.
                get(position).getImage()).into(holder.subjectImage);


        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseId = String.valueOf(certificateModelArrayList.get(position).getId());


                new downloadCertificate().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //Toast.makeText(context, "Certificate Not Generated Yet !", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return certificateModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView subjectName;
        ImageView subjectImage, downloadBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectName = itemView.findViewById(R.id.subjectTitle
            );
            subjectImage = itemView.findViewById(R.id.subjectImg);
            downloadBtn = itemView.findViewById(R.id.downloadImgBtn);

        }
    }









    public class downloadCertificate extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser(context);

            studId = DashboardActivity.sid;
            String param = "sid=" + studId + "&course_id=" + courseId;

            String url = "https://irctc.chalksnboard.com/api/v2/certificate-download";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            if (jsonObject != null) {
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

                    Log.e("Result : ", s);

                    //Do work-----------------------------
                    String status = jsonObject.getString("status");
                    String url = jsonObject.getString("url");

                    PDF_URL=url;


                    switch (status) {
                        case "success":

                                startDownload();

                            break;

                        case "maintenance":
//
                            break;
                        case "failure":
                            Toast.makeText(context.getApplicationContext(), "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private void startDownload() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(PDF_URL), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);

        PackageManager packageManager =context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);

        if (activities.size() > 0) {
            context.startActivity(intent);
        } else {
            // No PDF viewer app found, open the link in a web browser
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PDF_URL));
            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(webIntent);
        }

//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(PDF_URL));
//        request.setTitle("Downloading PDF");
//        request.setDescription("Please wait...");
//
//        // Set the destination folder and file name
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,courseName +courseId+"SID000"+studId+ PDF_FILE_NAME);
//
//        // Get the download service and enqueue the request
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        long downloadId = downloadManager.enqueue(request);
//
//        // Optionally, you can listen for download completion using a BroadcastReceiver
//        // and handle any post-download actions.
//        // Here's a sample code snippet to register the receiver:
//        // IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        // registerReceiver(downloadReceiver, filter);
//
//        Toast.makeText(context, "PDF download started", Toast.LENGTH_SHORT).show();
    }

}
