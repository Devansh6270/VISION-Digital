package com.vision_classes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vision_classes.R;
import com.vision_classes.helperClasses.JSONParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TopperTwentyRegistration extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    int studId = 0;
    String uid = "";
    public static String courseId = "";
    ProgressDialog dialog;
    int a;

    //Payment Work-----------------------------------
    String orderID;

    String timeStamp;

    String price = "1";
    String mid = "IxDAFe91483847846332"; //Rajit
    String marchentKey = "1BGSS5qX5OuGWITA"; //Rajit

    Button submitButton, closeButton;
    ImageView close;
    Spinner qualificationSpinner, competitionSpinner, dateOneSpinner, dateTwoSpinner,
            dateThreeSpinner, dateFourSpinner, cityOneSpinner, cityTwoSpinner, cityThreeSpinner, cityFourSpinner;
    List<String> qualificationsList = new ArrayList<String>();
    List<String> competitionTypeList = new ArrayList<String>();
    List<String> cityList = new ArrayList<String>();

    List<String> dateList = new ArrayList<String>();

//    List<String> competitionTypeList = new ArrayList<String>();
//    List<String> competitionTypeList = new ArrayList<String>();


    EditText qualificaton, competitionType, address, dateOne, cityOne, dateTwo, cityTwo, dateThree, cityThree, dateFour, cityFour, otherQualification;
    CheckBox checkboxTerms;
    TextView txtOtherQuali;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topper_twenty_registration);
        submitButton = findViewById(R.id.submitButton);
        closeButton = findViewById(R.id.closeButton);
        close = findViewById(R.id.close);
        qualificaton = findViewById(R.id.qualification);
        qualificaton.setFocusable(false);
        otherQualification = findViewById(R.id.otherQualification);
        txtOtherQuali = findViewById(R.id.txtOtherQuali);
        competitionSpinner = findViewById(R.id.competitionTypeSpinner);
        competitionType = findViewById(R.id.competitionType);
        competitionType.setFocusable(false);
        address = findViewById(R.id.address);
        dateOne = findViewById(R.id.dateOne);
        dateOne.setFocusable(false);
        cityOne = findViewById(R.id.cityOne);
        cityOne.setFocusable(false);
        dateTwo = findViewById(R.id.dateTwo);
        dateTwo.setFocusable(false);
        cityTwo = findViewById(R.id.cityTwo);
        cityTwo.setFocusable(false);
        dateThree = findViewById(R.id.dateThree);
        dateThree.setFocusable(false);
        cityThree = findViewById(R.id.cityThree);
        cityThree.setFocusable(false);
        dateFour = findViewById(R.id.dateFour);
        dateFour.setFocusable(false);
        cityFour = findViewById(R.id.cityFour);
        cityFour.setFocusable(false);
        checkboxTerms = findViewById(R.id.checkboxTerms);
        dateOneSpinner = findViewById(R.id.dateOneSpinner);
        dateTwoSpinner = findViewById(R.id.dateTwoSpinner);
        dateThreeSpinner = findViewById(R.id.dateThreeSpinner);
        dateFourSpinner = findViewById(R.id.dateFourSpinner);
        cityOneSpinner = findViewById(R.id.cityOneSpinner);
        cityTwoSpinner = findViewById(R.id.cityTwoSpinner);
        cityThreeSpinner = findViewById(R.id.cityThreeSpinner);
        cityFourSpinner = findViewById(R.id.cityFourSpinner);

        dialog = new ProgressDialog(TopperTwentyRegistration.this);
        dialog.setMessage("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        cityList.clear();
        dateList.clear();


        qualificationSpinner = findViewById(R.id.qualificationSpinner);
        new GetTopperPageData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopperTwentyRegistration.this, DashboardActivity.class));
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TopperTwentyRegistration.this, DashboardActivity.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendUserDetailTOServerdd().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });


        /// Here spinners and edittext work have done
        qualificaton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualificationSpinner.performClick();
                qualificationSpinner.setVisibility(View.VISIBLE);
            }
        });

        qualificationsList.add("Select your Qualification");
        qualificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    qualificaton.setVisibility(View.VISIBLE);
                    qualificaton.setText(String.valueOf(qualificationSpinner.getSelectedItem()));
                    qualificationSpinner.setVisibility(View.GONE);

                    if (qualificationSpinner.getSelectedItem().equals("other")) {

                        otherQualification.setVisibility(View.VISIBLE);
                        txtOtherQuali.setVisibility(View.VISIBLE);
                    } else {
                        otherQualification.setVisibility(View.GONE);
                        txtOtherQuali.setVisibility(View.GONE);

                    }


                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        competitionType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                competitionSpinner.performClick();
                competitionSpinner.setVisibility(View.VISIBLE);
            }
        });

        competitionTypeList.add("Select Competition Type");
        competitionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    competitionType.setVisibility(View.VISIBLE);
                    competitionType.setText(String.valueOf(competitionSpinner.getSelectedItem()));
                    competitionSpinner.setVisibility(View.GONE);



                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //date spinner work start here
        dateOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateOneSpinner.performClick();
                dateOneSpinner.setVisibility(View.VISIBLE);
            }
        });
        dateList.add("Select Date");
        dateOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    dateOne.setVisibility(View.VISIBLE);
                    dateOne.setText(String.valueOf(dateOneSpinner.getSelectedItem()));
                    dateOneSpinner.setVisibility(View.GONE);
                    dateList.remove(dateOneSpinner.getSelectedItem());



                } else {


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        dateTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTwoSpinner.performClick();
                dateTwoSpinner.setVisibility(View.VISIBLE);
            }
        });

        dateTwoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    dateTwo.setVisibility(View.VISIBLE);
                    dateTwo.setText(String.valueOf(dateTwoSpinner.getSelectedItem()));
                    dateTwoSpinner.setVisibility(View.GONE);
                    dateList.remove(dateTwoSpinner.getSelectedItem());


                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dateThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateThreeSpinner.performClick();
                dateThreeSpinner.setVisibility(View.VISIBLE);
            }
        });

        dateThreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    dateThree.setVisibility(View.VISIBLE);
                    dateThree.setText(String.valueOf(dateThreeSpinner.getSelectedItem()));
                    dateThreeSpinner.setVisibility(View.GONE);
                    dateList.remove(dateThreeSpinner.getSelectedItem());



                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dateFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFourSpinner.performClick();
                dateFourSpinner.setVisibility(View.VISIBLE);
            }
        });

        dateFourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    dateFour.setVisibility(View.VISIBLE);
                    dateFour.setText(String.valueOf(dateFourSpinner.getSelectedItem()));
                    dateFourSpinner.setVisibility(View.GONE);



                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //date spinner work End here

        //city spinner work start here

        cityOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityOneSpinner.performClick();
                cityOneSpinner.setVisibility(View.VISIBLE);
            }
        });

        cityOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    cityOne.setVisibility(View.VISIBLE);
                    cityOne.setText(String.valueOf(cityOneSpinner.getSelectedItem()));
                    cityOneSpinner.setVisibility(View.GONE);


                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cityTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityTwoSpinner.performClick();
                cityTwoSpinner.setVisibility(View.VISIBLE);
            }
        });

        cityTwoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    cityTwo.setVisibility(View.VISIBLE);
                    cityTwo.setText(String.valueOf(cityTwoSpinner.getSelectedItem()));
                    cityTwoSpinner.setVisibility(View.GONE);


                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cityThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityThreeSpinner.performClick();
                cityThreeSpinner.setVisibility(View.VISIBLE);
            }
        });

        cityThreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    cityThree.setVisibility(View.VISIBLE);
                    cityThree.setText(String.valueOf(cityThreeSpinner.getSelectedItem()));
                    cityThreeSpinner.setVisibility(View.GONE);


                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cityFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityFourSpinner.performClick();
                cityFourSpinner.setVisibility(View.VISIBLE);
            }
        });

        cityFourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {

                    cityFour.setVisibility(View.VISIBLE);
                    cityFour.setText(String.valueOf(cityFourSpinner.getSelectedItem()));
                    cityFourSpinner.setVisibility(View.GONE);


                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //city spinner work End here

        /// Here spinners and edittext work have End


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Retriving user details stored in Local---------------------------------------
        SharedPreferences studDetails = getSharedPreferences("CNB", MODE_PRIVATE);
        studId = studDetails.getInt("sid", 0);
        //HardCode----
        //Catching Course Details from previous activity
        courseId = getIntent().getStringExtra("id");
        //Hardcode---

        //Paytm Payment-----------------------------------------
        String alpha = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        Long tsLong = System.currentTimeMillis() / 1000;
        timeStamp = tsLong.toString();
        orderID = alpha.charAt(random.nextInt(26)) + uid + alpha.charAt(random.nextInt(26)) + timeStamp;

        //Marchent check--------------------
        DocumentReference flagValues = FirebaseFirestore.getInstance().collection("FlagValues").document("7Mf6LOHmxLmhLrbBpB8E");

        flagValues.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        int flag = Integer.parseInt(String.valueOf(document.get("flagValue")));
                        if (flag == 1) {
                            mid = "TLvNGP42879274127615";
                            marchentKey = "G&%UEPNTnz6&4cti";
                            Log.e("Details", "Fetched");
                        }
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());

                }
            }
        });
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {

    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network gone", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onErrorProceed(String s) {

    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {

    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Log.e("checksum ", " ui fail respon  " + inErrorMessage);


    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Log.e("checksum ", " error loading pagerespon true " + inErrorMessage + "  s1 " + inFailingUrl);

    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();

    }

    class SendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(TopperTwentyRegistration.this);

        String host = "https://securegw.paytm.in/";
        String varifyurl = host + "theia/paytmCallback?ORDER_ID=" + orderID;
        String url = getApplicationContext().getString(R.string.apiURL) + "generateCheckSum";


//        String varifyurlOld = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

        String CHECKSUMHASH = "";

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(TopperTwentyRegistration.this);
            String newMKey = null;

            try {
                newMKey = URLEncoder.encode(marchentKey, String.valueOf(StandardCharsets.UTF_8));
                Log.e("New Key", newMKey);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String param =
                    "MID=" + mid +
                            "&MERCHANT_KEY=" + newMKey +
                            "&ORDER_ID=" + orderID +
                            "&CUST_ID=" + uid +
                            "&CHANNEL_ID=WAP&TXN_AMOUNT=" + Long.parseLong(price) + "&WEBSITE=DEFAULT" +
                            "&CALLBACK_URL=" + varifyurl + "&INDUSTRY_TYPE_ID=Retail";
            Log.e("params", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            // yaha per checksum ke sath order id or status receive hoga..
            Log.e("CheckSum result >>", jsonObject.toString());
            if (jsonObject != null) {
                Log.e("CheckSum result >>", jsonObject.toString());
                try {
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    CHECKSUMHASH = dataObj.has("CHECKSUMHASH") ? dataObj.getString("CHECKSUMHASH") : "";
                    Log.e("CheckSum result >>", CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ", "  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            PaytmPGService Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("MERCHANT_KEY", marchentKey);
            paramMap.put("ORDER_ID", orderID);
            paramMap.put("CUST_ID", uid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", "" + Long.parseLong(price));
            paramMap.put("WEBSITE", "DEFAULT");
            paramMap.put("CALLBACK_URL", varifyurl);

            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param " + paramMap.toString());
            Service.initialize(Order, null);
            // start payment service call here
            Service.startPaymentTransaction(TopperTwentyRegistration.this, true, true,
                    TopperTwentyRegistration.this);
        }
    }

    class GetTopperPageData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(TopperTwentyRegistration.this);
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
                    for (int i = 0; i < qualifications.length(); i++) {
                        qualificationsList.add(qualifications.getString(i));
                    }
//

                    ArrayAdapter<String> qualificationAdapter = new ArrayAdapter<String>(TopperTwentyRegistration.this,
                            android.R.layout.simple_spinner_item, qualificationsList);
                    qualificationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    qualificationSpinner.setAdapter(qualificationAdapter);


                    JSONArray competitionTypes = jsonObject.getJSONArray("competitionType");
                    for (int i = 0; i < competitionTypes.length(); i++) {
                        competitionTypeList.add(competitionTypes.getString(i));
                    }
//

                    ArrayAdapter<String> competitionTypeAdapter = new ArrayAdapter<String>(TopperTwentyRegistration.this,
                            android.R.layout.simple_spinner_item, competitionTypeList);
                    competitionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    competitionSpinner.setAdapter(competitionTypeAdapter);


                    JSONArray date = jsonObject.getJSONArray("dates");
                    for (int i = 0; i < date.length(); i++) {
                        dateList.add(date.getJSONObject(i).getString("date"));
                        JSONObject jsonObj = date.getJSONObject(i);

                        // getting inner array Ingredients
                        JSONArray ja = jsonObj.getJSONArray("city");
                        int len = ja.length();

                        for (int j = 0; j < len; j++) {
                            cityList.add(ja.getString(j));


                        }
                        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(TopperTwentyRegistration.this,
                                android.R.layout.simple_spinner_item, cityList);
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cityOneSpinner.setAdapter(cityAdapter);
                        cityTwoSpinner.setAdapter(cityAdapter);
                        cityThreeSpinner.setAdapter(cityAdapter);
                        cityFourSpinner.setAdapter(cityAdapter);


                    }
                    ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(TopperTwentyRegistration.this,
                            android.R.layout.simple_spinner_item, dateList);
                    dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dateOneSpinner.setAdapter(dateAdapter);
                    dateTwoSpinner.setAdapter(dateAdapter);
                    dateThreeSpinner.setAdapter(dateAdapter);
                    dateFourSpinner.setAdapter(dateAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
