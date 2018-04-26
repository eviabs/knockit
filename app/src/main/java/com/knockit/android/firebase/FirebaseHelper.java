package com.knockit.android.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.knockit.android.activities.MainActivity;
import com.knockit.android.fragments.MainFragment;
import com.knockit.android.net.KnockitMessage;

import java.util.ArrayList;

public class FirebaseHelper {

    private DatabaseReference db;
    private ArrayList<KnockitMessage> knockitMessages = new ArrayList<>();
    private MainFragment activity;

    public FirebaseHelper(DatabaseReference db, final MainFragment activity) {
        this.db = db;
        this.activity = activity;


        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                }

                fetchData(dataSnapshot);
                activity.updateList(retrieve());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
                activity.updateList(retrieve());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
                activity.updateList(retrieve());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
                activity.updateList(retrieve());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // WRITE
    public boolean add(KnockitMessage knockitMessage) {
        boolean added = false;

        if (knockitMessage == null) {
            added = false;
        } else {
            try {
                db.child("KnockitMessage").push().setValue(knockitMessage);
                added = true;

            } catch (DatabaseException e) {
                e.printStackTrace();
                added = false;
            }
        }

        return added;
    }

    // REMOVE
    public boolean remove(KnockitMessage knockitMessage) {
        boolean removed = false;

        if (knockitMessage == null) {
            removed = false;
        } else {
            try {
                Query query = db.child("KnockitMessage").orderByChild("time").equalTo(knockitMessage.getTime());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                removed = true;

            } catch (DatabaseException e) {
                e.printStackTrace();
                removed = false;
            }
        }

        return removed;
    }

    // READ
    public ArrayList<KnockitMessage> retrieve() {
        return this.knockitMessages;
    }

    private void fetchData(DataSnapshot dataSnapshot) {
        knockitMessages.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            knockitMessages.add(ds.getValue(KnockitMessage.class));
        }
    }

    public static boolean addOutsideOfApp(DatabaseReference db, KnockitMessage knockitMessage) {
        boolean saved;

        if (knockitMessage == null) {
            saved = false;
        } else {
            try {
                db.child("KnockitMessage").push().setValue(knockitMessage);
                saved = true;

            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }
        }

        return saved;
    }
}