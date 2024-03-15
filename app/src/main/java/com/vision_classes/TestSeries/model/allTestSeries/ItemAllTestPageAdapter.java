package com.vision_classes.TestSeries.model.allTestSeries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_classes.R;
import com.vision_classes.TestSeries.OngoingTestActivity;
import com.vision_classes.TestSeries.model.testResultNew.NewResultActivity;
//import com.vision_digital.activities.payment.PaymentActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ItemAllTestPageAdapter extends RecyclerView.Adapter<ItemAllTestPageAdapter.ViewHolder> {
    Context context;
    ArrayList<ItemAllTestPageList> arrayList;


    public ItemAllTestPageAdapter(Context context, ArrayList<ItemAllTestPageList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_all_test_page, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // You can also format the date if needed
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String presentDate = dateFormat.format(currentDate);

        holder.tvTestName.setText(arrayList.get(position).getTestName());
        holder.tvTotalQuestion.setText(arrayList.get(position).getTotalQuestions());
        holder.tvTotalTime.setText(arrayList.get(position).getTotalTime());
        holder.tvTotalMarks.setText(arrayList.get(position).getTotalMarks());
        holder.tvStart.setText(arrayList.get(position).getResult());

//        if (arrayList.get(position).getResult().equals("Start")){
//            holder.tvStart.setTextColor(Color.GREEN);
//        } else if (arrayList.get(position).getResult().equals("Result")){
//            holder.tvStart.setTextColor(R.color.mahroon);
//        }


        String buyStatus = arrayList.get(position).getBuy_status();
        String buyButton = arrayList.get(position).getBuy_button();

        if (arrayList.get(position).getLock().equals("1") && arrayList.get(position).buy_status.equals("0")) {
            holder.tvStart.setVisibility(View.GONE);
            holder.ivImageLock.setVisibility(View.VISIBLE);
        } else if (arrayList.get(position).getLock().equals("1") && arrayList.get(position).buy_status.equals("1")) {
            holder.ivImageLock.setVisibility(View.GONE);
            holder.tvStart.setVisibility(View.VISIBLE);
        } else if (arrayList.get(position).getLock().equals("1") && arrayList.get(position).buy_status.equals("2")) {
            holder.ivImageLock.setVisibility(View.GONE);
            holder.tvStart.setVisibility(View.VISIBLE);
        } else if (arrayList.get(position).getLock().equals("0")) {
            holder.ivImageLock.setVisibility(View.GONE);
            holder.tvStart.setVisibility(View.VISIBLE);
        }

        holder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Buy Status", buyStatus + "  BUY BUTTON---" + buyButton);

                if (buyStatus.equals("2")) {
                    Intent i = new Intent(context, NewResultActivity.class);
                    i.putExtra("id", arrayList.get(position).getTestListId());
                    context.startActivity(i);
                } else {
//                    Intent intent;
                    if (buyButton == "0") {
//                        Intent intent = new Intent(context, PaymentActivity.class);
//                        intent.putExtra("id", arrayList.get(position).getTestListId());
//                        intent.putExtra("price", arrayList.get(position).getPrice());
//                        intent.putExtra("title", arrayList.get(position).getTestName());
//                        intent.putExtra("courseType", "testseries");
//                        ((Activity) context).finish();
//                        context.startActivity(intent);
                    } else {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                        Date presentDate = new Date();

                        try {
                            Date apiDate = dateFormat.parse(arrayList.get(position).getStartTimeTestList());

                            if (presentDate.after(apiDate) || presentDate.equals(apiDate)) {
                                // Run your program here
                                Intent intent = new Intent(context, OngoingTestActivity.class);
                                intent.putExtra("id", arrayList.get(position).getTestListId());
                                intent.putExtra("price", arrayList.get(position).getPrice());
                                intent.putExtra("title", arrayList.get(position).getTestName());
                                intent.putExtra("subscriptionValidity", "6");
                                context.startActivity(intent);
                            } else {
                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                                String extractedDate = outputFormat.format(apiDate);
                                Toast.makeText(context, "Your test will start on " + extractedDate, Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
//                    context.startActivity(intent);
                }

            }
        });

        holder.ivImageLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Please subscribe the test", Toast.LENGTH_SHORT).show();
            }
        });


// ========= Trying to pass data from TestSeriesDashboardActivity -> AllTestPageActivity -> TestDetailsActivity
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, TestDetailsActivity.class);
//                intent.putExtra("desc", myTestBundleList.get(position).getDescription());
//                intent.putExtra("price", myTestBundleList.get(position).getPrice());
//                intent.putExtra("testType", myTestBundleList.get(position).getTestType());
//                intent.putExtra("id", myTestBundleList.get(position).getId());
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size() != 0 ? arrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTestName, tvTotalQuestion, tvTotalTime, tvTotalMarks, tvStart;
        ImageView ivImageLock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTestName = itemView.findViewById(R.id.tvTestName);
            tvTotalQuestion = itemView.findViewById(R.id.tvTotalQuestion);
            tvTotalMarks = itemView.findViewById(R.id.tvTotalMarks);
            tvTotalTime = itemView.findViewById(R.id.tvTotalTime);
            tvStart = itemView.findViewById(R.id.tvStart);

            ivImageLock = itemView.findViewById(R.id.ivImageLock);

        }
    }
}