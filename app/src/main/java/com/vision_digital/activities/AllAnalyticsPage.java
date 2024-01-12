package com.vision_digital.activities;

import static com.vision_digital.activities.DashboardActivity.courseAnalytics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vision_digital.R;
import com.vision_digital.model.analytics.ItemAllAnalyticsAdapter;
import com.vision_digital.model.analytics.ItemAnalytics;

import java.util.ArrayList;

public class AllAnalyticsPage extends AppCompatActivity {

    //My Course work-------------------------------------------
  //  ItemMyCourseAdapter itemMyCourseAdapter;
//    ItemAnalyticsAdapter itemAnalyticsAdapter;
//    RecyclerView myAnalyticsListView;
//
//
//    ArrayList<ItemAnalytics> analytics;


    ItemAllAnalyticsAdapter itemAnalyticsAdapter;
    RecyclerView myAnalyticsListView;


    ArrayList<ItemAnalytics> analytics;

    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_analytics_page);


        backBtn = findViewById(R.id.backBtnAnalytics);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

//        myAnalyticsListView = findViewById(R.id.myAnalyticsList);
//        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        myAnalyticsListView.setLayoutManager(layoutManager);
//
//        itemAnalyticsAdapter = new ItemAnalyticsAdapter(getApplicationContext(),courseAnalytics);
//        myAnalyticsListView.setAdapter(itemAnalyticsAdapter);

        myAnalyticsListView = findViewById(R.id.myAnalyticsList);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        myAnalyticsListView.setLayoutManager(layoutManager);

        itemAnalyticsAdapter = new ItemAllAnalyticsAdapter(getApplicationContext(),courseAnalytics);
        myAnalyticsListView.setAdapter(itemAnalyticsAdapter);

        }
}