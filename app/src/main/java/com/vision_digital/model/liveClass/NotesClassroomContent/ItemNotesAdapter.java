package com.vision_digital.model.liveClass.NotesClassroomContent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.imageview.ShapeableImageView;
import com.vision_digital.R;

import java.util.ArrayList;

public class ItemNotesAdapter extends RecyclerView.Adapter<ItemNotesAdapter.ViewHolder> {

    Context context;
    ArrayList<ItemNotesList> itemNotesLists;

    public ItemNotesAdapter(Context context, ArrayList<ItemNotesList> itemNotesLists){
        this.context = context;
        this.itemNotesLists = itemNotesLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes_classroom_content, parent, false);




        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.tvNotesTitle.setText(itemNotesLists.get(position).getNotesTitle());


        holder.ivDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemNotesLists.get(position).isIs_locked()){
                    Toast.makeText(context, "Please subscribe the course", Toast.LENGTH_SHORT).show();
                } else {
                    String notesUrl = itemNotesLists.get(position).getUrl();
                    openPdfInBrowser(notesUrl);
                }
            }
        });


    }

    @Override
    public int getItemCount() {

        return itemNotesLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivBook, ivDownload;
        TextView tvNotesTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBook = itemView.findViewById(R.id.ivBook);
            tvNotesTitle = itemView.findViewById(R.id.tvNotesTitle);
            ivDownload = itemView.findViewById(R.id.ivDownload);

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
