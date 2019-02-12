package com.example.YT_8.cytrition.tools;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This page is for the nutrition calculator which we likely won't end up implementing.
 * @author Ryan
 */

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        getAPIData2();
    }

    /** Makes a volley request to the Nutritionix API and gets data for cheddar cheese.
     * Currently a dummy function with the intention of testing api functionality. */
    private void getAPIData2() {
        String s = "https://api.nutritionix.com/v1_1/search/cheddar%20cheese?fields=item_name%2Cnf_calories&appId=e624afd3&appKey=4f3918ab8a2504b6097a4546ee7067c4";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, s, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CalulatorActivity", response);
                if(response.contains("{")) {
                    TextView tv = (TextView) findViewById(R.id.fillerTV);
                    tv.setText(response);
                } else {
                    Toast.makeText(getApplicationContext(),"Incorrect returned data.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while GETing api data",Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_api_test");
    }
}
