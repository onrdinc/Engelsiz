package com.example.engelsiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Posts> posts;


    public MyAdapter(Context c, ArrayList<Posts> p){
        context = c;
        posts = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_listitem,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.username.setText(posts.get(position).getUserName());
        holder.helpType.setText(posts.get(position).getHelpType());
        holder.helpQuantity.setText(posts.get(position).getHelpQuantity());
        holder.helpAddress.setText(posts.get(position).getHelpAddress());
        holder.helpPhone.setText(posts.get(position).getHelpPhone());
        holder.user_id.setText(posts.get(position).getUserId());
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username, helpType, helpQuantity, helpAddress, helpPhone, user_id;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.displayName);
            helpType = itemView.findViewById(R.id.txtDisplayType);
            helpQuantity = itemView.findViewById(R.id.txtDisplayQuantity);
            helpAddress = itemView.findViewById(R.id.txtDisplayAddress);
            helpPhone = itemView.findViewById(R.id.txtDisplayPhone);
            user_id = itemView.findViewById(R.id.txtUserId);

        }
    }
}
