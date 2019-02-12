package com.example.YT_8.cytrition.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.YT_8.cytrition.HomePageActivity;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.app.AppController;
import com.example.YT_8.cytrition.app.Globals;
import com.example.YT_8.cytrition.food.RecipeActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravi on 2/20/2018.
 */

/**
 * Allows the user to inout a new recipe name and add it to their recipe list
 */
public class AddRecipeActivity extends AppCompatActivity {

    String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);


        Globals g = Globals.getInstance();
        Intent i = getIntent();
        if(i.hasExtra("child")) {
            user = i.getStringExtra("child");
        } else {
            user = g.getLoginID();
        }

        Button ok = (Button) findViewById(R.id.add_recipe_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText recipeName = (EditText) findViewById(R.id.add_recipe_name);
                AddRecipe(user, recipeName.getText().toString());
                Intent intent = new Intent(getApplicationContext(),RecipeActivity.class);
                intent.putExtra("child", user);
                startActivity(intent);
            }
        });

        Button cancel = (Button) findViewById(R.id.add_recipe_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RecipeActivity.class);
                intent.putExtra("child", user);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),RecipeActivity.class);
        startActivity(intent);
    }

    /**
     * Posts the recipe to the user in the database
     * @param login_id the user name for the current account
     * @param recipeName the name of the recipe to be appended to the user
     */
    private void AddRecipe(final String login_id, final String recipeName) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/add_recipe_name.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AddRecipeActivity", response);
                if(response.contains("yes")) {
                    Toast.makeText(getApplicationContext()," Added Recipe", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Can not add Recipe", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while POSTing recipe name",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id", login_id);
                params.put("recipe_name", recipeName);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "add_recipe");
    }


}
