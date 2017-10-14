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
import com.example.alin_.animationht.view.fragment.ContactsFragment;

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

    public void showFragmentWithTransition(Fragment current, Fragment newFragment, View sharedView, String sharedElementName) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            current.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
            current.setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));

            newFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
            newFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
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
