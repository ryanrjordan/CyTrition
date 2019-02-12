package com.example.YT_8.cytrition.utils;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.app.AppController;

/**
 * Currently used to store API connection information. Serves no functional purpose from user's perspective.
 * @author Ryan
 */
public class APIUtil {
    private static final String KEY = "4f3918ab8a2504b6097a4546ee7067c4";
    private static final String ID = "e624afd3";


    private String getAPIData(String ingredientName) {
        String s = "https://api.nutritionix.com/v1_1/search/cheddar%20cheese?fields=item_name%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat&appId=e624afd3&appKey=4f3918ab8a2504b6097a4546ee7067c4";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, s, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CalulatorActivity", response);
                if(response.contains("{")) {

                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_api_data");
        return "";
    }

}
