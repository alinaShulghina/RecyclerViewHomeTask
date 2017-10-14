package com.example.alin_.animationht.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alin_.animationht.R;
import com.example.alin_.animationht.animator.UserItemAnimator;
import com.example.alin_.animationht.adapters.UserAdapter;
import com.example.alin_.animationht.model.User;
import com.example.alin_.animationht.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by alin- on 13.10.2017.
 */

public class ContactsFragment extends Fragment {
    private Context context;
    private UserAdapter listAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private View rootView;
    static ArrayList<User> users;
    int i = 0;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
            recyclerView = rootView.findViewById(R.id.users_list_view);
            context = getActivity();
            users = new ArrayList<>();
            listAdapter = new UserAdapter(rootView.getContext(), users, this);
            //drag and drop
            ItemTouchHelper.Callback touchHelperCallback = new ItemTouchHelper.Callback() {
                @Override
                public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN | ItemTouchHelper.UP |
                            ItemTouchHelper.START | ItemTouchHelper.END);
                }

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    Collections.swap(listAdapter.getUsers(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    listAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                }
            };
            ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
            touchHelper.attachToRecyclerView(recyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
            recyclerView.setLayoutManager(layoutManager);
            UserItemAnimator animator = new UserItemAnimator();
            animator.setChangeDuration(1000);
            animator.setRemoveDuration(1000);
            animator.setAddDuration(2000);
            recyclerView.setItemAnimator(animator);
            recyclerView.setAdapter(listAdapter);

            addButton = rootView.findViewById(R.id.fab_add_user);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    users.add(getData().get(i));
                    listAdapter.notifyItemInserted(i);
                    i++;
                }
            });
        }
        return rootView;
    }

    public void openContactDetailsFragment(int position, View view) {
        User user = users.get(position);
        Bundle bundle = new Bundle();
        String transitionName = "transition" + position;
        bundle.putString("transitionName", transitionName);
        bundle.putSerializable("user", user);
        ProfileFragment profileFragment = ProfileFragment.newInstance();
        profileFragment.setArguments(bundle);
        ((MainActivity) context).showFragmentWithTransition(ContactsFragment.this, profileFragment, view, transitionName);
    }

    private ArrayList<User> getData() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(0, R.drawable.kim, "Kim"));
        users.add(new User(1, R.drawable.kim, "Kim"));
        users.add(new User(2, R.drawable.kim, "Kim"));
        users.add(new User(3, R.drawable.kim, "Kim"));
        users.add(new User(4, R.drawable.kim, "Kim"));
        users.add(new User(5, R.drawable.kim, "Kim"));
        return users;
    }
}
