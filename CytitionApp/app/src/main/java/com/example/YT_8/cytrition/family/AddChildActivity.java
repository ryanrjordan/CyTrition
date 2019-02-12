package com.example.YT_8.cytrition.family;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.app.AppController;
import com.example.YT_8.cytrition.family.ManageFamilyActivity;

import java.util.HashMap;
import java.util.Map;

/** This page is used by parent/family accounts to create connection to their desired child accounts.
 * @author Ryan
 */

public class AddChildActivity extends AppCompatActivity {
    private String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        /* Get parent's login_id as extra from ManageFamilyActivity. */
        Intent i = getIntent();
        user = i.getStringExtra("parent_id");

        /* Button to call addChild(String) with data in EditText as input. */
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText childNameET = (EditText) findViewById(R.id.childNameET);
                String child = childNameET.getText().toString();
                addChild(child);
            }
        });
    }

    /** Volley request(post) that calls server-side php file that adds row to database table that link the parent and the child.
     * If everything worked. It sends the user back to the ManageFamily page.
     * @param childName corresponds to login_id of child account.
     */
    private void addChild(final String childName) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/add_child.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AddChildActivity", response);
                if(response.contains("success")) {
                    Toast.makeText(getApplicationContext(),"Successfully added child!", Toast.LENGTH_SHORT).show();
                    Intent familyIntent = new Intent(getApplicationContext(), ManageFamilyActivity.class);
                    startActivity(familyIntent);
                } else if(response.contains("name")){
                    Toast.makeText(getApplicationContext(),"Error. Make sure child has account first.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Error adding to database.", Toast.LENGTH_SHORT).show();
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
                params.put("parent_id",user);
                params.put("child_id",childName);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_add_child");
    }
}
