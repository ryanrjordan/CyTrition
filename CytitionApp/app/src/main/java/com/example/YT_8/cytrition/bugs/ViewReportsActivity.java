package com.example.YT_8.cytrition.bugs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.app.AppController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** This page is only viewable by admins. Pending bug reports are listing on this page.
 * @author Ryan
 */

public class ViewReportsActivity extends AppCompatActivity {
    private String user;
    private ArrayList<String> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        Intent i = getIntent();
        user = i.getStringExtra("loginID");

        //noinspection Convert2Diamond
        reportList = new ArrayList<String>();
        getReportList(user);
    }

    /** Volley request that returns bug reports, if there are any pending.
     * @param admin login_id of logged in admin
     * @see #parseData(String)
     * @see #setReportList(ArrayList)
     */
    private void getReportList(final String admin) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_bug_reports.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ViewReportsActivity", response);
                if(response.contains(",")) {
                    ArrayList<String> parsed = parseData(response);
                    setReportList(parsed);
                } else {
                    Toast.makeText(getApplicationContext(),"Error finding reports in database", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error connecting to database",Toast.LENGTH_SHORT).show();
                error.getStackTrace();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",admin);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_retrieve_bugs");
    }

    /** Method that takes long string of all bug report data from database.
     * @param data string containing data from all bug reports. Needs to be parsed.
     * @return ArrayList<String> paresed list of pending report data.
     */
    private ArrayList<String> parseData(String data){
        @SuppressWarnings("Convert2Diamond") ArrayList<String> reportInfo = new ArrayList<String>();
        String[] reports = data.split("-");
        for(String sub: reports) {
            String[] temp = sub.split(",");
            reportInfo.add(temp[1]);
            reportInfo.add(temp[2]);
            reportInfo.add(temp[3]);
        }
        return reportInfo;
    }

    /** Method that takes parsed list of bug reports and organizes them to appear in an ordered list on screen.
     * Also, makes items in listview clickable to transfer to resolve activity.
     * @param list list of strings where each string contains data from 1 bug report.
     */
    private void setReportList(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i += 3) {
            reportList.add(list.get(i) + ":" + list.get(i+1) + ","+ list.get(i+2));
        }
        //noinspection Convert2Diamond
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reportList);
        ListView reportListView = (ListView) findViewById(R.id.reportListView);
        reportListView.setAdapter(arrayAdapter);
        reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent resolveInent = new Intent(getApplicationContext(),ResolveBugActivity.class);
                resolveInent.putExtra("report",reportList.get(i));
                resolveInent.putExtra("loginID",user);
                startActivity(resolveInent);
            }
        });
    }
}
