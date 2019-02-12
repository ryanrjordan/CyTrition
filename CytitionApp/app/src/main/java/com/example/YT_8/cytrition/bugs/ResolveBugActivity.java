package com.example.YT_8.cytrition.bugs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

/** The class' associated activity is accessible only by admins after they click on a bug report to view.
 * @author Ryan
 */

public class ResolveBugActivity extends AppCompatActivity {
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolve_bug);

        /* Report data sent from last activity as extras get handled here. */
        Intent i = getIntent();
        String r = i.getStringExtra("report");
        user = i.getStringExtra("loginID");
        final String[] reportData = parseReport(r);

        /* Displays report data for admin to view. */
        TextView reportIDTV = findViewById(R.id.reportIDTV);
        reportIDTV.setText("Report ID: " + reportData[0] + ",  Posted by " + reportData[1]);
        TextView reportTV = findViewById(R.id.reportTV);
        reportTV.setText(reportData[2]);

        /* Button that admin click when a bug has been resolved or noted. It deletes the report from the database. */
        Button resolveButton = findViewById(R.id.resolveButton);
        resolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resolveBug(reportData[0]);
                Intent homepage = new Intent(getApplicationContext(),HomePageActivity.class);
                homepage.putExtra("loginID",user);
                startActivity(homepage);
            }
        });
    }

    /** Function to parse string containing report data(poster_id,report).
     * @param report string containing all report data in easily parsable form
     * @return string[] of organized/separated data associated with report.
     */
    private String[] parseReport(String report) {
        String delims = ",|:";
        return report.split(delims);
    }

    /** Uses volley request to call server-side php file that deletes row from database table bug_reports that has specified report_id
     * @param id corresponds to report_id in row of database table.
     */
    private void resolveBug(final String id) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/resolve_bug_report.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResolveBugActivity", response);
                if(response.contains("success")) {
                    Toast.makeText(getApplicationContext(),"Report deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Server file failed to update database.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while updating user data",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("report_id",id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_resolve_bug");
    }
}
