package com.dietdoc.henry.dietdoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SelectDiet extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDietsRef = database.getReference("Diets");
    ListView dietList;
    ArrayList<String> dietArray = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    DatabaseReference mAccountsRef = database.getReference("Accounts");
    ArrayList<String> dietIDList = new ArrayList<>();

    //sample arraylists for suggested and avoid
    //ArrayList<String> badFoods = new ArrayList<>(Arrays.asList("badFood1", "badFood2"));
    //ArrayList<String> suggestedFoods = new ArrayList<>(Arrays.asList("goodFood1", "goodFood2"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_diet);
        dietList = (ListView) findViewById(R.id.conditionList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dietArray);
        dietList.setAdapter(adapter);

        //insertDiet(0, "condition0", suggestedFoods, badFoods);
        //insertDiet(1, "condition1", suggestedFoods, badFoods);
        //insertDiet(2, "condition2", suggestedFoods, badFoods);

        mDietsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Diet diet = dataSnapshot.getValue(Diet.class);
                dietArray.add(diet.dietName);
                dietIDList.add(String.valueOf(diet.id));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        dietList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDietName = dietList.getItemAtPosition(position).toString();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                String nameKey = sharedPref.getString(getString(R.string.accountStatus), "");
                //Toast.makeText(SelectDiet.this, nameKey, Toast.LENGTH_SHORT).show();
                editor.putString(getString(R.string.dietName), selectedDietName);
                editor.commit();

                //Toast.makeText(SelectDiet.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                mAccountsRef.child(nameKey).child("dietID").setValue(dietIDList.get(position));
                //Toast.makeText(SelectDiet.this, currentDiet.getDietID(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectDiet.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public void insertDiet(int id, String dietName, ArrayList<String> suggestedFoods, ArrayList<String> avoidFoods){
        Diet diet = new Diet(id, dietName, suggestedFoods, avoidFoods);
        mDietsRef.child(dietName).setValue(diet);
    }
}
