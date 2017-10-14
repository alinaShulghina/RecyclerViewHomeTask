package com.example.alin_.animationht.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.alin_.animationht.R;
import com.example.alin_.animationht.model.User;

import com.example.alin_.animationht.view.fragment.ContactsFragment;

import java.util.List;

/**
 * Created by alin- on 10.10.2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> users;
    private ContactsFragment fragment;

    public UserAdapter(Context context, List<User> users, ContactsFragment fragment) {
        this.context = context;
        this.users = users;
        this.fragment = fragment;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, final int position) {
        final User user = users.get(position);
        holder.message.setText(user.getName());
        Glide.with(context).load(user.getImageId()).apply(new RequestOptions().circleCrop())
                .into(holder.userPhoto);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.userPhoto.setTransitionName("transition" + position);
        }
        holder.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.openContactDetailsFragment(holder.getAdapterPosition(), view.findViewById(R.id.profilePhoto));
            }
        });
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(itemView);

    }

    public List<User> getUsers() {
        return users;
    }

    //removes row from recyclerview
    public void delete(int position) {
        users.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView userPhoto;
        public TextView message;
        public ImageButton deleteButton;


        public UserViewHolder(View view) {
            super(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setClipToOutline(false);
            }
            userPhoto = view.findViewById(R.id.profilePhoto);
            message = view.findViewById(R.id.user_name);
            deleteButton = view.findViewById(R.id.user_delete);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            delete(getAdapterPosition());
        }
    }

}
