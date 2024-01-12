package com.vision_digital.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.vision_digital.R;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.searchResults.ItemSearch;
import com.vision_digital.model.searchResults.ItemSearchAdapter;
import com.vision_digital.searchPage.CategorySearchAdapter;
import com.vision_digital.searchPage.CategorySearchModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    String getSearchElementURL = "";
    EditText searchEt;
    String searchKey;
    ProgressDialog dialog;

    RecyclerView categorySearchRecycler;
    CategorySearchAdapter categorySearchAdapter;
    ArrayList<CategorySearchModel> searchModelList = new ArrayList<>();

    public static RecyclerView topResultList, otherResultList, coursesList, teachersList;
    LinearLayout topResultLayout, otherResultLayout, courseResultLayout, teacherResultLayout, category_layout;
    TextView noItemFound;
    ImageView backBtn, clearBtn;
    TextView topResultsfor;

    ArrayList<ItemSearch> topResultsArrayList = new ArrayList<>();
    ItemSearchAdapter topResultAdapter;
    ArrayList<ItemSearch> otherResultsArrayList = new ArrayList<>();
    ItemSearchAdapter otherResultAdapter;
    ArrayList<ItemSearch> courseResultsArrayList = new ArrayList<>();
    ItemSearchAdapter courseResultAdapter;
    ArrayList<ItemSearch> teacherResultsArrayList = new ArrayList<>();
    ItemSearchAdapter teacherResultAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(R.layout.activity_search);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);


        getSearchElementURL = getApplicationContext().getString(R.string.apiURL) + "getSearchResultData";

        topResultList = findViewById(R.id.topResultList);
        topResultLayout = findViewById(R.id.topResults);
        topResultLayout.setVisibility(View.GONE);
        otherResultList = findViewById(R.id.otherResultList);
        otherResultLayout = findViewById(R.id.otherResults);
        otherResultLayout.setVisibility(View.GONE);
        coursesList = findViewById(R.id.coursesList);
        courseResultLayout = findViewById(R.id.courseResults);
        courseResultLayout.setVisibility(View.GONE);
        teachersList = findViewById(R.id.teachersList);
        teacherResultLayout = findViewById(R.id.teachersResults);
        teacherResultLayout.setVisibility(View.GONE);
        category_layout = findViewById(R.id.category_layout);
        category_layout.setVisibility(View.GONE);
        categorySearchRecycler = findViewById(R.id.category_recyclerView);





        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categorySearchRecycler.setLayoutManager(linearLayoutManager);

//        List<CategorySearchModel> searchModelList = new ArrayList<CategorySearchModel>();


//        topResultsfor = findViewById(R.id.topResultsfor);

        noItemFound = findViewById(R.id.noItemFound);

        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEt.setText("");
            }
        });


        searchEt = findViewById(R.id.searchET);
        searchEt.requestFocus();
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void performSearch() {
        topResultsArrayList.clear();
        otherResultsArrayList.clear();
        teacherResultsArrayList.clear();
        courseResultsArrayList.clear();
        searchModelList.clear();
        searchKey = searchEt.getText().toString().trim();
        if (!searchKey.equals("")) {
            hideKeyboard(SearchActivity.this);
            dialog = new ProgressDialog(SearchActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading");
            dialog.show();
//            topResultsfor.setText("TOP RESULTS for \"" + searchKey + "\":");

            new GetSearchElement().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(this, "Please enter the keyword.", Toast.LENGTH_SHORT).show();
        }
    }

    class GetSearchElement extends AsyncTask<String, Void, String> {
//         private ProgressDialog dialog = new ProgressDialog(SearchActivity.this);

        @Override
        protected void onPreExecute() {
//                        this.dialog.setMessage("Please wait");
//                        this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(SearchActivity.this);

            String param = "search_word=" + searchKey;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(getSearchElementURL, "POST", param);
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
                    final JSONObject dataObj = jsonObject.getJSONObject("data");
                    switch (status) {
                        case "success":
                            //Running Fine
                            topResultsArrayList.clear();
                            otherResultsArrayList.clear();
                            teacherResultsArrayList.clear();
                            courseResultsArrayList.clear();
                            searchModelList.clear();
                            String dataFound = dataObj.getString("data_found");
                            if (dataFound.equals("yes")) {
                                noItemFound.setVisibility(View.GONE);
                                topResultList.setVisibility(View.VISIBLE);
                                JSONObject searchReult = dataObj.getJSONObject("search_result");

                                JSONArray mileStoneResults = searchReult.getJSONArray("milestone");
                                if (!(mileStoneResults.length() == 0)) {
                                    for (int i = 0; i < mileStoneResults.length(); i++) {
                                        ItemSearch itemSearch = new ItemSearch();
                                        JSONObject topResultObj = mileStoneResults.getJSONObject(i);
                                        itemSearch.setTitle(topResultObj.getString("title"));
                                        itemSearch.setDuration(topResultObj.getString("duration"));
                                        itemSearch.setImageUrl(topResultObj.getString("image"));
                                        itemSearch.setTitle(topResultObj.getString("title"));
                                        itemSearch.setOwnedBy(topResultObj.getString("owner_name"));
                                        itemSearch.setId(topResultObj.getString("id"));
                                        itemSearch.setType("milestone");
                                        itemSearch.setCid(topResultObj.getString("cid"));
                                        itemSearch.setDescription(topResultObj.getString("description"));
                                        topResultsArrayList.add(itemSearch);

//                                        if (!topResultObj.getString("type").equals("school")) {
//                                        } else {
//
//                                        }
                                    }
                                    searchModelList.add(new CategorySearchModel("Milestone", true));

                                    Log.e("milestonelist", String.valueOf(topResultsArrayList));

                                    LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
                                    topResultList.setLayoutManager(layoutManager);
                                    topResultAdapter = new ItemSearchAdapter(topResultsArrayList);
                                    topResultList.setAdapter(topResultAdapter);
                                    topResultLayout.setVisibility(View.VISIBLE);
                                    topResultAdapter.notifyDataSetChanged();

                                } else {
                                    topResultLayout.setVisibility(View.GONE);
                                }
                                JSONArray courseResults = searchReult.getJSONArray("course");
                                if (!(courseResults.length() == 0)) {
                                    for (int i = 0; i < courseResults.length(); i++) {
                                        ItemSearch itemSearch = new ItemSearch();
                                        JSONObject courseResultsJSONObject = courseResults.getJSONObject(i);
                                        itemSearch.setTitle(courseResultsJSONObject.getString("title"));
                                        itemSearch.setDuration(courseResultsJSONObject.getString("duration"));
                                        itemSearch.setImageUrl(courseResultsJSONObject.getString("image"));
                                        itemSearch.setOwnedBy(courseResultsJSONObject.getString("owner_name"));
                                        itemSearch.setId(courseResultsJSONObject.getString("id"));
                                        itemSearch.setDescription("");
                                        itemSearch.setType("course");
                                        itemSearch.setCid("");
                                        courseResultsArrayList.add(itemSearch);
                                    }
                                    if (!(mileStoneResults.length() == 0)) {
                                        searchModelList.add(new CategorySearchModel("Course", false));

                                    } else {
                                        searchModelList.add(new CategorySearchModel("Course", true));

                                    }

                                    Log.e("courseList", String.valueOf(courseResultsArrayList));
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
                                    coursesList.setLayoutManager(layoutManager);
                                    courseResultAdapter = new ItemSearchAdapter(courseResultsArrayList);
                                    coursesList.setAdapter(courseResultAdapter);
                                    courseResultLayout.setVisibility(View.VISIBLE);
                                    courseResultAdapter.notifyDataSetChanged();
                                } else {
                                    courseResultLayout.setVisibility(View.GONE);
                                }
                                JSONArray teacherResults = searchReult.getJSONArray("teacher");
                                if (!(teacherResults.length() == 0)) {
                                    for (int i = 0; i < teacherResults.length(); i++) {
                                        ItemSearch itemSearch = new ItemSearch();
                                        JSONObject teacherResultObj = teacherResults.getJSONObject(i);
                                        itemSearch.setTitle(teacherResultObj.getString("name"));
                                        itemSearch.setImageUrl(teacherResultObj.getString("image"));
                                        itemSearch.setOwnedBy(teacherResultObj.getString("institute"));
                                        itemSearch.setId(teacherResultObj.getString("id"));
                                        itemSearch.setDescription("");
                                        itemSearch.setDuration("");
                                        itemSearch.setType("teacher");
                                        itemSearch.setCid("");

                                        teacherResultsArrayList.add(itemSearch);
                                    }
                                    if (!(mileStoneResults.length() == 0) && !(courseResults.length() == 0)) {
                                        searchModelList.add(new CategorySearchModel("Teacher", false));


                                    } else {
                                        searchModelList.add(new CategorySearchModel("Teacher", true));

                                    }


                                    Log.e("Teachers List", String.valueOf(teacherResultsArrayList));
                                    categorySearchAdapter = new CategorySearchAdapter(searchModelList);
                                    categorySearchRecycler.setAdapter(categorySearchAdapter);
                                    category_layout.setVisibility(View.VISIBLE);
                                    categorySearchAdapter.notifyDataSetChanged();

                                    LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false);
                                    teachersList.setLayoutManager(layoutManager);
                                    teacherResultAdapter = new ItemSearchAdapter(teacherResultsArrayList);
                                    teachersList.setAdapter(teacherResultAdapter);
                                    teacherResultLayout.setVisibility(View.VISIBLE);
                                    teacherResultAdapter.notifyDataSetChanged();
                                } else {
                                    teacherResultLayout.setVisibility(View.GONE);
                                }


                            } else if (dataFound.equals("no")) {
                                category_layout.setVisibility(View.GONE);
                                topResultList.setVisibility(View.GONE);
                                noItemFound.setVisibility(View.VISIBLE);
                                otherResultList.setVisibility(View.GONE);
                                teachersList.setVisibility(View.GONE);
                                coursesList.setVisibility(View.GONE);



                            }
                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SearchActivity.this);
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = SearchActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
                            String msgContent = dataObj.getString("message");
                            maintainanceContent.setText(Html.fromHtml(msgContent));

                            TextView btnOK = dialogView.findViewById(R.id.btnOK);
                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finishAndRemoveTask();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            break;
                        case "failure":
                            Toast.makeText(SearchActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(SearchActivity.this, "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
            dialog.dismiss();
        }
    }
}
