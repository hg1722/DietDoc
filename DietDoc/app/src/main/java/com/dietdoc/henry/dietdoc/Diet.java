package com.dietdoc.henry.dietdoc;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by henry on 10/22/17.
 */

public class Diet {
    int id;
    String dietName;
    ArrayList<String> suggestedFoods;
    ArrayList<String> avoidFoods;

    public Diet(){

    }

    public Diet(int id, String dietName, ArrayList<String> suggestedFoods, ArrayList<String> avoidFoods){
        this.id = id;
        this.dietName = dietName;
        this.suggestedFoods = suggestedFoods;
        this.avoidFoods = avoidFoods;
    }

    public int getDietID(){
        return id;
    }

    public String getDietName(){
        return dietName;
    }

    public ArrayList<String> getSuggestedFoods(){
        return suggestedFoods;
    }

    public ArrayList<String> getAvoidFoods(){
        return avoidFoods;
    }
}
