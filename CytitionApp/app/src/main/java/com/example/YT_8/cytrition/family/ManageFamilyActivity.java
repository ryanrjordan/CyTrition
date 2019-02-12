package com.example.YT_8.cytrition.family;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.app.AppController;
import com.example.YT_8.cytrition.app.Globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/** This page is only accessible by parent/family accounts and admins. It is used for these users to manage their child accounts.
 * @author Ryan */

public class ManageFamilyActivity extends AppCompatActivity {
    private ArrayList<String> children;
    private String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family);

        Globals g = Globals.getInstance();
        user = g.getLoginID();

        //noinspection Convert2Diamond
        children = new ArrayList<String>();
        getChildrenList(user);

        /* Button that starts AddChildActivity. */
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addChildIntent = new Intent(getApplicationContext(),AddChildActivity.class);
                addChildIntent.putExtra("parent_id",user);
                startActivity(addChildIntent);
            }
        });
    }

    /** Volley request(post) to server-side php file that returns list of children linked to user account.
     * @param login_id corresponds to parent_id in table.
     * @see #parseData(String)
     * @see #setChildList(String[])
     */
    private void getChildrenList(final String login_id) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_user_children.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ManageFamilyActivity", response);
                if(response.contains(",")) {
                    String[] parsed = parseData(response);
                    setChildList(parsed);
                } else {
                    Toast.makeText(getApplicationContext(),"You currently have no children linked to your account", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error connecting to database",Toast.LENGTH_SHORT).show();
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
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_retrieve_children");
    }

    /** Function to parse data returned by php file.
     * @param data string containing children's login_ids
     * @return String[] of parsed list of children
     */
    private String[] parseData(String data){
        String[] result;
        result = data.split(",");
        return result;
    }

    /** Takes list of children names and add them to listview via an arrayadapter.
     * @param list list of children.
     */
    private void setChildList(String[] list) {
        children.addAll(Arrays.asList(list));
        //noinspection Convert2Diamond
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, children);
        ListView childListView = (ListView) findViewById(R.id.childListView);
        childListView.setAdapter(arrayAdapter);
        childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent childIntent = new Intent(getApplicationContext(), ManageChildActivity.class);
                childIntent.putExtra("child",children.get(i));
                startActivity(childIntent);
            }
        });
    }

}
