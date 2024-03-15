package com.vision_classes.activities;

import static com.vision_classes.activities.DashboardActivity.testSeriesList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.vision_classes.R;
import com.vision_classes.TestSeries.model.dashboardTestSeries.DashTestSeriesAdapter;
import com.vision_classes.databinding.ActivityAllTestSeriesListBinding;

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

        if (testSeriesList.size()==0){
            binding.tvNoTestSeries.setVisibility(View.VISIBLE);
            binding.testSeriesList.setVisibility(View.GONE);
            binding.lottieEmptyBox.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoTestSeries.setVisibility(View.GONE);
            binding.testSeriesList.setVisibility(View.VISIBLE);
            binding.lottieEmptyBox.setVisibility(View.GONE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(AllTestSeriesListActivity.this, LinearLayoutManager.VERTICAL, false);

            binding.testSeriesList.setLayoutManager(layoutManager);
            DashTestSeriesAdapter dashTestSeriesAdapter = new DashTestSeriesAdapter(testSeriesList,AllTestSeriesListActivity.this);
            binding.testSeriesList.setAdapter(dashTestSeriesAdapter);
        }






    }
}