package com.example.chat_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private ArrayList<Users> mUserList;
    private Context mContext;
    public OnItemClickListener mListener;
    private String url;

    public interface OnItemClickListener{
        void onItemCLick(int position);
    }
    public Context getItemContext(){
        return mContext;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;

        public UserViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.profile);
            mTextView1 = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemCLick(position);
                        }
                    }
                }
            });
        }
    }

    public UsersAdapter(ArrayList<Users> exampleList, Context context) {
        mUserList = exampleList;
        mContext = context;
//        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends, parent, false);
        UserViewHolder uvh = new UserViewHolder(v, mListener);
        return uvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users currentItem = mUserList.get(position);
//        holder.mImageView.setImageURI(currentItem.getProfile());
        holder.mTextView1.setText(currentItem.getName());
        url = currentItem.getAddress();
        if(url==null)
            holder.mImageView.setImageResource(R.drawable.ic_round_person_24);
        else
            Picasso.get().load(url).fit().centerInside().placeholder(R.drawable.ic_round_person_24).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if(mUserList !=null)
            return mUserList.size();
        return 0;
    }
}
