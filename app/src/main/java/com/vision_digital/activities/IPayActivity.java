package com.vision_digital.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vision_digital.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class IPayActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipay);

        webView = findViewById(R.id.webView);

        webView.clearCache(true);
        configureWebView();
        webView.getSettings().setJavaScriptEnabled(true);

        Random rand = new Random();
        int num = rand.nextInt(90000000) + 10000000;
        String ranOrderId = "IPAY" + num;
        ranOrderId = getIntent().getStringExtra("order_id");
        Log.e(TAG, "onCreate: ranOrderId========================================================"+ranOrderId);


       // String payId = "1009100504145404";
        String payId = "6791510724204211";
        String payMode = "UPI/NB/NONUPI";
        String orderId = ranOrderId;
        String amount = "100";
        amount = getIntent().getStringExtra("amount");
        Log.e(TAG, "onCreate: amount========================================================"+amount);

        String txnType = "SALE";
        String custName = "DEMO USER";
//        custName = getIntent().getStringExtra("cust_name");
        Log.e(TAG, "onCreate: custName========================================================"+custName);

        String custStreetAddress1 = "Gurgaon";
        String custZip = "122016";
        String custPhone = "9959999999";
        custPhone = getIntent().getStringExtra("mobile");
        Log.e(TAG, "onCreate: custPhone========================================================"+custPhone);


        String custEmail = "devyadav@gmail.com";
        String custId = "1234628";
        custId = getIntent().getStringExtra("sid");
        Log.e(TAG, "onCreate: custId========================================================"+custId);


        String productDesc = "Demo Transaction";
        productDesc = getIntent().getStringExtra("product_desc");
        Log.e(TAG, "onCreate: productDesc========================================================"+productDesc);


        String currencyCode = "356";
//        String returnUrl1 = "https://uat.irctcipay.com/pgui/jsp/response.jsp";
        String returnUrl1 = "https://irctc.chalksnboard.com/api/v2/payment/ipay";

     // String url = "https://uat.irctcipay.com/pgui/jsp/paymentrequest";
        String url = "https://www.irctcipay.com/pgui/jsp/paymentrequest";

        Log.e(TAG, "returnUrl1: " + returnUrl1);
        // Call the HashGenerator code

        String input = "AMOUNT=" + amount + "~CURRENCY_CODE=" + currencyCode + "~CUST_EMAIL=" + custEmail + "~CUST_ID=" + custId + "~CUST_NAME=" + custName + "~CUST_PHONE=" + custPhone + "~CUST_STREET_ADDRESS1=" + custStreetAddress1 + "~CUST_ZIP=" + custZip + "~ORDER_ID=" + orderId + "~PAY_ID=" + payId + "~PAY_MODE=" + payMode + "~PRODUCT_DESC=" + productDesc + "~RETURN_URL=" + returnUrl1 + "~TXNTYPE=" + txnType + "2ed1c52072f34631";  // test salt "c3a640590ed74a6b"  //2ed1c52072f34631 original salt

        Log.e(TAG, "input: " + input);

        String hexHash = generateSHA256Hash(input);
        String hashKey = hexHash.toUpperCase();
        Log.e("HashGenerator", "SHA-256 Hash: " + hexHash.toUpperCase());
        Log.e("HashGenerator", "SHA-256 Hash length: " + hexHash.length());

        String postData = "PAY_ID=" + payId + "&PAY_MODE=" + payMode + "&ORDER_ID=" + orderId + "&AMOUNT=" + amount + "&TXNTYPE=" + txnType + "&CUST_NAME=" + custName + "&CUST_STREET_ADDRESS1=" + custStreetAddress1 + "&CUST_ZIP=" + custZip + "&CUST_PHONE=" + custPhone + "&CUST_EMAIL=" + custEmail + "&CUST_ID=" + custId + "&PRODUCT_DESC=" + productDesc + "&CURRENCY_CODE=" + currencyCode + "&RETURN_URL=" + returnUrl1 + "&HASH=" + hashKey;
        Log.e(TAG, "postData: " + postData);
        webView.postUrl(url, postData.getBytes());

    }

    private void configureWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("TAG", "view: dcodedJsonResponse==========================================" + view.getUrl());
                if (url != null && url.equals("https://irctc.chalksnboard.com/api/v2/payment/ipay")) {

                    view.evaluateJavascript("document.documentElement.innerText", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            // Handle the received value (text content of the page)
                            Uri uri = Uri.parse(value);
                            Log.d("TAG", "urlreturn: dcodedJsonResponse========================================" + url);
                            String infoValue = uri.getQueryParameter("info");
                            Log.e("WebView", "onReceiveValue12334455666==========================: " + value);

                            getAllResponseDateFromJson(decodeBase64(infoValue));
                        }

                    });

                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Print the request URL
                String url = request.getUrl().toString();

                Log.d("WebView", "Request URL=============================: " + request.getUrl().toString());
                // Check if the URL matches the specific page you want to extract text from
                if (url.equals("https://irctc.chalksnboard.com/payment/ipay")) {
                    // Get the HTML content of the page
                    view.evaluateJavascript("document.documentElement.innerText", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            // Handle the received value (text content of the page)
                            String extractedText = value.replaceAll("\"", "");
                            Log.e("WebView", "onReceiveValue==========================: " + value);
                            Log.e("WebView", "extractedText========================: " + extractedText);
                            // Do something with the extracted text
                            // ...
                        }
                    });
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

        });
    }

    private void getAllResponseDateFromJson(String s) {
        try {
            JSONObject responseJson = new JSONObject(s);
            Intent intent = new Intent();
            intent.putExtra("raw_response",s);
            intent.putExtra("status", responseJson.getString("STATUS"));
//            intent.putExtra("orderNo", responseJson.getString(""));
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    private String decodeBase64(String info) {
        // Decode the Base64 string
        byte[] decodedBytes = Base64.decode(info, Base64.DEFAULT);
        String decodedString = new String(decodedBytes);

        // Print the decoded text
        System.out.println(decodedString);
        Log.e("TAG", "onCreate  decodedString===========================================: "+decodedString);
        return decodedString;

    }

    private String generateSHA256Hash(String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] encodedHash = digest.digest(input.getBytes());
        return bytesToHex(encodedHash);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    
}