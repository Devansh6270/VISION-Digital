package com.vision_classes.activities;

import static com.vision_classes.activities.DashboardActivity.packageLists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.vision_classes.R;
import com.vision_classes.databinding.ActivityAllPackageBinding;
import com.vision_classes.model.CoursePackage.ItemPackageAdapter;


public class AllPackageActivity extends AppCompatActivity {

    ItemPackageAdapter itemPackageAdapter;

    ActivityAllPackageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_all_package);




        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Teachers-------------

        LinearLayoutManager layoutManager = new LinearLayoutManager(AllPackageActivity.this, LinearLayoutManager.VERTICAL, false);

        binding.packagesList.setLayoutManager(layoutManager);
        itemPackageAdapter = new ItemPackageAdapter(packageLists,AllPackageActivity.this);
        binding.packagesList.setAdapter(itemPackageAdapter);
    }
}