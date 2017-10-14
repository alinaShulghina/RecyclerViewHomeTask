package com.example.alin_.animationht.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.alin_.animationht.R;
import com.example.alin_.animationht.model.User;

/**
 * Created by alin- on 13.10.2017.
 */

public class ProfileFragment extends Fragment {

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        final ImageView imageView = rootView.findViewById(R.id.profilePhoto);
        final TextView textView = rootView.findViewById(R.id.profileUserName);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String transitionName = bundle.getString("transitionName");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setTransitionName(transitionName);
            }
            User user = (User) bundle.getSerializable("user");
            int imageId = user.getImageId();
            imageView.setImageResource(imageId);
            textView.setText(user.getName());
        }
        return rootView;
    }
}
