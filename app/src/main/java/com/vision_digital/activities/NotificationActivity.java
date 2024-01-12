package com.vision_digital.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.model.notification.ItemNotification;
import com.vision_digital.model.notification.ItemNotificationViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView notifList;
    FirestoreRecyclerAdapter notifAdapter;

    Context context;
    ImageView backBtn;
    ImageButton clearAllBtn;
    String mobString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SharedPreferences uidDetails = getSharedPreferences("CNBUID", MODE_PRIVATE);
        String  uid = uidDetails.getString("uid", "NO_NAME");

        SharedPreferences studDetails = getSharedPreferences("CNBMOBILE", MODE_PRIVATE);
        mobString = studDetails.getString("mobileNo", "NO_NAME");
        Log.e("mobString",mobString);

        clearAllBtn = findViewById(R.id.clearAll);
        clearAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);

                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to delete all the notification?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        db.collection("pushNotification").document("studentsNotification").collection(mobString)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("TAG", document.getId() + " => " + document.getData());
                                                db.collection("pushNotification").document("studentsNotification").collection(mobString).document(document.getId()).delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("TAG", "Error deleting document", e);
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.d("TAG", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });


                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        notifList = findViewById(R.id.notifList);
        final Query query = db.collection("pushNotification").document("studentsNotification").collection(mobString);
        final FirestoreRecyclerOptions<ItemNotification> options = new FirestoreRecyclerOptions.Builder<ItemNotification>().setQuery(query, ItemNotification.class).build();



        notifAdapter = new FirestoreRecyclerAdapter<ItemNotification, ItemNotificationViewHolder>(options) {
            @NonNull
            @Override
            public ItemNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);

                context = parent.getContext();
                return new ItemNotificationViewHolder(mView);
            }

            @Override
            protected void onBindViewHolder(@NonNull ItemNotificationViewHolder holder, int position, @NonNull final ItemNotification model) {

                holder.notifTitle.setText(model.getTitle());
                holder.notifDesc.setText(model.getMessage());
                Glide.with(context).load(model.getImageUrl()).into(holder.notifImg);


                holder.clearBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);

                        builder.setTitle("Confirmation");
                        builder.setMessage("Are you sure you want to delete this notification?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                db.collection("pushNotification").document("studentsNotification").collection(mobString).document(model.getPushKey())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting document", e);
                                            }
                                        });

                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        };

        notifList.setHasFixedSize(true);
        notifList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notifList.setAdapter(notifAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        notifAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notifAdapter.stopListening();
    }
}
