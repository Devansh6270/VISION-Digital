package com.vision_classes.TestSeries.model.TestSeriesAvailabilityBundle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_classes.R;
import com.vision_classes.TestSeries.AllTestPageActivity;

import java.util.ArrayList;

public class ItemTestSeriesAvailabilityAdapter extends RecyclerView.Adapter<ItemTestSeriesAvailabilityAdapter.ViewHolder> {
    Context context;
    ArrayList<ItemTestSeriesAvailabilityList> itemTestSeriesAvailabilityLists;

    public ItemTestSeriesAvailabilityAdapter(Context context, ArrayList<ItemTestSeriesAvailabilityList> itemTestSeriesAvailabilityLists){
        this.context = context;
        this.itemTestSeriesAvailabilityLists = itemTestSeriesAvailabilityLists;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_series_availability, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        Glide.with(context).load(itemTestSeriesAvailabilityLists.get(position).getCategoryImage()).into( holder.ivImageTitle);
        holder.tvTitle.setText(itemTestSeriesAvailabilityLists.get(position).getTitle());
        holder.tvNoOfTest.setText(itemTestSeriesAvailabilityLists.get(position).getTotal_test_count());
        holder.tvStartTime.setText(itemTestSeriesAvailabilityLists.get(position).getStartTime());
        holder.tvEndTime.setText(itemTestSeriesAvailabilityLists.get(position).getEndTime());
        holder.tvActualPrice.setText(itemTestSeriesAvailabilityLists.get(position).getActualPrice());
        holder.tvSellingPrice.setText(itemTestSeriesAvailabilityLists.get(position).getSellingPrice());

      //  holder.btnTestSchedule.setText(itemTestSeriesAvailabilityLists.get(position).getTestSchedulePDFURl());
        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AllTestPageActivity.class);
                i.putExtra("id",itemTestSeriesAvailabilityLists.get(position).getId());
                i.putExtra("subscriptionValidity",itemTestSeriesAvailabilityLists.get(position).getSubscriptionValidity());
                context.startActivity(i);
            }
        });

//   This is the demo pdf to test the pdf is working or not. and in urlOfPdf we are fetching data from getPackageCourseTestseriesList api
//        String urlOfPdf = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
        String urlOfPdf = itemTestSeriesAvailabilityLists.get(position).getTestSchedulePDFURl();
        if (urlOfPdf.equals("")){
            holder.btnTestSchedule.setVisibility(View.INVISIBLE);
        } else {
            holder.btnTestSchedule.setVisibility(View.VISIBLE);
            holder.btnTestSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openPdfInBrowser(urlOfPdf);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return itemTestSeriesAvailabilityLists.size() != 0 ? itemTestSeriesAvailabilityLists.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvNoOfTest, tvStartTime, tvEndTime, tvActualPrice, tvSellingPrice, btnTestSchedule, btnViewDetails;
        ImageView ivImageTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageTitle = itemView.findViewById(R.id.ivImageTitle);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvNoOfTest = itemView.findViewById(R.id.tvNoOfTest);
            tvStartTime = itemView.findViewById(R.id.tvTestStartTime);
            tvEndTime = itemView.findViewById(R.id.tvTestEndTime);
            tvActualPrice = itemView.findViewById(R.id.tvActualPrice);
            tvSellingPrice = itemView.findViewById(R.id.tvSellingPrice);
            btnTestSchedule = itemView.findViewById(R.id.btnTestSchedule);
            btnViewDetails = itemView.findViewById(R.id.viewDetailsBtn);
        }
    }

    private void openPdfInBrowser(String pdfUrl) {
        // Create an Intent to open the PDF in a browser
        Intent intentPdf = new Intent(Intent.ACTION_VIEW);
        intentPdf.setData(Uri.parse(pdfUrl));

        try {
            context.startActivity(intentPdf);
        } catch (Exception e) {
            // Handle exceptions, such as the browser not being available or the URL being invalid
            Toast.makeText(context, "Document will be uploaded soon", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
