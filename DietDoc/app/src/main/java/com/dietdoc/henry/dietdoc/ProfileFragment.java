package com.dietdoc.henry.dietdoc;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    SharedPreferences sharedPref;
    String nameKey;
    String dietName;
    DatabaseReference mAccountRef;
    DatabaseReference mConditionRef;
    TextView userNameView;
    TextView conditionNameView;
    RingProgressBar progressBar;
    int progress;
    TextView progressTitle;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        userNameView = (TextView) rootView.findViewById(R.id.username);
        conditionNameView = (TextView) rootView.findViewById(R.id.conditionName);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        nameKey = sharedPref.getString(getString(R.string.accountStatus), "");
        dietName = sharedPref.getString(getString(R.string.dietName), "");
        mAccountRef = database.getReference("Accounts").child(nameKey);
        mConditionRef = database.getReference("Diets").child(dietName);
        progressTitle = (TextView) rootView.findViewById(R.id.progress_title);
        progressBar = (RingProgressBar) rootView.findViewById(R.id.progress_bar);

        mAccountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("userName").getValue();
                progress = dataSnapshot.child("progress").getValue(Integer.class);
                progressBar.setProgress(progress);
                progressTitle.setText("Weekly Goal: " + progress + "%");
                //Toast.makeText(getActivity(), String.valueOf(progress), Toast.LENGTH_SHORT).show();
                userNameView.setText(name);
                conditionNameView.setText(dietName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //userNameView.setText();

        progressTitle.setText("Weekly Goal: " + progress + "%");
        // Inflate the layout for this fragment
        return rootView;
    }

}
