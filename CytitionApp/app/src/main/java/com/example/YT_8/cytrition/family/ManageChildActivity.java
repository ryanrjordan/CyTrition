package com.example.YT_8.cytrition.family;

import android.content.Intent;
import android.provider.Settings;
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
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.food.RecipeActivity;
import com.example.YT_8.cytrition.personal.SetLimitsActivity;
import com.example.YT_8.cytrition.app.AppController;
import com.example.YT_8.cytrition.app.Globals;
import com.example.YT_8.cytrition.calendar.CalendarActivity;

import java.util.HashMap;
import java.util.Map;

/** This activity is for parents to manage one selected child.
 * @author Ryan */

public class ManageChildActivity extends AppCompatActivity {
    private String parent = "";
    private String child = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_child);

        Intent i = getIntent();
        Globals g = Globals.getInstance();
        parent = g.getLoginID();
        child = i.getStringExtra("child");
        TextView titleTV = findViewById(R.id.titleTV);
        titleTV.setText("Currently Managing Child: " + child);

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder alertBuild = new AlertDialog.Builder(getApplicationContext());
//                alertBuild.setMessage("Are you sure you want to delete this child?");
//                alertBuild.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // call delete method
//                    }
//                });
//                alertBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                AlertDialog alert = alertBuild.create();
//                alert.show();
                deleteChild(parent,child);
                Intent temp = new Intent(getApplicationContext(), ManageFamilyActivity.class);
                startActivity(temp);
            }
        });

        Button calendarButton = findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calIntent = new Intent(getApplicationContext(), CalendarActivity.class);
                calIntent.putExtra("child",child);
                startActivity(calIntent);
            }
        });

        Button calLimitButton = findViewById(R.id.calLimitButton);
        calLimitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent limitIntent = new Intent(getApplicationContext(), SetLimitsActivity.class);
                limitIntent.putExtra("child",child);
                startActivity(limitIntent);
            }
        });

        Button childRecipe = findViewById(R.id.manage_child_recipe);
        childRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                //intent.putExtra("child",child);
                Globals g = Globals.getInstance();
                g.setParentID(parent);
                g.setChildID(child);
                g.setState("child");
                g.setLoginID(g.getChildID());
                startActivity(intent);
            }
        });
    }

    /**
     * Volley request to server that deletes a parent-child connection from database.
     * @param parentName login_id of parent
     * @param childName login_id of child
     */
    private void deleteChild(final String parentName, final String childName) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/delete_child.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ManageChildActivity", response);
                if(response.contains("success")) {
                    Toast.makeText(getApplicationContext(),"Child deleted successfully!", Toast.LENGTH_SHORT).show();
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
                params.put("parent_id",parentName);
                params.put("child_id",childName);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_delete_child");
    }
}
