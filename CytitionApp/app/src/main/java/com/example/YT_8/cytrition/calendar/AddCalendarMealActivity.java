package com.example.YT_8.cytrition.calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.HomePageActivity;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.app.AppController;
import com.example.YT_8.cytrition.app.Globals;
import com.example.YT_8.cytrition.family.ManageFamilyActivity;
import com.example.YT_8.cytrition.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** This page will display all user recipes that can be added to consumed meal activity on calendar.
 * This is still a work in progress.
 * @author Ryan
 */

public class AddCalendarMealActivity extends AppCompatActivity {
    private String user = "";
    private String date = "";
    private int meal_type = -1;
    private ArrayList<String> recipes;
    private ArrayList<String> inMealRecipes;
    private ArrayAdapter<String> adapterR;
    private ArrayAdapter<String> adapterM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar_meal);

        /* Get extras from calendar activity. */
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        date = intent.getStringExtra("date");
        String temp = intent.getStringExtra("mealType");
        meal_type = Integer.parseInt(temp);
        String[] types = {"breakfast", "lunch", "snack", "dinner"};

        /* Displays what action user is performing for clarification. */
        TextView titleTV = (TextView) findViewById(R.id.titleTV);
        String message = "Adding " + types[meal_type] + " to " + user + "'s calendar on " + date;
        titleTV.setText(message);

        recipes = new ArrayList<>();
        inMealRecipes = new ArrayList<>();

        ListView recipeListView = (ListView) findViewById(R.id.recipeListView);
        adapterR = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,recipes);
        recipeListView.setAdapter(adapterR);
        ListView mealListView = (ListView) findViewById(R.id.mealListView);
        adapterM = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,inMealRecipes);
        mealListView.setAdapter(adapterM);

        getRecipeList(Globals.getInstance().getLoginID());

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //add item to mealList
                String rec = recipes.get(i);
                inMealRecipes.add(rec);
                adapterM.notifyDataSetChanged();
            }
        });

        mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //remove item from list
                inMealRecipes.remove(i);
                adapterM.notifyDataSetChanged();
            }
        });

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arr = inMealRecipes.toArray(new String[inMealRecipes.size()]); //array of recipes
                String[] ids = new String[arr.length];
                int i = 0;
                for(String r_id : arr) {
                    ids[i] = arr[i].split(":")[0]; //converts  'id: name' to just return 'id'
                    i++;
                }
//                Toast.makeText(getApplicationContext(), Arrays.toString(ids),Toast.LENGTH_SHORT).show();
                addMealEvent(user,date,Integer.toString(meal_type),ids);
            }
        });
    }

    /**
     * Returns list of recipes associated with active user's account. Meaning if the parent is updated a child's calendar, the parent's recipes appear.
     * @param real_user login_id of active user(may be parent).
     */
    private void getRecipeList(final String real_user) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/ryans_recipe_retriever.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AddCalendarMealActivity/getRecipeList", response);
                if(response.contains(":")) {
                    String[] parsed = parseRecipes(response);
                    updateList(parsed);
                } else {
                    Toast.makeText(getApplicationContext(),"Error retrieving recipes from database", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AddCalendarMealActivity/getRecipeList", error.toString());
                Toast.makeText(getApplicationContext(),"Error connecting to database.",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",real_user);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_cal_retrieve_recipes");
    }

    /**
     * Adds data to calendar_meal_event table and gets new meal_id as response to be used later.
     * @param login_id login_id of user who consumed meal.
     * @param date date meal was consumed in MM/DD/YYYY format.
     * @param meal_type integer representing breakfast, lunch, snack, or dinner.
     */
    private void addMealEvent(final String login_id, final String date, final String meal_type, final String[] recipe_ids) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/add_meal_event.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AddCalendarMealActivity/addMealEvent", response);
                if(response.contains("")) { //response should be meal_id
//                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    try {
                        creationLooper(response,recipe_ids);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"Meal successfully logged for " + date,Toast.LENGTH_SHORT).show();
                    Intent homePageIntent = new Intent(getApplicationContext(), HomePageActivity.class);
                    startActivity(homePageIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"Error adding meal to database", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AddCalendarMealActivity/addMealEvent", error.toString());
                Toast.makeText(getApplicationContext(),"Error connecting to database.",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",login_id);
                params.put("date",date);
                params.put("meal_type",meal_type);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_add_meal_event");
    }

    /**
     * Used to update meal_composition table. It associates recipes with a meal.
     * @param meal_id id of created meal event.
     * @param recipe_id id of a recipe that makes up the meal event.
     */
    private void createMeal(final String meal_id, final String recipe_id, final int count) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/create_meal.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AddCalendarMealActivity/createMeal", response);
                if(!response.contains("success")) {
                    Toast.makeText(getApplicationContext(),"Error adding meal component to database.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AddCalendarMealActivity/createMeal", error.toString());
                Toast.makeText(getApplicationContext(),"Error connecting to database.",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("meal_id",meal_id);
                params.put("recipe_id",recipe_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_create_meal" + count);
    }

    /**
     * Called in addMealEvent() and takes it's Volley response as input for meal_id.
     * Contains loop that call create meal over and over for each recipe within the new meal.
     * @param meal_id id of created meal event.
     * @param r_ids recipe_ids of recipes that make up the meal event.
     */
    private void creationLooper(String meal_id, String[] r_ids) throws InterruptedException {
        int count = 1;
        for(String recipe : r_ids) {
            Thread.sleep(500);
            createMeal(meal_id,recipe,count);
            count++;
        }
    }

    private String[] parseRecipes(String response) {
        return response.split(",");
    }

    private void updateList(String[] arr) {
        Collections.addAll(recipes, arr);
        adapterR.notifyDataSetChanged();
    }
}
