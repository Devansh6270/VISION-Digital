package com.vision_classes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vision_classes.R;
import com.vision_classes.model.PopularCourses.ItemAllPopularCourseAdapter;

import static com.vision_classes.activities.DashboardActivity.popularCoursesList;

public class PopularCoursesActivity extends AppCompatActivity {

    //
    ItemAllPopularCourseAdapter itemPopularCoursesAdapter;
    RecyclerView popularCourseslistView;

    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_courses);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //My courses-------------
        popularCourseslistView = findViewById(R.id.popularCoursesList);
      //  LinearLayoutManager layoutManagerPopularCourses = new LinearLayoutManager(PopularCoursesActivity.this, LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager layoutManagerPopularCourses = new GridLayoutManager(this,2);
        popularCourseslistView.setLayoutManager(layoutManagerPopularCourses);
        itemPopularCoursesAdapter = new ItemAllPopularCourseAdapter(popularCoursesList);
        popularCourseslistView.setAdapter(itemPopularCoursesAdapter);
    }
}
