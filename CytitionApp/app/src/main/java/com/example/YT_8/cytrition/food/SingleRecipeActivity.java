package com.example.YT_8.cytrition.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravi on 2/18/2018.
 */

/**
 * Displays the full list of ingredients for a single recipe which belongs to the user
 */
public class SingleRecipeActivity extends AppCompatActivity {

    String user;
    String recipeName;
    String recipeId;
    ArrayList<String> ingredientsList;
    private ArrayAdapter<String> arrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_recipe);

        Globals g = Globals.getInstance();
        recipeName = g.getRecipeName();
        Intent i = getIntent();
        if(i.hasExtra("child")) {
            user = i.getStringExtra("child");
        } else {
            user = g.getLoginID();
        }

        TextView recipeNameText = (TextView) findViewById(R.id.single_recipe_name);
        recipeNameText.setText(recipeName);

        ingredientsList = new ArrayList<>();

        retrieveRecipeIngredients(user, recipeName);

        listView = (ListView) findViewById(R.id.single_recipe_ingredient_list);
        //noinspection Convert2Diamond
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientsList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), IngredientActivity.class);
                Globals g = Globals.getInstance();
                g.setIngredientName(ingredientsList.get(i));
                startActivity(intent);
                //Toast.makeText(SingleRecipeActivity.this, ingredientsList.get(i), Toast.LENGTH_SHORT).show();
            }
        });

        Button addbtn = (Button) findViewById(R.id.single_recipe_btn_add_ing);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddIngredientActivity.class);
                intent.putExtra("child", user);
                startActivity(intent);
            }
        });

        Button deletebtn = (Button) findViewById(R.id.single_recipe_btn_delete);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecipe(user, recipeName);
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
     * Gets the complete list of ingredients from the database that associate with the a specific recipe
     * @param loginID the user name for the current user
     * @param recipeName the recipe name for which to retrieve the list of ingredients
     */
    private void retrieveRecipeIngredients(final String loginID, final String recipeName) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_recipe_ingredients.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SingleRecipeActivity", response);
                if(response.contains(",")) {
                    String[] parsedData = parseIngredientData(response.toString());
                    setIngredients(parsedData);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ingredients does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SingleRecipeActivity", error.toString());
                Toast.makeText(getApplicationContext(),"Error while POSTing ingredients name",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("recipe_name",recipeName);
                params.put("login_id", loginID);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "list_recipe_ingredients");
    }

    private void deleteRecipe(final String loginID, final String recipeName) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/delete_recipe.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SingleRecipeActivity", response);
                if(response.contains("yes")) {
                    Toast.makeText(getApplicationContext(), "Deleted Recipe", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "delete_recipe");
    }

    private String[] parseIngredientData(String data){
        String[] result;
        result = data.split(",");
        return result;
    }

    private void setIngredients(String[] data) {
        for (int i = 0; i < data.length; i ++) {
            arrayAdapter.add(data[i]);
        }
    }

}
