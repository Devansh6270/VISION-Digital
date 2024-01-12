package com.vision_digital.community.chatPages;

import static com.vision_digital.activities.DashboardActivity.sid;
import static com.vision_digital.community.CommunityChatPageActivity.communityLogo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vision_digital.BuildConfig;
import com.vision_digital.activities.CourseDetailsActivity;
import com.vision_digital.activities.DashboardActivity;
import com.vision_digital.activities.PdfRenderActivity;
import com.vision_digital.R;
import com.vision_digital.community.GroupStudyVideoPlayerActivity;
import com.vision_digital.community.UserHoldingScreenActivity;
import com.vision_digital.community.chatModels.ImageModel;
import com.vision_digital.community.chatModels.ItemChat;
import com.vision_digital.community.invitation.SelectMilestoneInvitationActivity;
import com.vision_digital.helperClasses.JSONParser;
import com.vision_digital.model.chapters.ItemChapter;
import com.vision_digital.model.milestone.ItemMileStone;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private String currentPhotoPath;
    private String fileName = "";
    private Boolean isReply = false;
    private BottomSheetDialog bottomSheetDialog;
    private ImageView btnSend, buttonPlus, chat_send_image, btndoubtSms, chat_send_image_close;
    private EditText msgEdt;
    private FirestoreRecyclerAdapter firestoreRecyclerChatAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId;
    private String chatImage = "";
    private String timestamp;
    private Boolean isDoubt = false;
    private Boolean isAnswer = false;
    private Boolean isInvitation = false;
    private Boolean isChat = true;
    private String msgCategory = "";
    private Boolean isImage = false;
    public static String community_id;
    private String chat_id = "";
    private String chatIdForVideo = "";
    private String communityName;
    private String invitedTime = "";
    private RecyclerView chat_recyclerview;
    final int MSG_TYPE_LEFT = 0;
    final int MSG_TYPE_RIGHT = 1;
    private LinearLayoutManager linearLayoutManager;
    private Uri selectedImageUri;
    private ImageModel imageModel;
    private String replyOfUserString = "";
    private String replyOfMsgString = "";
    private String replyOfChatIDString = "";
    private int doubt_weight = 0;
    private int videoPosForSendInvite = 0;

    private LinearLayoutCompat edtTxtReplyLayout;
    private TextView edtTxtReplyUser, edtTxtReply;
    private ImageView chat_reply_cancel_image;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChatImage");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private HashMap<String, Object> chatdetails = new HashMap<>();
    private HashMap<String, Object> invitedVideodetails = new HashMap<>();
    private DocumentReference reference, documentReferenceForVideoStatus;
    private String milestoneId = "", milestone_name;
    private String from_activity = "";

    // for invitation join
    private String courseDetailsUrl = "";
    int chapter_id;
    private String courseDescription;
    int videoPos = 0;
    int videoPosTemp = 0;
    private String chap_id = "";
    private String lastMilestoneId = "";
    private String subscriptionStatus = "";
    private String courseDuration = "";
    private String courseOwnerName = "", courseOwnerQuali = "";
    private String invitationId = "";
    private List<Long> forMonths = new ArrayList<>();
    private Uri previewVideoURI;
    private ArrayList<Uri> courseMilestoneList = new ArrayList<>();
    private ArrayList<ItemMileStone> mileStonesArrayList = new ArrayList<>();
    private ArrayList<ItemChapter> chaptersList = new ArrayList<>();
    private ArrayList<String> invitedStudentId = new ArrayList<>();
    private ArrayList<String> invitedStudentName = new ArrayList<>();
    private String studentUId = "";
    private String userType = "";
    private String videoStatusId = "";
    private String firestoreUserId = "";
    private String admin = "";

    private CollectionReference collectionReference;
    private HashMap<String, Object> invitedStudentDetails = new HashMap<>();

    //profile

    private String userNameString = "";
    private String profileImageUrlString = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        getProfileData();
        userId = DashboardActivity.uid;
        btnSend = view.findViewById(R.id.btnSendSms);
        edtTxtReplyLayout = view.findViewById(R.id.edtTxtReplyLayout);
        edtTxtReplyUser = view.findViewById(R.id.edtTxtReplyUser);
        edtTxtReply = view.findViewById(R.id.edtTxtReply);
        chat_reply_cancel_image = view.findViewById(R.id.chat_reply_cancel_image);

        btndoubtSms = view.findViewById(R.id.btndoubtSms);
        btndoubtSms.setVisibility(View.GONE);
        buttonPlus = view.findViewById(R.id.btnPlus);
        chat_send_image = view.findViewById(R.id.chat_send_image);
        chat_send_image.setVisibility(View.GONE);
        chat_send_image_close = view.findViewById(R.id.chat_send_image_close);
        chat_send_image_close.setVisibility(View.GONE);
        msgEdt = view.findViewById(R.id.edtTxtSms);
        courseDetailsUrl = getActivity().getString(R.string.apiURL) + "getCourseDetails";

        studentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        chat_recyclerview = view.findViewById(R.id.chat_recyclerview);
        from_activity = getActivity().getIntent().getStringExtra("activity");
        if (from_activity.equals("community_list") || from_activity.equals("course_details")) {
            community_id = getActivity().getIntent().getStringExtra("community_id");
            communityName = getActivity().getIntent().getStringExtra("community_name");
            Log.e("communityid", community_id);


        } else if (from_activity.equals("select_student")) {
            milestoneId = getActivity().getIntent().getStringExtra("milestone_id");
            Log.e("milestone_idCht", milestoneId);

            milestone_name = getActivity().getIntent().getStringExtra("milestone_name");
            String videoposTemperary = getActivity().getIntent().getStringExtra("videoPos");
            videoPosForSendInvite = Integer.parseInt(videoposTemperary);
            Log.e("videoPosForSendInvite", String.valueOf(videoPosForSendInvite));

            invitedTime = getActivity().getIntent().getStringExtra("invitedTime");
            invitedStudentId = (ArrayList<String>) getActivity().getIntent().getSerializableExtra("invitedStudId");
            invitedStudentName = (ArrayList<String>) getActivity().getIntent().getSerializableExtra("invitedStudName");
            Log.e("selectedItems", String.valueOf(invitedStudentId));
            Log.e("milestonename chat", milestone_name);
            Log.e("invitedTime chat", invitedTime);

            msgEdt.requestFocus();
            isInvitation = true;
            isDoubt = false;
            isAnswer = false;
            isChat = false;
            isReply = false;
            msgEdt.setText(userId + "send you Invitation for Group Study on Topic: " + milestone_name);
            edtTxtReply.setHeight(40);

        } else {

        }


        Long tsLong = System.currentTimeMillis() / 1000;
        timestamp = tsLong.toString();
        String chat_id = userId + timestamp;
        videoStatusId = userId + "video" + timestamp;

        Log.e("chat_id", chat_id);

        edtTxtReplyLayout.setVisibility(View.GONE);
        chat_reply_cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtTxtReplyLayout.setVisibility(View.GONE);
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
        linearLayoutManager.setStackFromEnd(true);
        chat_recyclerview.setLayoutManager(linearLayoutManager);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImage) {
                    msgCategory = "image";
                    sendImage();
                    btndoubtSms.setVisibility(View.GONE);
                    edtTxtReplyLayout.setVisibility(View.GONE);
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
        chat_send_image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chat_send_image_close.setVisibility(View.GONE);
                chat_send_image.setVisibility(View.GONE);
            }
        });

        getChatList();

        return view;


    }

    private void sendImage() {
        Log.e("method call sendImage", "method call");

        if (selectedImageUri != null) {
            Log.e("enter if ", "enter if");

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Sending Image...");
            progressDialog.show();
//            StorageReference fileRef = storageReference.child(fileName + "." + getFileExtension(selectedImageUri));

            StorageReference fileRef = storageReference.child("ChatImage").child(fileName);
            fileRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            isDoubt = false;
//                            isAnswer = false;
//                            isInvitation = false;
//                            isChat = true;
                            btndoubtSms.setVisibility(View.GONE);
                            edtTxtReplyLayout.setVisibility(View.GONE);
                            isImage = false;
//                            isReply = false;

                            imageModel = new ImageModel(uri.toString());
                            Log.e("chatImage", imageModel.getChatImageUrl());
                            chatImage = imageModel.getChatImageUrl();

                            chat_send_image.setVisibility(View.GONE);
                            chat_send_image_close.setVisibility(View.GONE);
                            bottomSheetDialog.dismiss();
                            sendMessage();

                            chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
                            firestoreRecyclerChatAdapter.notifyDataSetChanged();

                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.show();

                    btndoubtSms.setVisibility(View.GONE);
                    edtTxtReplyLayout.setVisibility(View.GONE);
                    isImage = false;

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

                    btndoubtSms.setVisibility(View.GONE);
                    edtTxtReplyLayout.setVisibility(View.GONE);
                    isImage = false;
                }
            });


        } else {

            btndoubtSms.setVisibility(View.GONE);
            edtTxtReplyLayout.setVisibility(View.GONE);
            isImage = false;
        }
    }

    private String getFileExtension(Uri mUri) {

        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(mUri));

    }

    private void sendMessage() {
        chap_id = "";
        String chatMsg = msgEdt.getText().toString().trim();
        Log.e("chatmsg", chatMsg);
        Log.e("userId", userId);
        Log.e("chat", "chat");

        chatdetails.put("message", chatMsg);
        chatdetails.put("userId", userId);
        chatdetails.put("userName", userNameString);
        chatdetails.put("userImage", profileImageUrlString);
        Log.e("msgType", String.valueOf(isDoubt));
        Log.e("msgType", String.valueOf(isAnswer));
        Log.e("msgType", String.valueOf(isInvitation));

        if (isDoubt) {
            chatdetails.put("msgType", "doubt");
            chatdetails.put("doubtWeight", doubt_weight);

        } else if (isAnswer) {
            chatdetails.put("msgType", "answer");
            chatdetails.put("doubtWeight", 0);

        } else if (isInvitation) {
            chatdetails.put("msgType", "invitation");
            chatdetails.put("doubtWeight", 0);

        } else if (isChat) {
            chatdetails.put("msgType", "chat");
            chatdetails.put("doubtWeight", 0);

        }

        chat_id = userId + timestamp;

        Log.e("chat_id", chat_id);

        chatdetails.put("chatId", chat_id);
        chatdetails.put("isReply", isReply);
        chatdetails.put("replyOfMsg", replyOfMsgString);
        chatdetails.put("replyOfUser", replyOfUserString);
        chatdetails.put("repliedOfChatId", replyOfChatIDString);
        chatdetails.put("msgTime", FieldValue.serverTimestamp());
        chatdetails.put("chatImage", chatImage);
        chatdetails.put("msgCategory", msgCategory);
        chatdetails.put("invitationTime", invitedTime);
        chatdetails.put("milestoneID",milestoneId);
        chatdetails.put("videoPosition",videoPosForSendInvite);

        reference = db.collection("Communities").document(community_id).collection("Chats").document(chat_id);


        reference.set(chatdetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (isInvitation) {
                    collectionReference = db.collection("Communities").document(community_id).collection("Chats").document(chat_id).collection("invitedStudent");
                    documentReferenceForVideoStatus = db.collection("Communities").document(community_id).collection("Chats").document(chat_id).collection("invitedVideoPlayingStatus").document(videoStatusId);

                    saveInvitedStudentDetails();
                    saveInvitedVideoStatus();
                    msgEdt.setText("");
                    msgEdt.setFocusable(false);
                    chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
                    firestoreRecyclerChatAdapter.notifyDataSetChanged();
                    Log.e("Chat saved", "Chat saved");
                    msgCategory = "";
                    isDoubt = false;
                    isAnswer = false;
                    isInvitation = false;
                    isChat = true;
                    isReply = false;
                    btndoubtSms.setVisibility(View.GONE);
                    edtTxtReplyLayout.setVisibility(View.GONE);
                } else {
                    msgEdt.setText("");
                    msgEdt.setFocusable(false);
                    chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());
                    firestoreRecyclerChatAdapter.notifyDataSetChanged();
                    Log.e("Chat saved", "Chat saved");
                    msgCategory = "";
                    isDoubt = false;
                    isAnswer = false;
                    isInvitation = false;
                    isChat = true;
                    isReply = false;
                    btndoubtSms.setVisibility(View.GONE);
                    edtTxtReplyLayout.setVisibility(View.GONE);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Chat saved failed", e.toString());

                isDoubt = false;
                isAnswer = false;
                isInvitation = false;
                isChat = true;
                isReply = false;
                btndoubtSms.setVisibility(View.GONE);
                edtTxtReplyLayout.setVisibility(View.GONE);
            }
        });


    }

    private void saveInvitedVideoStatus() {
        invitedVideodetails.put("videoPlayingStatus", 00.00);
        invitedVideodetails.put("videoStatus","pause");
        invitedVideodetails.put("liveUnliveStatus","unlive");
        documentReferenceForVideoStatus.set(invitedVideodetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void saveInvitedStudentDetails() {

        String studentName = "";
        String studentId = "";
        for (int i = 0; i < invitedStudentId.size(); i++) {
            studentId = invitedStudentId.get(i);
            studentName = invitedStudentName.get(i);

            if (studentId.equals(studentUId)) {
                userType = "admin";
            } else {
                userType = "member";
            }

            invitedStudentDetails.put("invStudUsername", studentName);
            invitedStudentDetails.put("invStudId", studentId);
            invitedStudentDetails.put("userType", userType);
            invitedStudentDetails.put("status","unlive");

            collectionReference.add(invitedStudentDetails).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override

                public void onSuccess(DocumentReference documentReference) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.e("Chat saved failed", e.toString());

                }
            });
        }

    }

    private void getChatList() {
        Query query = db.collection("Communities").document(community_id).collection("Chats").orderBy("msgTime", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ItemChat> response = new FirestoreRecyclerOptions.Builder<ItemChat>()
                .setQuery(query, ItemChat.class)
                .build();

        firestoreRecyclerChatAdapter = new FirestoreRecyclerAdapter<ItemChat, ChatHolder>(response) {
            @Override
            public void onBindViewHolder(ChatHolder holder, @SuppressLint("RecyclerView") int position, ItemChat model) {
                holder.replied_msg_card.setVisibility(View.GONE);
                replyOfChatIDString = "";
                replyOfMsgString = "";
                replyOfUserString = "";


                if (!model.getMessage().equals("")) {
                    holder.chat.setVisibility(View.VISIBLE);

                    holder.chat.setText(model.getMessage());

                } else {
                    holder.chat.setVisibility(View.INVISIBLE);

                }

                String replyMessage = model.getReplyOfMsg();

//                Log.e("reply",replyMessage);
//                Log.e("Reply of message",model.getReplyOfMsg());
                if (model.getUserId().equals(userId)) {
                    holder.incoming_chat_user.setText("");

                } else {
                    holder.incoming_chat_user.setVisibility(View.VISIBLE);
                    holder.incoming_chat_user.setText(model.getUserName());

                }


                if (!model.getUserImage().equals("")) {
                    Glide.with(getActivity().getApplicationContext())
                            .load(model.getUserImage())
                            .into(holder.communityChatImage);
                } else {
                    Log.e("no image", "No image");
                }


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
                        Log.e("date", String.valueOf(date));
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
                if (model.getMsgType().equals("chat")) {
                    Glide.with(getActivity().getApplicationContext())
                            .load(R.drawable.ic_baseline_sms_24)
                            .into(holder.chat_type);
                }
                if (model.getMsgType().equals("answer")) {
                    Glide.with(getActivity().getApplicationContext())
                            .load(R.drawable.ic_baseline_check_circle_outline_24)
                            .into(holder.chat_type);
                }
                if (model.getMsgType().equals("invitation")) {
                    Glide.with(getActivity().getApplicationContext())
                            .load(R.drawable.ic_keyboard_arrow_right_black_24dp)
                            .into(holder.chat_type);
                }

//                Log.e("isReply", model.getReply().toString());

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

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.exit_popup, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        final AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button cancelBtn = dialogView.findViewById(R.id.cancelButton);
                        Button yesBtn = dialogView.findViewById(R.id.yesButton);
                        TextView message = dialogView.findViewById(R.id.message);
                        TextView title = dialogView.findViewById(R.id.title);


                        message.setText("Do you want to reply this Message?");
                        title.setVisibility(View.GONE);

                        if (model.getMsgType().equals("doubt")) {
                            yesBtn.setText("Answer");
                            cancelBtn.setText("Raise Doubt");

                        } else if (model.getMsgType().equals("invitation")) {
                            yesBtn.setText("join");
                            cancelBtn.setText("share");


                        } else {
                            yesBtn.setText("Reply");
                            cancelBtn.setText("No");
                        }

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                if (model.getMsgType().equals("doubt")) {
                                    doubt_weight = model.getDoubtWeight();

                                    int dw = doubt_weight + 1;

                                    db.collection("Communities").document(ChatsFragment.community_id).collection("Chats")
                                            .document(model.getChatId()).update("doubtWeight", dw);

                                    alertDialog.dismiss();


                                } else if (model.getMsgType().equals("invitation")) {


                                } else {
                                    alertDialog.dismiss();

                                }
                            }
                        });
                        yesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                isReply = true;
                                if (model.getMsgType().equals("doubt")) {
                                    edtTxtReplyLayout.setVisibility(View.VISIBLE);
                                    edtTxtReplyUser.setText(model.getUserId());
                                    edtTxtReplyUser.setMaxLines(1);
                                    edtTxtReply.setMaxLines(1);

                                    isAnswer = true;
                                    isChat = false;
                                    isReply = true;
                                    isInvitation = false;
                                    isDoubt = false;
                                } else if (model.getMsgType().equals("invitation")) {
                                    String invitationTime = model.getInvitationTime();
                                    DateFormat formatter;
                                    formatter = new SimpleDateFormat("hh:mm a");
                                    Date date = null;
//                                    try {
//                                        date = (Date) formatter.parse(invitationTime);
//
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }




                                    Log.e("enter invtation", "true");
                                    isReply = false;
                                    invitationId = model.getChatId();
                                    chatIdForVideo = model.getChatId();
                                    lastMilestoneId = model.getMilestoneID();
                                    fetchStudentJoinedList(invitationId);

                                } else {
                                    edtTxtReplyLayout.setVisibility(View.VISIBLE);
                                    edtTxtReplyUser.setText(model.getUserId());
                                    edtTxtReplyUser.setMaxLines(1);
                                    edtTxtReply.setMaxLines(1);

                                    isAnswer = false;
                                    isChat = true;
                                    isInvitation = false;
                                    isDoubt = false;
                                    isReply = true;
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

                holder.pdf_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getMsgCategory().equals("pdf")) {
                            if (!model.getChatImage().equals("")) {
                                Intent mileStonePlayer = new Intent(getActivity(), PdfRenderActivity.class);
                                mileStonePlayer.putExtra("pdfLink", model.getChatImage());
                                getActivity().startActivity(mileStonePlayer);

                            } else {


                            }
                        }

                    }
                });


                holder.chat_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                        LayoutInflater inflater = getActivity().getLayoutInflater();

//                        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        View dialogView = inflater.inflate(R.layout.activity_image_transition, null);
                        dialogBuilder.setView(dialogView);

                        //Alert Dialog Layout work
                        final AlertDialog alertDialog = dialogBuilder.create();
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
                if (getItem(position).getUserId().equals(userId))
                    return MSG_TYPE_RIGHT;
                else
                    return MSG_TYPE_LEFT;
            }


            @Override
            public ChatHolder onCreateViewHolder(ViewGroup group, int viewType) {

                if (viewType == MSG_TYPE_RIGHT) {
                    View view = LayoutInflater.from(group.getContext())
                            .inflate(R.layout.single_normal_chat_outgoing_layout, group, false);
                    return new ChatHolder(view);


                } else {
                    View view = LayoutInflater.from(group.getContext())
                            .inflate(R.layout.single_normal_chat_incoming_layout, group, false);
                    return new ChatHolder(view);

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


        firestoreRecyclerChatAdapter.notifyDataSetChanged();
        chat_recyclerview.setAdapter(firestoreRecyclerChatAdapter);
        firestoreRecyclerChatAdapter.notifyDataSetChanged();

        chat_recyclerview.smoothScrollToPosition(chat_recyclerview.getBottom());

    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        TextView incoming_chat_user, chat, chat_time, replied_by_user, replied_of_msg;
        ImageView communityChatImage, chat_type, chat_image, pdf_icon;
        LinearLayoutCompat replied_msg_card;

        public ChatHolder(View itemView) {
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


        chat_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    isImage = true;
//                    takePictureFromGallery();
                    ImagePicker.Companion.with(ChatsFragment.this)
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
//                    takePictureFromCamera();
//                    dispatchTakePictureIntent();
                    ImagePicker.Companion.with(ChatsFragment.this)
                            .cameraOnly()
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                    bottomSheetDialog.dismiss();
                }
            }
        });

        chat_doubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    bottomSheetDialog.dismiss();
                    isDoubt = true;
                    isChat = false;
                    isAnswer = false;
                    isInvitation = false;

                    Log.e("bottomdialog", String.valueOf(isDoubt));
                    btndoubtSms.setVisibility(View.VISIBLE);
                    msgEdt.requestFocus();
                    msgEdt.setFocusable(true);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(msgEdt, InputMethodManager.SHOW_IMPLICIT);


                }
            }
        });


        // After Clicking on this we will be
        // redirected to choose pdf
        chat_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                // We will be redirected to choose pdf
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, 3);

            }
        });

        chat_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectMilestoneInvitationActivity.class);
                intent.putExtra("id", community_id);
                intent.putExtra("community_name", communityName);
                intent.putExtra("community_logo", communityLogo);
                Log.e("community_logo", communityLogo);
                startActivity(intent);
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
            dispatchTakePictureIntent();
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

    //     ---------- camera capture in uri-----------
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.e("storageDir", "enter");


        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


//     ---------- camera capture in uri-----------

    private void dispatchTakePictureIntent() {
        Log.e("dispach", "enter");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.e("createImageFile", "enter");

            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.chalksnboard.android.fileprovider",
                        photoFile);
                Log.e("photoFile", "enter");

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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


    class GetCourseDetailsDash extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(getActivity().getApplicationContext());

        @Override
        protected void onPreExecute() {
//            this.dialog.setMessage("Please wait");
//            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jsonParser = new JSONParser(getActivity().getApplicationContext());
            int versionCode = BuildConfig.VERSION_CODE;
            String param = "uid=" + DashboardActivity.uid + "&app_version=" + versionCode + "&student_id=" + sid + "&course_id=" + community_id;

            Log.e("param", param);

            JSONObject jsonObject = jsonParser.makeHttpRequest(courseDetailsUrl, "POST", param);
            if (jsonObject != null) {
                return jsonObject.toString();
            } else {

            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("json", s);

            if (!s.equals("")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    Log.e("Result : ", s);

                    //Do work-----------------------------
                    String status = jsonObject.getString("status");
                    final JSONObject dataObj = jsonObject.getJSONObject("data");

                    String available_subscription_months, subscribed_months;
                    switch (status) {
                        case "success":
                            //Running Fine


                            int positionNumber = 0;

                            //Setting course Details for subscribed course--------------------------
                            subscriptionStatus = dataObj.getString("subscription_status");


                            String courseName = dataObj.getString("title");
                            String courseDesc = dataObj.getString("description");
                            courseDuration = dataObj.getString("duration");
                            courseOwnerName = dataObj.getJSONObject("owner_details").getString("owned_name");
                            courseOwnerQuali = dataObj.getJSONObject("owner_details").getString("owner_qualification");

                            if (subscriptionStatus.equals("subscribed")) {

                                int totalMonths = dataObj.getInt("available_subscription_months");
                                int currentSubsMonths = dataObj.getInt("subscribed_months");
                                if (totalMonths <= currentSubsMonths) {
                                } else {
                                    JSONArray subscriptionJsonArray = dataObj.getJSONArray("available_subscriptions");
                                    forMonths.clear();
                                }


                                courseDescription = courseDesc;
                                previewVideoURI = Uri.parse(dataObj.getString("promo_video_link"));
                                MediaItem mediaItem = MediaItem.fromUri(previewVideoURI);


                                //Setting content--------------------
                                JSONArray courseContent = dataObj.getJSONArray("course_content");
                                JSONArray monthContent = dataObj.getJSONArray("course_content");

                                chaptersList.clear();
                                courseMilestoneList.clear();
                                mileStonesArrayList.clear();

                                for (int i = 0; i < courseContent.length(); i++) {
                                    ItemChapter chapter = new ItemChapter();
                                    JSONObject chapterObj = courseContent.getJSONObject(i);

                                    chapter.setId(chapterObj.getString("id"));
                                    chapter.setTitle("Chapter " + (i + 1) + ": " + chapterObj.getString("title"));
                                    chapter.setSort_order(chapterObj.getString("sort_order"));
                                    chapter.setMin_month("Subscription Month: " + chapterObj.getString("min_month"));

                                    int subMonths = Integer.parseInt(chapterObj.getString("min_month"));

                                    JSONArray notestones = chapterObj.getJSONArray("notes");

                                    JSONArray milestones = chapterObj.getJSONArray("milestones");
                                    for (int j = 0; j < milestones.length(); j++) {
//                                        dialogForIntent.show();
                                        ItemMileStone mileStone = new ItemMileStone();
                                        JSONObject mileStoneObj = milestones.getJSONObject(j);
                                        mileStone.setId(mileStoneObj.getString("id"));
//                                        mileStone.setVideoPosition(positionNumber++);
                                        videoPosTemp = positionNumber++;
                                        mileStone.setVideoPosition(videoPosTemp);
                                        Log.e("Topic name", "" + mileStoneObj.getString("title"));
                                        mileStone.setTitle(mileStoneObj.getString("title"));
                                        mileStone.setDuration(mileStoneObj.getString("duration"));
                                        mileStone.setSort_order(mileStoneObj.getString("sort_order"));
                                        mileStone.setVideoUrl(mileStoneObj.getString("video_link"));
                                        mileStone.setActivityType("courseDetails");
                                        mileStone.setSelected(false);

                                        if (!mileStoneObj.getString("video_link").equals("")) {
                                            courseMilestoneList.add(Uri.parse(mileStoneObj.getString("video_link")));
                                        }
                                        if (!mileStoneObj.getString("duration").equals("1")) {
                                            mileStonesArrayList.add(mileStone);

                                        }
                                        Log.e("Milestone Id", lastMilestoneId);
                                        if (mileStoneObj.getString("id").equals(lastMilestoneId)) {
                                            videoPos = videoPosTemp;
                                            Log.e("videoPos", String.valueOf(videoPos));

                                            chap_id = chapterObj.getString("id");
                                            Log.e("chap_id", String.valueOf(chap_id));

                                            chapter_id = Integer.parseInt(chapterObj.getString("id"));
                                            Log.e("chapter_id", String.valueOf(chapter_id));

                                        } else {
                                            Log.e("No video Pos", "no video pos");
                                        }

                                        chapter.mileStonesList.add(mileStone);
                                        chapter.setItemMileStone(mileStone);
                                    }

                                    chaptersList.add(chapter);

                                }

                                Log.e("chat videoStatus", videoStatusId);
                                if (studentUId.equals(admin)) {
                                    Intent mileStonePlayer = new Intent(getActivity().getApplicationContext(), GroupStudyVideoPlayerActivity.class);
                                    mileStonePlayer.putExtra("videoPosition", videoPos);
                                    mileStonePlayer.putExtra("mileStonesList", mileStonesArrayList);
                                    mileStonePlayer.putExtra("id", community_id);
                                    mileStonePlayer.putExtra("chapter_id", chapter_id);
                                    mileStonePlayer.putExtra("chatId", chatIdForVideo);
                                    mileStonePlayer.putExtra("videoStatusId", videoStatusId);
                                    getActivity().startActivity(mileStonePlayer);
                                } else {
                                    Intent mileStonePlayer = new Intent(getActivity().getApplicationContext(), UserHoldingScreenActivity.class);
                                    mileStonePlayer.putExtra("videoPosition", videoPos);
                                    mileStonePlayer.putExtra("mileStonesList", mileStonesArrayList);
                                    mileStonePlayer.putExtra("id", community_id);
                                    mileStonePlayer.putExtra("chapter_id", chapter_id);
                                    getActivity().startActivity(mileStonePlayer);
                                }


                            } else if (subscriptionStatus.equals("unsubscribed")) {
                                Toast.makeText(getActivity().getApplicationContext(), "Subscribe this Course Again!..", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity().getApplicationContext(), CourseDetailsActivity.class);
                                intent.putExtra("id", community_id);
                                getActivity().startActivity(intent);


                            }

                            break;
                        case "maintenance":
                            //Undermaintance
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            LayoutInflater inflater = getActivity().getApplicationContext().getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.under_maintanance_dialog, null);
                            dialogBuilder.setView(dialogView);
                            //Alert Dialog Layout work
                            TextView maintainanceContent = dialogView.findViewById(R.id.underMaintananceContent);
                            String msgContent = dataObj.getString("message");
                            maintainanceContent.setText(Html.fromHtml(msgContent));

                            TextView btnOK = dialogView.findViewById(R.id.btnOK);
                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((Activity) getActivity().getApplicationContext()).finish();
                                }
                            });
                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            break;
                        case "failure":
                            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Contact to support over website", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Response reached---------------value in 's' variable
            dialog.dismiss();
        }
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


    private void fetchStudentJoinedList(String invitationId) {

        Log.e("community Id", community_id);
        db.collection("Communities").document(community_id).collection("Chats").document(invitationId).collection("invitedStudent").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> studentList = queryDocumentSnapshots.getDocuments();
                Log.e("List", studentList.toString());


                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        String invitedStuId = documentChange.getDocument().getData().get("invStudId").toString();
                        String invitedUserType = documentChange.getDocument().getData().get("userType").toString();


                        if (invitedUserType.equals("admin")) {
                            admin = documentChange.getDocument().getData().get("invStudId").toString();
                        }


                        Log.e("studentIdloop11", invitedStuId);
                        if (studentUId.equals(invitedStuId)) {
                            Log.e("studentIdloop", invitedStuId);
                            firestoreUserId = invitedStuId;

                            break;

                        } else {
                            firestoreUserId = "";
                        }
                    }
                    if (studentUId.equals(firestoreUserId)) {

                        new GetCourseDetailsDash().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                    } else {
                        Toast.makeText(getContext(), "This Invitation is not for you!...", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getContext(), "This Invitation is not for you!...", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }




}