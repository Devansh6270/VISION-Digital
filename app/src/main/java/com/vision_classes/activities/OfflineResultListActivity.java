package com.vision_classes.activities;

import static com.vision_classes.activities.DashboardActivity.offlineResultListArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vision_classes.R;
import com.vision_classes.model.offlineResult.ItemOfflineResultAdapter;

public class OfflineResultListActivity extends AppCompatActivity {


    RecyclerView offlineResultList;
    ImageView backBtn;
    TextView noOfflineResultText;
    ItemOfflineResultAdapter offlineResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_result_list);

        offlineResultList=findViewById(R.id.rvOfflineResult);
        backBtn=findViewById(R.id.ic_back);
        noOfflineResultText=findViewById(R.id.noOfflineRltTxt);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (offlineResultListArrayList.size()==0){
            noOfflineResultText.setVisibility(View.VISIBLE);
        }else{
            noOfflineResultText.setVisibility(View.GONE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(OfflineResultListActivity.this, RecyclerView.VERTICAL, false);
        offlineResultList.setLayoutManager(layoutManager);
        offlineResultAdapter = new ItemOfflineResultAdapter(OfflineResultListActivity.this, offlineResultListArrayList);
        offlineResultList.setAdapter(offlineResultAdapter);


    }
}