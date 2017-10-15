package com.example.alin_.animationht.view.activity;

import android.os.Build;
import android.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.alin_.animationht.R;
import com.example.alin_.animationht.model.User;
import com.example.alin_.animationht.view.fragment.ContactsFragment;
import com.example.alin_.animationht.view.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ContactsFragment.newInstance());
        transaction.addToBackStack("userList");
        transaction.commit();
    }

    public void showFragmentWithTransition(Fragment current, User user, View sharedView, String sharedElementName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ProfileFragment profileFragment = ProfileFragment.newInstance(user,sharedElementName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            current.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
            current.setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));

            profileFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
            profileFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.addSharedElement(sharedView, sharedElementName);
        fragmentTransaction.commit();

    }

    /**
     * function to go back to previous fragment
     */


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() >= 2) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
