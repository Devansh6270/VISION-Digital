package com.vision_digital.model.chats;

import static com.vision_digital.activities.DashboardActivity.sid;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.vision_digital.R;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MessagesAdapter  extends RecyclerView.Adapter{

    Context context;
    ArrayList<Messages> messages;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage ;

    final  int MSG_SENT = 1;
    final int MSG_RECEIVE = 2;

    String senderRoom ;
    String receiverRoom;

    public MessagesAdapter(Context context, ArrayList<Messages> messages,String senderRoom, String receiverRoom){
        this.context=context;
        this.messages=messages;
        this.senderRoom=senderRoom;
        this.receiverRoom=receiverRoom;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType== MSG_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.msg_sent,parent,false);
            return  new SentViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.msg_receive,parent,false);
            return  new ReceiveViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {

        Messages message = messages.get(position);
        if (String.valueOf(sid).equals(message.getSenderId())){
            return  MSG_SENT;
        }
        else {
            return  MSG_RECEIVE;
        }

    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages message = messages.get(position);




        int[] reactions =  new int[]{
                R.drawable.like,
                R.drawable.dislikes,
        };



        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();


        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {

            if (pos < 0)
                return false;

            if (holder.getClass()==SentViewHolder.class){
                SentViewHolder viewHolder =(SentViewHolder)holder ;
                viewHolder.feelingSent.setImageResource(reactions[pos]);
                viewHolder.feelingSent.setVisibility(View.VISIBLE);


            }
            else{
                ReceiveViewHolder viewHolder =(ReceiveViewHolder) holder;
                viewHolder.feelingReceive.setImageResource(reactions[pos]);
                viewHolder.feelingReceive.setVisibility(View.VISIBLE);
            }


            message.setFeeling(pos);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(message.getMessageId()).setValue(message);



            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .child(message.getMessageId()).setValue(message);




            return true;
        });

        if (holder.getClass()==SentViewHolder.class){
            SentViewHolder viewHolder= (SentViewHolder) holder;
           // viewHolder.binding.message.setText(message.getMessage());
            viewHolder.messageSent.setText(message.getMessage());
            viewHolder.messageSentTimestamp.setText(String.valueOf(message.getTimestamp()));

            if (message.getFeeling() >= 0){
//                viewHolder.binding.feelings.setImageResource(reactions[ message.getFeeling()]);
//                viewHolder.binding.feelings.setVisibility(View.VISIBLE);
                viewHolder.feelingSent.setImageResource(reactions[message.getFeeling()]);
                viewHolder.feelingSent.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.feelingSent.setVisibility(View.GONE);
            }


            viewHolder.messageSentLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    popup.onTouch(v,event);
                    popup.showAtLocation(v, Gravity.CENTER,0,0);




                    return false;
                }
            });

        }

        else {
            ReceiveViewHolder viewHolder =(ReceiveViewHolder) holder;
            viewHolder.messageReceive.setText(message.getMessage());
            viewHolder.getMessageReceiveTimeStamp.setText(String.valueOf(message.getTimestamp()));
            viewHolder.userNameReceive.setText(message.getUserName());

            if (message.getFeeling()>=0){
                viewHolder.feelingReceive.setImageResource(reactions[ message.getFeeling()]);
                viewHolder.feelingReceive.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.feelingReceive.setVisibility(View.GONE);
            }

            viewHolder.messageReceiveLayout.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    popup.onTouch(v,event);
                    popup.showAtLocation(v, Gravity.CENTER,0,0);
                    return false;
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class  SentViewHolder extends RecyclerView.ViewHolder{

        TextView  messageSent ,messageSentTimestamp;
        ImageView feelingSent;
        ConstraintLayout messageSentLayout;
     //   MsgSentBinding binding ;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
          //  binding = MsgSentBinding.bind(itemView);
            messageSent=itemView.findViewById(R.id.message);
            feelingSent=itemView.findViewById(R.id.feelings);
            messageSentTimestamp=itemView.findViewById(R.id.timeStamp);
            messageSentLayout=itemView.findViewById(R.id.messageLayout);
        }
    }

    public  class  ReceiveViewHolder extends  RecyclerView.ViewHolder{

       // MsgReceiveBinding binding ;
       TextView  messageReceive ,getMessageReceiveTimeStamp, userNameReceive;
        ImageView feelingReceive;
        ConstraintLayout messageReceiveLayout;
        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
           // binding=MsgReceiveBinding.bind(itemView);
            messageReceive=itemView.findViewById(R.id.message);
            feelingReceive=itemView.findViewById(R.id.feelings);
            getMessageReceiveTimeStamp=itemView.findViewById(R.id.timeStamp);
            userNameReceive=itemView.findViewById(R.id.userName);
            messageReceiveLayout=itemView.findViewById(R.id.messageLayout);
        }
    }
}