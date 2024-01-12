package com.vision_digital.oboardingScreen;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.vision_digital.R;
import com.vision_digital.activities.DashboardActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager viewPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button button_next;
    int position = 0;
    Button getStarted;
    Animation btnAnimation;

    String mobile= "";
    String deep = "";
    TextView skip_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        tabIndicator = findViewById(R.id.tabLayout);
        button_next = findViewById(R.id.button_next);
        skip_btn = findViewById(R.id.skip_btn);
        getStarted = findViewById(R.id.btn_get_started);
        btnAnimation = AnimationUtils.loadAnimation(IntroActivity.this,R.anim.button_animation);

        List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Doubt Solution","Pause to ask your doubts & get solutions",R.drawable.grupstudy));
        mList.add(new ScreenItem("Ai Analytics","Know where to focus while learning",R.drawable.testseriesonb));
        mList.add(new ScreenItem("Transaction","You can your every transaction after paying.",R.drawable.grouponboarding));

        viewPager = findViewById(R.id.viewPager);

        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        viewPager.setAdapter(introViewPagerAdapter);
        tabIndicator.setupWithViewPager(viewPager);


        deep = getIntent().getStringExtra("deeplinkFirebase");
        mobile = getIntent().getStringExtra("mobileNo");

        if (Build.VERSION.SDK_INT >= 32) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED)
                return;
            ActivityResultLauncher<String> launcher = registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(), isGranted -> {

                    }
            );
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("CNB", MODE_PRIVATE).edit();
                editor.putBoolean("intro", true);
                editor.apply();

                Intent intent = new Intent(IntroActivity.this, DashboardActivity.class);
                intent.putExtra("mobileNo",mobile);
                intent.putExtra("deeplinkFirebase",deep);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                finish();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewPager.getCurrentItem();
                if (position < mList.size()){
                    position++;
                    viewPager.setCurrentItem(position);
                }

                if (position==mList.size()-1){
                    loadLastScreen();
                }
            }
        });

        tabIndicator.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (position==mList.size()-1){
                    loadLastScreen();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("CNBINTRO", MODE_PRIVATE).edit();
                editor.putBoolean("intro", true);
                editor.apply();

                Intent intent = new Intent(IntroActivity.this, DashboardActivity.class);
                intent.putExtra("mobileNo",mobile);
                intent.putExtra("deeplinkFirebase",deep);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }
        });


    }

    private void loadLastScreen() {
        button_next.setVisibility(View.INVISIBLE);
        getStarted.setVisibility(View.VISIBLE);
        getStarted.setAnimation(btnAnimation);
        skip_btn.setVisibility(View.GONE);
        tabIndicator.setVisibility(View.INVISIBLE);

    }
}