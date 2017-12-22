package com.dietdoc.henry.dietdoc;

/**
 * Created by henry on 10/21/17.
 */

public class User {
    int userID;
    String userName;
    int dietID;
    int progress;

    public User(){

    }

    public int getUserID(){
        return userID;
    }

    public void setUserID(int userID){
        this.userID = userID;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public int getDietID(){
        return dietID;
    }

    public void setDietID(int dietID){
        this.dietID = dietID;
    }

    public int getProgress(){
        return progress;
    }

    public void setProgress(int progress){
        this.progress = progress;
    }
}
