package com.example.YT_8.cytrition.bugs;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.HashMap;
import java.util.Map;

/** This page is for submitting bug reports. It is only accessible by non-admins.
 * @author Ryan
 */

public class SubmitBugActivity extends AppCompatActivity {
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bug);

        Globals g = Globals.getInstance();
        user = g.getLoginID();
        if(user==null) {
            Intent i = getIntent();
            i.getStringExtra("loginID");
        }

        /*Sends edittext data to database if not too long. */
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText reportEditText = findViewById(R.id.reportEditText);
                String report = reportEditText.getText().toString();
                if(report.length()>100) { //if report is too long
                    Toast.makeText(getApplicationContext(),"Bug report too long.",Toast.LENGTH_SHORT).show();
                } else { //if report is correct length
                    sendReport(report,user);
//                    sendReportSpring(report,user);
                    Intent homepage = new Intent(getApplicationContext(),HomePageActivity.class);
                    startActivity(homepage);
                }
            }
        });
        /* Updates character counter */
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override //This is the only useful method from the abstract class
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                TextView counterTV = findViewById(R.id.counterTV);
                counterTV.setText("Characters: " + String.valueOf(charSequence.length()));
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        EditText reportEditText = findViewById(R.id.reportEditText);
        reportEditText.addTextChangedListener(tw);
    }

    /** Volley request to php file to insert bug report into database.
     * @param report contains actual bug report text.
     * @param poster corresponds to poster_id(login_id of reporter).
     */
    private void sendReport(final String report, final String poster) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/submit_bug_report.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SubmitBugActivity", response);
                if(response.contains("success")) {
                    Toast.makeText(getApplicationContext(),"Bug reported successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Server file failed to update database.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while updating bug_reports table",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("poster_id",poster);
                params.put("report_text",report);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_submit_bug");
    }

    /** Volley request to Spring file to insert bug report into database.
     * @param report contains actual bug report text.
     * @param poster corresponds to poster_id(login_id of reporter).
     */
    private void sendReportSpring(final String report, final String poster) {
        String url = "http://10.31.30.177:8080/cy/bugreports/" + poster + "/" + report;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SubmitBugActivity2", response);
                if(response.contains("Saved")) {
                    Toast.makeText(getApplicationContext(),"Bug reported successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Server file failed to update database.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while updating bug report table: " + error.toString(),Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return null;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_submit_bug_spring");
    }
}
