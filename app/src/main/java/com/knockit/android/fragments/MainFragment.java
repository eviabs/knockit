package com.knockit.android.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.knockit.android.R;
import com.knockit.android.activities.MainActivity;
import com.knockit.android.activities.RecyclerItemTouchHelper;
import com.knockit.android.adapters.KnockitAdapter;
import com.knockit.android.firebase.FirebaseHelper;
import com.knockit.android.net.KnockitMessage;
import com.knockit.android.utils.LocalPreferences;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private String TAG = MainFragment.class.getSimpleName() + "@asw!";
    private View view = null;

    RecyclerView recyclerViewMessages;
    KnockitAdapter adapter;
    DatabaseReference db;
    FirebaseHelper helper;
    LinearLayout layoutLoading;
    CoordinatorLayout coordinatorLayout;

    /**
     * Default empty Ctor
     */
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //SETUP FIREBASE
        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db, this);

        // We are holding a static member called view. We make sure to inflate the view ONLY ONCE.
        // We also remove the view from it's parent if needed.
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);

            recyclerViewMessages = (RecyclerView) view.findViewById(R.id.recycler_view_messages);
            layoutLoading = (LinearLayout) view.findViewById(R.id.layout_loading);


        } else {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        return view;
    }


    /**
     * Get the LocalPreferences object from the host activity.
     * @return local LocalPreferences object.
     */
    public LocalPreferences getLocalPreferences() {
        return ((MainActivity) getActivity()).getLocalPreferences();
    }

    public void updateList(ArrayList<KnockitMessage> knockitMessages) {

        if (getActivity() == null) {
            return;
        }


        layoutLoading.setVisibility(View.GONE);
        Collections.sort(knockitMessages);
        adapter = new KnockitAdapter(knockitMessages, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewMessages.setLayoutManager(mLayoutManager);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewMessages.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerViewMessages.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewMessages);

    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof KnockitAdapter.MyViewHolder) {

            // backup of removed item for undo purpose
            final KnockitMessage deletedItem = helper.retrieve().get(viewHolder.getAdapterPosition());

            // remove the item from recycler view
            this.getFirebaseHelper().remove(helper.retrieve().get(position));
            coordinatorLayout = getActivity().findViewById(R.id.coordinator_layout);
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout,"Event deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public FirebaseHelper getFirebaseHelper() {
        return helper;
    }
}
