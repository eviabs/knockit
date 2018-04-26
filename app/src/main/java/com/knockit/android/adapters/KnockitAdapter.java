package com.knockit.android.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.knockit.android.R;
import com.knockit.android.activities.MainActivity;
import com.knockit.android.fragments.MainFragment;
import com.knockit.android.net.KnockitMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KnockitAdapter extends RecyclerView.Adapter<KnockitAdapter.MyViewHolder> {

    private List<KnockitMessage> knockitMessageList;
    private MainFragment activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView type, time;
        public ImageView image;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            type = (TextView) view.findViewById(R.id.textViewMessageType);
            time = (TextView) view.findViewById(R.id.textViewMessageTime);
            image = (ImageView) view.findViewById(R.id.imageViewMessageImage);

            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public KnockitAdapter(List<KnockitMessage> knockitMessageList, MainFragment activity) {
        this.knockitMessageList = knockitMessageList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.knockit_message_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        KnockitMessage knockitMessage = knockitMessageList.get(position);
        holder.type.setText("Type: " + knockitMessage.getMessageType());

        Date date = new Date(knockitMessage.getTime());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String dateFormatted = formatter.format(date);
        holder.time.setText("  Time: " + dateFormatted);

        //holder.image.setImageDrawable();
    }

    @Override
    public int getItemCount() {
        return knockitMessageList.size();
    }

    public void restoreItem(KnockitMessage deletedItem) {
        activity.getFirebaseHelper().add(deletedItem);
    }

}