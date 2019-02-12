package com.example.YT_8.cytrition.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.app.AppController;

import org.json.JSONObject;

/**
 * Used for testing new Spring functionality without altering existing Android code.
 * @author Ryan
 */
public class SpringTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring_test);

        Button testButton = (Button) findViewById(R.id.runTestButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // put test function calls here
                getSpringData();
            }
        });
    }

    /**
     * Currently used to retrieve all user's data from database with a volley request and display it with a Toast.
     */
    private void getSpringData() {
        String url = "http://10.31.91.100:8080/cy/all";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("SpringTestActivity", response.toString());
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("SpringTestActivity","Error connecting to Spring: " + error.getStackTrace());
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,"tag_json_signup");
    }
}
