package com.example.YT_8.cytrition.app;

import android.app.Application;

/**
 * Created by Ravi on 3/4/2018.
 */

/**
 * A Singleton class to store data for a user between multiple activities
 */
public class Globals {
    private static Globals instance;

    // Global variable
    private int userID;
    private String loginID;
    private int user_type;
    private String recipeName;
    private String ingredientName;
    private String childID;
    private String parentID;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setUserID(int userID){
        this.userID = userID;
    }
    public int getUserID(){
        return this.userID;
    }
    public void setLoginID(String loginID){
        this.loginID = loginID;
    }
    public String getLoginID(){
        return this.loginID;
    }
    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }
    public int getUser_type() {
        return this.user_type;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public String getChildID() {
        return childID;
    }
    public void setChildID(String childID) {
        this.childID = childID;
    }
    public String getIngredientName() {
        return ingredientName;
    }
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    /**
     * Ensure that there is only one instance of this class during runtime
     * @return an instance of this class
     */
    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }


}
