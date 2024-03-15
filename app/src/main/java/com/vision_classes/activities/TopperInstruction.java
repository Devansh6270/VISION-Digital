package com.vision_classes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vision_classes.R;
import com.vision_classes.helperClasses.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopperInstruction extends AppCompatActivity {
    Button registration;
    ProgressDialog dialog;
    TextView instructiontxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topper_instruction);
        registration =findViewById(R.id.registration);
        instructiontxt = findViewById(R.id.instructiontxt);

        dialog = new ProgressDialog(TopperInstruction.this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        new GetTopperInstructionsPageData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopperInstruction.this,TopperTwentyRegistration.class));
            }
        });
    }
    class GetTopperInstructionsPageData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TopperInstruction.this);
            String param = "";

            JSONObject jsonObject = jsonParser.makeHttpRequest(getApplicationContext().getString(R.string.apiURL) + "getParmanuRegData", "POST", param);
            if (jsonObject != null) {
                Log.e("test-response", jsonObject.toString());
                return jsonObject.toString();

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            Log.i("json", s);

            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    Log.e("Result : ", s);

                    //Do work-----------------------------
                    JSONArray qualifications = jsonObject.getJSONArray("qualification");

                    String status = jsonObject.getString("text");
                    instructiontxt.setText(Html.fromHtml(jsonObject.getString("text")));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}