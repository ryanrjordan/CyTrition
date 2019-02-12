package com.example.YT_8.cytrition.food;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.YT_8.cytrition.app.Globals;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravi on 2/16/2018.
 */

/**
 * Displays the nutritional information of the selected ingredient
 */
public class IngredientActivity extends AppCompatActivity {

    String user;
    String recipeName;
    String ingredientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        Globals g = Globals.getInstance();
        recipeName = g.getRecipeName();
        ingredientName = g.getIngredientName();
        Intent i = getIntent();
        if(i.hasExtra("child")) {
            user = i.getStringExtra("child");
        } else {
            user = g.getLoginID();
        }

        TextView ingredientNameText = (TextView) findViewById(R.id.ingredient_name);
        ingredientNameText.setText(ingredientName);

        Button deletebtn = (Button) findViewById(R.id.ingredient_delete);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteIngredient(user, ingredientName, recipeName);
                Intent intent = new Intent(getApplicationContext(),SingleRecipeActivity.class);
                intent.putExtra("child", user);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),SingleRecipeActivity.class);
        startActivity(intent);
    }

    private void deleteIngredient(final String loginID, final String ingredientName, final String recipeName) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/delete_ingredient.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("IngredientActivity", response);
                if(response.contains("yes")) {
                    Toast.makeText(getApplicationContext(), "Deleted Ingredient", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed to Delete", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while POSTing ingredients name",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("recipe_name",recipeName);
                params.put("login_id", loginID);
                params.put("ingredient_name", ingredientName);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "delete_recipe");
    }



}
