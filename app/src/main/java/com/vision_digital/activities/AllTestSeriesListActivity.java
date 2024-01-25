package com.vision_digital.activities;

import static com.vision_digital.activities.DashboardActivity.packageLists;
import static com.vision_digital.activities.DashboardActivity.testSeriesList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.vision_digital.R;
import com.vision_digital.TestSeries.model.dashboardTestSeries.DashTestSeriesAdapter;
import com.vision_digital.databinding.ActivityAllTestSeriesListBinding;
import com.vision_digital.model.CoursePackage.ItemPackageAdapter;

public class AllTestSeriesListActivity extends AppCompatActivity {

    ActivityAllTestSeriesListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding= DataBindingUtil.setContentView(this,R.layout.activity_all_test_series_list);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(AllTestSeriesListActivity.this, LinearLayoutManager.VERTICAL, false);

        binding.testSeriesList.setLayoutManager(layoutManager);
        DashTestSeriesAdapter dashTestSeriesAdapter = new DashTestSeriesAdapter(testSeriesList,AllTestSeriesListActivity.this);
        binding.testSeriesList.setAdapter(dashTestSeriesAdapter);




    }
}