package com.bku.cnpm19.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.cnpm19.R;
import com.bku.cnpm19.ViewProfile;
import com.bku.cnpm19.info.ImageUploadInfo;
import com.bku.cnpm19.info.UserInfo;
import com.bku.cnpm19.viewimage.view.adapter.ProfileAdapter;
import com.bku.cnpm19.viewimageprofile.RecyclerViewAdapter;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Welcome on 4/8/2018.
 */

public class ViewUserAdapter extends RecyclerView.Adapter<ViewUserAdapter.ViewHolder> {
    static Context context;
    List<UserInfo> AllUserInfo;


    public ViewUserAdapter(Context context, List<UserInfo> TempList) {

        this.AllUserInfo = TempList;

        this.context = context;
    }

    @Override
    public ViewUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_list, parent, false);

        ViewUserAdapter.ViewHolder viewHolder = new ViewUserAdapter.ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewUserAdapter.ViewHolder holder, int position) {
        UserInfo userInfo = AllUserInfo.get(position);

        //   holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide.with(context).load(userInfo.getAvatarURL()).into(holder.avatarUser);
        holder.userName.setText(userInfo.getUsername());

        holder.email.setText(userInfo.getEmail());

    }

    @Override
    public int getItemCount() {

        return AllUserInfo.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatarUser;
        public TextView userName,email;

        public ViewHolder(View itemView) {
            super(itemView);

            avatarUser = (ImageView) itemView.findViewById(R.id.profile_image);

            userName = (TextView) itemView.findViewById(R.id.username);

            email=(TextView) itemView.findViewById(R.id.email);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent intent =  new Intent(context, ViewProfile.class);
                        intent.putExtra("email", email.getText().toString());
                        intent.putExtra("username",userName.getText().toString());
                        context.startActivity(intent);

                }
            });
        }
    }
}
