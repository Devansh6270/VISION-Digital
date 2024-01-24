package com.vision_digital.activities;

import static com.vision_digital.activities.DashboardActivity.packageLists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vision_digital.R;
import com.vision_digital.databinding.ActivityAllPackageBinding;
import com.vision_digital.model.CoursePackage.ItemPackageAdapter;


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