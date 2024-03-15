package com.vision_classes.activities;

import static com.vision_classes.activities.DashboardActivity.liveClassList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.vision_classes.R;
import com.vision_classes.databinding.ActivityAllLiveClassBinding;
import com.vision_classes.model.liveClass.Dash.ItemDashLiveClassAdapter;

public class AllLiveClassActivity extends AppCompatActivity {

    ItemDashLiveClassAdapter adapter;
    ActivityAllLiveClassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_all_live_class);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(AllLiveClassActivity.this, LinearLayoutManager.VERTICAL, false);

        if (liveClassList.size()==0){
            binding.tvNoLiveClasses.setVisibility(View.VISIBLE);
            binding.liveClassList.setVisibility(View.GONE);
            binding.lottieEmptyBox.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoLiveClasses.setVisibility(View.GONE);
            binding.lottieEmptyBox.setVisibility(View.GONE);
            binding.liveClassList.setVisibility(View.VISIBLE);

            binding.liveClassList.setLayoutManager(layoutManager);
            adapter = new ItemDashLiveClassAdapter(liveClassList,AllLiveClassActivity.this);
            binding.liveClassList.setAdapter(adapter);
        }




    }
}