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
import com.example.YT_8.cytrition.food.SingleRecipeActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravi on 2/26/2018.
 */


/**
 * Activity for handling the addition of a new ingredient to a already existing recipe
 */
public class AddIngredientActivity extends AppCompatActivity {

    String user = "";
    String recipeName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        Globals g = Globals.getInstance();
        recipeName = g.getRecipeName();
        Intent i = getIntent();
        if(i.hasExtra("child")) {
            user = i.getStringExtra("child");
        } else {
            user = g.getLoginID();
        }

        Button ok = (Button) findViewById(R.id.add_ingredient_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ingredientName = (EditText) findViewById(R.id.add_ingredient_name);
                addIngredient(user, recipeName, ingredientName.getText().toString());
                Intent intent = new Intent(getApplicationContext(),SingleRecipeActivity.class);
                intent.putExtra("child", user);
                startActivity(intent);
            }
        });

        Button cancel = (Button) findViewById(R.id.add_ingredient_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    /**
     * Posts the new ingredient to the database
     * @param loginID the users name for the current account
     * @param recipeName the name of the recipe the ingredient is to be appended to
     * @param ingredientName the name of the ingredient added
     */
    private void addIngredient(final String loginID, final String recipeName, final String ingredientName) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/add_ingredient_name.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AddIngredientActivity", response);
                if(response.contains("yes")) {
                    Toast.makeText(getApplicationContext()," Added Ingredient", Toast.LENGTH_SHORT).show();
                } else if(response.contains("failed1")) {
                    Toast.makeText(getApplicationContext(),"Processed Request but failed response1", Toast.LENGTH_SHORT).show();
                } else if(response.contains("failed2")) {
                    Toast.makeText(getApplicationContext(),"Processed Request but failed response2", Toast.LENGTH_SHORT).show();
                } else if(response.contains("failed3")) {
                    Toast.makeText(getApplicationContext(),"Processed Request but failed response3", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Can not add Ingredient", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while POSTing ingredient name",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id", loginID);
                params.put("recipe_name", recipeName);
                params.put("ingredient_name", ingredientName);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "add_ingredient");
    }

}
