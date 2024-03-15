package com.vision_classes.activities;

import static com.vision_classes.activities.DashboardActivity.popularTeacherList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vision_classes.R;
import com.vision_classes.model.popularTeachers.ItemTeacherAdapter;

public class AllPopularTeachers extends AppCompatActivity {
    //My Course work-------------------------------------------
    ItemTeacherAdapter itemTeachersAdapter;
    RecyclerView popularTeachers;

    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_popular_teachers);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Teachers-------------
        popularTeachers = findViewById(R.id.popularTeachersList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AllPopularTeachers.this, LinearLayoutManager.VERTICAL, false);

        popularTeachers.setLayoutManager(layoutManager);
        itemTeachersAdapter = new ItemTeacherAdapter(popularTeacherList);
        popularTeachers.setAdapter(itemTeachersAdapter);

    }
}