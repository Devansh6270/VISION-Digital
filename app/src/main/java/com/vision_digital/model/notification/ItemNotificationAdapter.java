package com.vision_digital.model.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ItemNotificationAdapter extends RecyclerView.Adapter<ItemNotificationViewHolder> {
    List<ItemNotification> itemNotificationList;
    Context context;
     private String mobString;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ItemNotificationAdapter(List<ItemNotification> itemNotificationList, Context context , String mobString) {
        this.itemNotificationList = itemNotificationList;
        this.context = context;
        this.mobString=mobString;
    }

    @NonNull
    @Override
    public ItemNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        return new ItemNotificationViewHolder(LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_notification, parent, false));

        return new ItemNotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ItemNotificationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.notifTitle.setText(itemNotificationList.get(position).getTitle());
        holder.notifDesc.setText(itemNotificationList.get(position).getMessage());
        Glide.with(context).load(itemNotificationList.get(position).getImageUrl()).into(holder.notifImg);



        holder.clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("pushNotification").document("studentsNotification").collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document(itemNotificationList.get(position).getDocId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                notifyDataSetChanged();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error deleting document", e);
                                notifyDataSetChanged();
                            }
                        });
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemNotificationList.size();
    }
}
