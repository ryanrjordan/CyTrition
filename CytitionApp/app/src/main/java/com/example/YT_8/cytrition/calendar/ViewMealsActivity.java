package com.example.YT_8.cytrition.calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.app.AppController;

import java.util.HashMap;
import java.util.Map;

/**
 * Displays meals consumed/logged for the selected day by the user or user's child.
 * @author Ryan
 */
public class ViewMealsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meals);

        Intent intent = getIntent();
        String userLogin = intent.getStringExtra("userLogin");
        String selectedDate = intent.getStringExtra("date");

        TextView titleTV = (TextView) findViewById(R.id.titleTV);
        String titleString = "Viewing " + userLogin + "'s consumed/scheduled meals for " + selectedDate + ".";
        titleTV.setText(titleString);

        updateMealDisplays(userLogin, selectedDate);
    }

    /**
     * Volley request to PHP file that returns meal_type and meal_id of all meals consumed on the selected date.
     * @param user login_id of user who consumed or is going to consume the meal.
     * @param date the date the meal was consumed in MM/DD/YYYY format.
     */
    private void updateMealDisplays(final String user, final String date) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_meals_for_date.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ViewMealsActivity", response);
                if(response.contains("-")) {
                    handleMealData(response);
                } else {
                    Toast.makeText(getApplicationContext(),"Couldn't find any logged data for " + date + " in database.", Toast.LENGTH_SHORT).show();
                }
                displayTempData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ViewMealsActivity", error.toString());
                Toast.makeText(getApplicationContext(),"Error while contact server",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",user);
                params.put("date",date);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_get_meal_data_for_day");
    }

    /**
     * Parses meal event data for selected day.
     * @param data response from server in <meal_type>-<meal_id>,<meal_type... format, if not null.
     */
    private void handleMealData(String data) {
        //data/response format should be  <meal_type>-<meal_id>,<meal_type.......
        String[] parsed = data.split(",");
        for (String p : parsed) {
            String[] temp = p.split("-");
            getSpecificMealData(temp[1], temp[0]);
        }
    }

    /**
     * Sets all TextViews that can show recipes from meal event to say 'Did not eat?'.
     * This is replaced, if there is logged meal data for a given meal slot.
     */
    private void displayTempData() {
        TextView breakfastTV = (TextView) findViewById(R.id.breakfastTV);
        TextView lunchTV = (TextView) findViewById(R.id.lunchTV);
        TextView snackTV = (TextView) findViewById(R.id.snackTV);
        TextView dinnerTV = (TextView) findViewById(R.id.dinnerTV);
        breakfastTV.setText("Did not eat?");
        lunchTV.setText("Did not eat?");
        snackTV.setText("Did not eat?");
        dinnerTV.setText("Did not eat?");
    }

    /**
     * Finds all recipe_name's associated with meal_id. PHP script does most of the work.
     * @param meal_id primary key for each meal.
     */
    private void getSpecificMealData(final String meal_id, final String meal_type) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_meal_composition.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ViewMealsActivity2", response);
                if(response.contains(",")) {
                    displayProperData(Integer.parseInt(meal_type),response);
                } else {
                    Toast.makeText(getApplicationContext(),"Error finding meal in database", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ViewMealsActivity2", error.toString());
                Toast.makeText(getApplicationContext(),"Error while loading recipe data",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("meal_id",meal_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_get_meal_data");
    }

    /**
     * Takes list of recipes and meal type and updates display for correct meal type.
     * @param meal_type distinguishes what type of meal to update the display of(breakfast,lunch,etc.).
     * @param response String of recipe_names that makeup given meal separated by commas.
     */
    private void displayProperData(int meal_type, String response) {
        if(meal_type==0) {
            TextView breakfastTV = (TextView) findViewById(R.id.breakfastTV);
            breakfastTV.setText(response.substring(0,response.length()-1));
        } else if(meal_type==1) {
            TextView lunchTV = (TextView) findViewById(R.id.lunchTV);
            lunchTV.setText(response.substring(0,response.length()-1));
        } else if(meal_type==2) {
            TextView snackTV = (TextView) findViewById(R.id.snackTV);
            snackTV.setText(response.substring(0,response.length()-1));
        } else { //meal_type==3
            TextView dinnerTV = (TextView) findViewById(R.id.dinnerTV);
            dinnerTV.setText(response.substring(0,response.length()-1));
        }
    }
}
