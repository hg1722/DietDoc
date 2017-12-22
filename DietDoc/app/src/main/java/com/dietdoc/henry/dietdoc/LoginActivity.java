package com.dietdoc.henry.dietdoc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final String ACCOUNT_SETTING = null;
    private EditText nameField;
    private Button signInButton;
    private String name;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mAccountRef = database.getReference("Accounts");
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("com.dietdoc.henry.dietdoc", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstRun", true)) {
            setupUser();
            prefs.edit().putBoolean("firstRun", false).commit();
        }
        else{
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void setupUser(){
        nameField = (EditText) findViewById(R.id.name);
        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameField.getText().toString();
                String nameKey = mAccountRef.push().getKey();
                User user = new User();
                user.setUserName(name);
                mAccountRef.child(nameKey).setValue(user);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.accountStatus), nameKey);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, SelectDiet.class);
                startActivity(intent);
            }
        });
    }
}
