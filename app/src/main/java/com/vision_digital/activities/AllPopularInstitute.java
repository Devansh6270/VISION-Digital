package com.vision_digital.activities;

import static com.vision_digital.activities.DashboardActivity.popularInstitutesList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vision_digital.R;
import com.vision_digital.model.popularTeachers.ItemTeacherAdapter;

public class AllPopularInstitute extends AppCompatActivity {
    ItemTeacherAdapter itemInstituteAdapter;
    RecyclerView popularInstitute;

    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_popular_institute);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Institute-------------
        popularInstitute = findViewById(R.id.popularInstituteList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AllPopularInstitute.this, LinearLayoutManager.VERTICAL, false);

        popularInstitute.setLayoutManager(layoutManager);
        itemInstituteAdapter = new ItemTeacherAdapter(popularInstitutesList);
        popularInstitute.setAdapter(itemInstituteAdapter);

    }
}