package com.dietdoc.henry.dietdoc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    FragmentManager manager = getSupportFragmentManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ProfileFragment profileFragment = new ProfileFragment();
                    manager.beginTransaction().replace(R.id.content, profileFragment, profileFragment.getTag()).commit();
                    return true;
                case R.id.navigation_suggested:
                    SuggestedFragment suggestedFragment = new SuggestedFragment();
                    manager.beginTransaction().replace(R.id.content, suggestedFragment, suggestedFragment.getTag()).commit();
                    return true;
                case R.id.navigation_avoid:
                    AvoidFragment avoidFragment = new AvoidFragment();
                    manager.beginTransaction().replace(R.id.content, avoidFragment, avoidFragment.getTag()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ProfileFragment profileFragment = new ProfileFragment();
        manager.beginTransaction().replace(R.id.content, profileFragment, profileFragment.getTag()).commit();
    }

}
