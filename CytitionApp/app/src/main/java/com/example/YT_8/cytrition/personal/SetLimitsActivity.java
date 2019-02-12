package com.example.YT_8.cytrition.personal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.YT_8.cytrition.utils.CalorieMathUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Page where users and user's parents can view/edit their daily calorie intake limit. Weekly value is auto-updated based on daily value picked.
 * @author Ryan
 */
public class SetLimitsActivity extends AppCompatActivity {
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_limits);

        Globals g = Globals.getInstance();
        String parent = g.getLoginID();
        Intent i = getIntent();
        String login_id = "";
        if(i.hasExtra("child")) {
            login_id = i.getStringExtra("child");
            setUserID(login_id);
        } else {
            login_id = g.getLoginID();
            user_id = Integer.toString(g.getUserID());
        }

        TextView titleTV = (TextView) findViewById(R.id.titleTV);
        String start = "Setting calorie limits for " + login_id;
        if(!parent.equals(login_id)) {
            titleTV.setText(start + " as parent.");
        } else {
            titleTV.setText(start);
        }
        getLimitData(login_id);

        Button setLimitButton = (Button) findViewById(R.id.setLimitButton);
        setLimitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText dailyLimitET = (EditText) findViewById(R.id.dailyLimitTV);
                String limit = dailyLimitET.getText().toString();
                sendCalLimit(user_id,limit);
            }
        });
    }

    /** Retrieves user data using volley request to get physical stats to be used to calculate calorie goals.
     * @param name representing login_id in database.
     * @see #getRelevantData(String)
     * @see #displayReccomendation(int[]) */
    private void getLimitData(final String name) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_user_data.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SetLimitsActivity", response);
                if(response.contains("password")) {
                    int[] parsedData = getRelevantData(response);
                    displayReccomendation(parsedData);
                } else {
                    Toast.makeText(getApplicationContext(),"Error finding user in database", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while loading user data",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",name);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_retrieve_limit_data");
    }

    /**
     * Takes string reponse from getLimitData's volley request and returns user's physical stats.
     * @param data sting response
     * @return personal physical stats in int array
     */
    private int[] getRelevantData(String data){
        String[] result;
        result = data.split(",");
        @SuppressWarnings("UnnecessaryLocalVariable") int[] relevant = {Integer.parseInt(result[7]),Integer.parseInt(result[9]),Integer.parseInt(result[11]),Integer.parseInt(result[15])};
        return relevant;
    }

    /**
     * Updates TextViews and EditTexts on page to display proper recommended calorie limits.
     * @param values contains user's physical data
     */
    private void displayReccomendation(int[] values) {
        EditText dailyLimitET = (EditText) findViewById(R.id.dailyLimitTV);
        TextView weeklyLimitTV = (TextView) findViewById(R.id.weeklyLimitTV);
        int dailyCals = CalorieMathUtil.getDailyLimit(values);
        int weeklyCals = CalorieMathUtil.getWeeklyLimit(values);
        dailyLimitET.setText(Integer.toString(dailyCals));
        weeklyLimitTV.setText("Based on daily value: " + weeklyCals);
    }

    /**
     * Volley request to server file. Called if parent is updating child's limit. Sets instance variable to user_id to be used when setting new limit.
     * @param login_id login_id of user whose limit is being updated
     */
    private void setUserID(final String login_id) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_user_id.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SetLimitsActivity2", response);
                if(!response.contains("C")) {
                    user_id = response;
                } else {
                    Toast.makeText(getApplicationContext(),"Error finding user in database", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while loading user ID",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",login_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_retrieve_id");
    }

    /**
     * Volley request to server file. Sends data to update user's daily calorie limit.
     * @param user_id user_id of user getting limit updated
     * @param limit daily calorie limit
     */
    private void sendCalLimit(final String user_id, final String limit) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/update_calorie_limit.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SetLimitsActivity3", response);
                if(response.contains("success")) {
                    Toast.makeText(getApplicationContext(),"Successfully set daily calorie limit to " + limit + " calories.",Toast.LENGTH_SHORT).show();
                    Intent backHome = new Intent(getApplicationContext(), HomePageActivity.class);
                    startActivity(backHome);
                } else {
                    Toast.makeText(getApplicationContext(),"Error updating value in database.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SetLimitsActivity3_Error",error.toString());
                Toast.makeText(getApplicationContext(),"Error while connecting to server.",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("cal_amount",limit);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_send_limit_data");
    }
}
