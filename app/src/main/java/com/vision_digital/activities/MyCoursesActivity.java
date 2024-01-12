package com.vision_digital.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.vision_digital.R;
import com.vision_digital.model.myCourses.ItemMyAllCourseAdapter;

import static com.vision_digital.activities.DashboardActivity.myCoursesList;

public class MyCoursesActivity extends AppCompatActivity {

    //My Course work-------------------------------------------
//    ItemMyCourseAdapter itemMyCourseAdapter;
//    RecyclerView myCourseslistView;

    ItemMyAllCourseAdapter itemMyAllCourseAdapter;
    RecyclerView myCourseslistView;

    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //My courses-------------
//        myCourseslistView = findViewById(R.id.myCoursesList);
//        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        myCourseslistView.setLayoutManager(layoutManager);
//        itemMyCourseAdapter = new ItemMyCourseAdapter(myCoursesList);
//        myCourseslistView.setAdapter(itemMyCourseAdapter);

        myCourseslistView = findViewById(R.id.myCoursesList);
       // LinearLayoutManager layoutManager = new LinearLayoutManager(MyCoursesActivity.this, LinearLayoutManager.VERTICAL, false);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        myCourseslistView.setLayoutManager(layoutManager);
        itemMyAllCourseAdapter = new ItemMyAllCourseAdapter(myCoursesList);
        myCourseslistView.setAdapter(itemMyAllCourseAdapter);

    }
}
