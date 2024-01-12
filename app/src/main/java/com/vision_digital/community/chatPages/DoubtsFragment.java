package com.vision_digital.community.chatPages;

import static com.vision_digital.activities.DashboardActivity.sid;
import static com.google.firebase.firestore.FieldValue.serverTimestamp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vision_digital.R;
import com.vision_digital.community.chatModels.ImageModel;
import com.vision_digital.community.chatModels.ItemChat;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoubtsFragment extends Fragment {


    ImageView btnSend, buttonPlus, btndoubtSms, chat_send_image, chat_send_image_close;
    EditText msgEdt;
    FirestoreRecyclerAdapter firestoreRecyclerChatAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String student_id;
    String timestamp;
    BottomSheetDialog bottomSheetDialog;
    Boolean isDoubt = true;
    Boolean isAnswer = false;
//    String community_id;
    private LinearLayoutCompat edtTxtReplyLayout;
    private TextView edtTxtReplyUser, edtTxtReply;
    private ImageView chat_reply_cancel_image;
    private String replyOfUserString = "";
    private String replyOfMsgString = "";
    private String replyOfChatIDString = "";
    private int doubt_weight = 0;
    private Boolean isReply = false;

    private RecyclerView chat_recyclerview;
    final int MSG_TYPE_LEFT = 0;
    final int MSG_TYPE_RIGHT = 1;
    LinearLayoutManager linearLayoutManager;
    private String checker = "";

    String msgCategory = "";
    ImageModel imageModel;
    String chatImage = "";
    private Uri selectedImageUri;
    String fileName = "";
    Boolean isImage = false;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChatImage");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    HashMap<String, Object> chatdetails = new HashMap<>();
    DocumentReference reference;

    String chat_id = "";

    //profile


    private String userNameString = "";
    private String profileImageUrlString = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doubts, container, false);

//        community_id = getActivity().getIntent().getStringExtra("community_id");


        getProfileData();

        btnSend = view.findViewById(R.id.btnSendSms);
        buttonPlus = view.findViewById(R.id.btnPlus);
        edtTxtReplyLayout = view.findViewById(R.id.edtTxtReplyLayout);
        edtTxtReplyUser = view.findViewById(R.id.edtTxtReplyUser);
        edtTxtReply = view.findViewById(R.id.edtTxtReply);
        chat_reply_cancel_image = view.findViewById(R.id.chat_reply_cancel_image);
        msgEdt = view.findViewById(R.id.edtTxtSms);
        student_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chat_recyclerview = view.findViewById(R.id.chat_recyclerview);
        btndoubtSms = view.findViewById(R.id.btndoubtSms);
        btndoubtSms.setVisibility(View.GONE);
        chat_send_image = view.findViewById(R.id.chat_send_image);
        chat_send_image_close = view.findViewById(R.id.chat_send_image_close);


        timestamp = String.valueOf(serverTimestamp());


        edtTxtReplyLayout.setVisibility(View.GONE);
        chat_reply_cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTxtReplyLayout.setVisibility(View.GONE);
            }
        });
        chat_send_image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat_send_image_close.setVisibility(View.GONE);
                chat_send_image.setVisibility(View.GONE);
            }
        });


        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBottomSheetDialog();
                chat_send_image.setVisibility(View.GONE);
                chat_send_image_close.setVisibility(View.GONE);
            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        chat_recyclerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImage) {
                    msgCategory = "image";
                    sendImage();


                } else {
                    String msgUtils = msgEdt.getText().toString();
                    if (msgUtils != null) {
                        msgCategory = "txt";
                        sendMessage();


                    } else {

                    }
                }
            }
        });

        getChatList();

        return view;


    }

    private void sendMessage() {
        String chatMsg = msgEdt.getText().toString().trim();
        Log.e("chatmsg", chatMsg);
        Log.e("student_id", student_id);
        Log.e("chat", "chat");
        Log.e("timestamp", String.valueOf(timestamp));


        chatdetails.put("message", chatMsg);
        chatdetails.put("userId", student_id);
        chatdetails.put("userName",userNameString);
        chatdetails.put("userImage",profileImageUrlString);

        if (isDoubt) {
            chatdetails.put("msgType", "doubt");
            chatdetails.put("doubtWeight", doubt_weight);

        }
        if (isAnswer) {
            chatdetails.put("msgType", "answer");
            chatdetails.put("doubtWeight", 0);

        }

       chat_id = student_id + timestamp;

        Log.e("chat_id", chat_id);

        chatdetails.put("chatId", chat_id);
        chatdetails.put("isReply", isReply);
        chatdetails.put("replyOfMsg", replyOfMsgString);
        chatdetails.put("replyOfUser", replyOfUserString);
        chatdetails.put("repliedOfChatId", replyOfChatIDString);
        chatdetails.put("msgTime", FieldValue.serverTimestamp());
        chatdetails.put("chatImage", chatImage);
        chatdetails.put("msgCategory", msgCategory);

        reference = db.collection("Communities").document(ChatsFragment.community_id).collection("Chats").document(chat_id);


        reference.set(chatdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                msgEdt.setText("");
                msgEdt.setFocusable(false);

                isDoubt = true;
                btndoubtSms.setVisibility(View.GONE);
                edtTxtReplyLayout.setVisibility(View.GONE);

                isAnswer = false;
                isDoubt = true;

                Log.e("Chat saved", "Chat saved");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isAnswer = false;

                isDoubt = true;
                btndoubtSms.setVisibility(View.GONE);
                edtTxtReplyLayout.setVisibility(View.GONE);

                Log.e("Chat saved failed", e.toString());
            }
        });

    }

    private void sendImage() {
        Log.e("method call sendImage", "method call");

        if (selectedImageUri != null) {
            Log.e("enter if ", "enter if");

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Sending Image...");
            progressDialog.show();
//            StorageReference fileRef = storageReference.child(fileName + "." + getFileExtension(selectedImageUri));

            StorageReference fileRef = storageReference.child("DoubtImage").child(fileName);
            fileRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            imageModel = new ImageModel(uri.toString());
                            Log.e("chatImage", imageModel.getChatImageUrl());
                            chatImage = imageModel.getChatImageUrl();
                            String modelId = databaseReference.push().getKey();
                            databaseReference.child(modelId).setValue(imageModel);
                            chat_send_image.setVisibility(View.GONE);
                            chat_send_image_close.setVisibility(View.GONE);
                            bottomSheetDialog.dismiss();
                            Toast.makeText(getContext(), "Image sent.", Toast.LENGTH_SHORT).show();
                            sendMessage();

                            btndoubtSms.setVisibility(View.GONE);
                            edtTxtReplyLayout.setVisibility(View.GONE);
                            isImage = false;
                            chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
                            firestoreRecyclerChatAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();

                    Log.e("uploading Exception", e.toString());
                    Toast.makeText(getContext(), "Uploading Failed..", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(getContext(), "Image Uploaded.", Toast.LENGTH_SHORT).show();

                }
            });


        } else {

        }
    }

    private void getChatList() {
        Query query = db.collection("Communities").document(ChatsFragment.community_id).collection("Chats").orderBy("msgTime", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ItemChat> response = new FirestoreRecyclerOptions.Builder<ItemChat>()
                .setQuery(query, ItemChat.class)
                .build();

        firestoreRecyclerChatAdapter = new FirestoreRecyclerAdapter<ItemChat, DoubtHolder>(response) {
            @Override
            public void onBindViewHolder(DoubtHolder holder, @SuppressLint("RecyclerView") int position, ItemChat model) {


                if (model.getMsgType().equals("doubt") || model.getMsgType().equals("answer")) {
                    holder.chat_layout.setVisibility(View.VISIBLE);
                    holder.chat.setVisibility(View.VISIBLE);
                    holder.incoming_chat_user.setVisibility(View.VISIBLE);
                    holder.chat_time.setVisibility(View.VISIBLE);
                    holder.chat_type.setVisibility(View.VISIBLE);
                    holder.communityChatImage.setVisibility(View.VISIBLE);


                    Glide.with(getActivity().getApplicationContext())
                            .load(R.drawable.ic_baseline_question_mark_24)
                            .into(holder.chat_type);


                    if (model.getUserId().equals(student_id)) {
                        holder.incoming_chat_user.setText("");

                    } else {
                        holder.incoming_chat_user.setText(model.getUserName());

                    }
                    holder.chat.setText(model.getMessage()+"  "+ model.getDoubtWeight());

                    if (model.getMsgCategory().equals("image")) {
                        if (!model.getChatImage().equals("")) {
                            holder.chat_image.setVisibility(View.VISIBLE);
                            Glide.with(getActivity().getApplicationContext())
                                    .load(model.getChatImage())
                                    .into(holder.chat_image);

                        } else {
                            holder.chat_image.setVisibility(View.GONE);


                        }
                    }

                    if (!model.getUserImage().equals("")){
                        Glide.with(getActivity().getApplicationContext())
                                .load(model.getUserImage())
                                .into(holder.communityChatImage);
                    }

                    if (model.getMsgCategory().equals("pdf")) {
                        if (!model.getChatImage().equals("")) {
                            holder.chat_image.setVisibility(View.GONE);
                            Log.e("msgCategory", model.getMsgCategory());
                            holder.pdf_icon.setVisibility(View.VISIBLE);
//                        holder.chat.setText("PDF file: \n" + model.getChatImage() + "\n" + model.getMessage());


                        } else {
                            holder.pdf_icon.setVisibility(View.GONE);


                        }
                    }


                } else {
                    holder.chat_layout.setVisibility(View.GONE);
                    holder.chat.setVisibility(View.GONE);
                    holder.incoming_chat_user.setVisibility(View.GONE);
                    holder.chat_time.setVisibility(View.GONE);
                    holder.chat_type.setVisibility(View.GONE);
                    holder.communityChatImage.setVisibility(View.GONE);


                    Log.e("No Doubts", "No Doubts");
                }
//                if (model.getMsgType().equals("chat")) {
//                    Glide.with(getActivity().getApplicationContext())
//                            .load(R.drawable.ic_baseline_sms_24)
//                            .into(holder.chat_type);
//                }

                if (!model.getReplyOfUser().equals("")) {
                    holder.replied_msg_card.setVisibility(View.VISIBLE);
                    holder.replied_by_user.setText(model.getReplyOfUser());
                    holder.replied_of_msg.setText(model.getReplyOfMsg());

                } else {
                    holder.replied_msg_card.setVisibility(View.GONE);
                }


//                ---------------------time manage----------------


//                -------------- for current time-------
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                Date dateNow = new Date();
                String currentServerTime = (formatter.format(dateNow));
                Log.e("current time", currentServerTime);


//                ------------chat time------------
                String time = String.valueOf(model.getMsgTime());
                Log.e("chat time", time);

                if (!time.equals("null")) {
                    Log.e("if enter time", time);
                    DateFormat inputFormat = new SimpleDateFormat(
                            "E MMM dd HH:mm:ss 'GMT'z yyyy", Locale.ENGLISH);

                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa", Locale.US);
                    SimpleDateFormat formatter2 = new SimpleDateFormat("hh:mm aaa", Locale.US);

                    String time_chat = time;

                    Date date = null;
                    try {
                        date = inputFormat.parse(time_chat);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String chatTiming = formatter.format(date);

                    if (chatTiming.equals(currentServerTime)) {
                        holder.chat_time.setText(formatter2.format(date));

                    } else {
                        holder.chat_time.setText(formatter1.format(date));

                    }


                } else {
                    Log.e("chat time", time);

                }


                if (model.getMsgType().equals("doubt")) {
                    Glide.with(getActivity().getApplicationContext())
                            .load(R.drawable.ic_baseline_question_mark_24)
                            .into(holder.chat_type);

                }
                if (model.getMsgType().equals("answer")) {
                    Glide.with(getActivity().getApplicationContext())
                            .load(R.drawable.ic_baseline_check_circle_outline_24)
                            .into(holder.chat_type);
                }

                ////reply of chat

                if (!model.getReplyOfUser().equals("")) {
                    holder.replied_msg_card.setVisibility(View.VISIBLE);
                    holder.replied_by_user.setText(model.getReplyOfUser());
                    holder.replied_of_msg.setText(model.getReplyOfMsg());

                } else {
                    holder.replied_msg_card.setVisibility(View.GONE);
                }

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.exit_popup, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
                        Button yesBtn = dialogView.findViewById(R.id.yesButton);
                        TextView message = dialogView.findViewById(R.id.message);
                        TextView title = dialogView.findViewById(R.id.title);


                        message.setText("Choose your Option");
                        title.setVisibility(View.GONE);

                        yesBtn.setText("Answer");
                        cancelBtn.setText("Raise Doubt");

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (model.getMsgType().equals("doubt")) {
                                    doubt_weight = model.getDoubtWeight();

                                    int dw = doubt_weight + 1;

                                    db.collection("Communities").document(ChatsFragment.community_id).collection("Chats")
                                            .document(model.getChatId()).update("doubtWeight", dw);

                                    alertDialog.dismiss();

                                } else {
                                    alertDialog.dismiss();


                                }
                            }
                        });
                        yesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                edtTxtReplyLayout.setVisibility(View.VISIBLE);

                                edtTxtReplyUser.setText(model.getUserId());

                                edtTxtReplyUser.setMaxLines(1);
                                edtTxtReply.setMaxLines(1);
                                isReply = true;
                                if (model.getMsgType().equals("doubt")) {
                                    isAnswer = true;
                                    Log.e("doubtmsgtypw","isanswer");
                                } else {
                                    isAnswer = false;
                                    Log.e("doubtmsgtypw","isanswerFalse");

                                }
                                edtTxtReply.setText(model.getMessage());

                                replyOfChatIDString = model.getChatId();
                                replyOfMsgString = edtTxtReply.getText().toString();
                                replyOfUserString = edtTxtReplyUser.getText().toString();
                                Log.e("replyOfUserString", replyOfUserString);
                                Log.e("replyOfMsgString", replyOfMsgString);


                                alertDialog.dismiss();
                                msgEdt.requestFocus();
                                msgEdt.setFocusable(true);
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(msgEdt, InputMethodManager.SHOW_IMPLICIT);

                            }
                        });


                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(true);
                        return false;
                    }
                });
                holder.chat_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getActivity().getLayoutInflater();

//                        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View dialogView = inflater.inflate(R.layout.activity_image_transition, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        final androidx.appcompat.app.AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                        ImageView optionImageTrans = dialogView.findViewById(R.id.optionImageTrans);
                        Glide.with(getActivity().getApplicationContext())
                                .load(model.getChatImage())
                                .into(optionImageTrans);
                        ImageView closeBtnn = dialogView.findViewById(R.id.closeBtnn);
                        closeBtnn.setVisibility(View.GONE);


                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(true);
                    }

                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        community_id = model.getCommunity_id();


                    }
                });

            }

            @Override
            public int getItemViewType(int position) {
                if (getItem(position).getUserId().equals(student_id))
                    return MSG_TYPE_RIGHT;
                else
                    return MSG_TYPE_LEFT;
            }


            @Override
            public DoubtHolder onCreateViewHolder(ViewGroup group, int viewType) {

                if (viewType == MSG_TYPE_RIGHT) {
                    View view = LayoutInflater.from(group.getContext())
                            .inflate(R.layout.single_normal_chat_outgoing_layout, group, false);
                    return new DoubtHolder(view);


                } else {
                    View view = LayoutInflater.from(group.getContext())
                            .inflate(R.layout.single_normal_chat_incoming_layout, group, false);
                    return new DoubtHolder(view);

                }
//                View view = LayoutInflater.from(group.getContext())
//                        .inflate(R.layout.single_normal_chat_incoming_layout, group, false);
//                return new ChatHolder(view);


            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }


        };
        chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
        chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
        firestoreRecyclerChatAdapter.notifyDataSetChanged();
        chat_recyclerview.setAdapter(firestoreRecyclerChatAdapter);
    }

    public class DoubtHolder extends RecyclerView.ViewHolder {
        TextView incoming_chat_user, chat, chat_time, replied_by_user, replied_of_msg;
        ImageView communityChatImage, chat_type, chat_image, pdf_icon;
        LinearLayoutCompat replied_msg_card;
        LinearLayout chat_layout;


        public DoubtHolder(View itemView) {
            super(itemView);
            incoming_chat_user = itemView.findViewById(R.id.incoming_chat_user);
            communityChatImage = itemView.findViewById(R.id.communityChatImage);
            chat = itemView.findViewById(R.id.chat);
            chat_time = itemView.findViewById(R.id.chat_time);
            chat_type = itemView.findViewById(R.id.chat_type);
            chat_image = itemView.findViewById(R.id.chat_image);
            pdf_icon = itemView.findViewById(R.id.chat_pdf);
            replied_by_user = itemView.findViewById(R.id.replied_by_user);
            replied_of_msg = itemView.findViewById(R.id.replied_of_msg);
            replied_msg_card = itemView.findViewById(R.id.replied_msg_card);
            chat_layout = itemView.findViewById(R.id.chat_layout);

        }
    }

    private void showBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.doc_bottom_sheet);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bottomSheetDialog.setCancelable(true);

        CircleImageView chat_doc = bottomSheetDialog.findViewById(R.id.chat_doc);
        CircleImageView chat_gallery = bottomSheetDialog.findViewById(R.id.chat_gallery);
        CircleImageView chat_camera = bottomSheetDialog.findViewById(R.id.chat_camera);
        CircleImageView chat_doubt = bottomSheetDialog.findViewById(R.id.chat_doubt);
        CircleImageView chat_invitation = bottomSheetDialog.findViewById(R.id.chat_invitation);


        chat_doubt.setVisibility(View.GONE);
        chat_doc.setVisibility(View.GONE);
        chat_invitation.setVisibility(View.GONE);

        chat_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    isImage = true;
                    isDoubt = true;
                    btndoubtSms.setVisibility(View.VISIBLE);
//                    takePictureFromGallery();
                    ImagePicker.Companion.with(DoubtsFragment.this)
                            .galleryOnly()
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                    bottomSheetDialog.dismiss();
                }
            }
        });

        chat_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    isImage = true;
                    isDoubt = true;
                    btndoubtSms.setVisibility(View.VISIBLE);
//                    takePictureFromCamera();
//                    dispatchTakePictureIntent();
                    ImagePicker.Companion.with(DoubtsFragment.this)
                            .cameraOnly()
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                    bottomSheetDialog.dismiss();
                }
            }
        });

        bottomSheetDialog.show();


    }


    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else
            Toast.makeText(getContext(), "Permission not Granted", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("onActivityResult", "enter");

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 3) {

                selectedImageUri = data.getData();
                msgCategory = "pdf";
                fileName = selectedImageUri.getLastPathSegment();
                Log.e("selectedImageUri", selectedImageUri.toString());
                Log.e("fileName", fileName);


                sendImage();
            } else {
                //            ---------Image URi-----------
                selectedImageUri = data.getData();
                chat_send_image.setVisibility(View.VISIBLE);
                chat_send_image_close.setVisibility(View.VISIBLE);
                Log.e("selectedImageUri", selectedImageUri.toString());
                fileName = selectedImageUri.getLastPathSegment();

                chat_send_image.setImageURI(selectedImageUri);
                chat_send_image.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        firestoreRecyclerChatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firestoreRecyclerChatAdapter.stopListening();
    }


    private void getProfileData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("StudentProfile");
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        DocumentReference uidRef = db.collection("StudentProfile").document(String.valueOf(sid));
                        uidRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        userNameString = document.getString("name");
                                        profileImageUrlString = document.getString("profileImage");

                                    } else {
                                        Log.d("No such document", "No such document");
                                    }
                                } else {
                                    Log.d("get failed with", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d("Error getting documents", "Error getting documents: ", task.getException());
                }
            }
        });
    }


}