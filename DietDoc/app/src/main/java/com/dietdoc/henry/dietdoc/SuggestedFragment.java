package com.dietdoc.henry.dietdoc;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestedFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mSuggestedRef;
    DatabaseReference mAccountRef;
    ListView suggestedFoodList;
    ArrayList<String> foodArray = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String dietName;
    String measure = "nf_calories";
    EditText ntlpField;
    String ntlp_input;
    String nameKey;
    int progress;
    double dietMult = 0.1;

    public SuggestedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_suggested, container, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        nameKey = sharedPref.getString(getString(R.string.accountStatus), "");
        dietName = sharedPref.getString(getString(R.string.dietName), "");
        mAccountRef = database.getReference("Accounts").child(nameKey);
        mSuggestedRef = database.getReference("Diets").child(dietName);
        //Toast.makeText(getActivity(), dietName, Toast.LENGTH_SHORT).show();
        ntlpField = (EditText) rootView.findViewById(R.id.ntlp_input);
        ntlpField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ntlpField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ntlp_input = ntlpField.getText().toString();
                    double progressValue = queryNutrition(ntlp_input) * dietMult;
                    if(progress <= (100 - progressValue)){
                        mAccountRef.child("progress").setValue(progress + progressValue);
                    }
                    else{
                        mAccountRef.child("progress").setValue(100);
                    }
                }
                return false;
            }
        });
        suggestedFoodList = rootView.findViewById(R.id.suggested_foods_list);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, foodArray);
        suggestedFoodList.setAdapter(adapter);

        mSuggestedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Diet diet = dataSnapshot.getValue(Diet.class);
                ArrayList<String> suggestedFoods = diet.getSuggestedFoods();
                for(String suggestedFood : suggestedFoods){
                    foodArray.add(suggestedFood);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAccountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progress = dataSnapshot.child("progress").getValue(Integer.class);
                //Toast.makeText(getActivity(), String.valueOf(progress), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        suggestedFoodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double progressValue = queryNutrition(suggestedFoodList.getItemAtPosition(position).toString()) * dietMult;

                if(progress <= (100 - progressValue)){
                    mAccountRef.child("progress").setValue(progress + progressValue);
                }
                else{
                    mAccountRef.child("progress").setValue(100);
                }
            }
        });

        return rootView;
    }

    public double queryNutrition(String query){
        double result = 0.0;
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();
        Response response;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "query=" + query);
        Request request = new Request.Builder()
                .url("https://trackapi.nutritionix.com/v2/natural/nutrients")
                .post(body)
                .addHeader("x-app-id", "4ab28781")
                .addHeader("x-app-key", "4fe401d21ebd0080aafa4711adb9ac36")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "5eedc7cb-b99f-3e40-b2ed-e58c8dc54e97")
                .build();

        try {
            response = client.newCall(request).execute();
            String jsonData = response.body().string();
            JSONObject jObject = new JSONObject(jsonData);
            String value = jObject.getJSONArray("foods").getJSONObject(0).getString(measure);
            Toast.makeText(getActivity(), value + " calories", Toast.LENGTH_SHORT).show();
            result = Double.parseDouble(value);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
