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
import com.example.YT_8.cytrition.HomePageActivity;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.adapter.RecipeAdapter;
import com.example.YT_8.cytrition.app.AppController;
import com.example.YT_8.cytrition.app.Globals;
import com.example.YT_8.cytrition.model.Recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
/**
 * Created by Ravi on 2/16/2018.
 */

/**
 * Displays the a list of the users recipes
 */
public class RecipeActivity extends AppCompatActivity {

    private TextView userRecipes;
    private ListView listView;
    private ArrayList<String> names;
    private String user = "";
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Creates back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Sets user to to login_id
        Globals g = Globals.getInstance();
        Intent i = getIntent();
        if(i.hasExtra("child")) {
            user = i.getStringExtra("child");
        } else {
            user = g.getLoginID();
        }

        //Shows "[Users]'s recipes
        userRecipes = (TextView) findViewById(R.id.recipe_user_name);
        userRecipes.setText(user + "'s Recipes");

        //declaration of ArrayList<>()
        //noinspection Convert2Diamond
        names = new ArrayList<String>();

        //sets an ArrayAdapter to ListView to convert the data into the proper format
        listView = (ListView) findViewById(R.id.recipe_list);
        //noinspection Convert2Diamond
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(arrayAdapter);

        //Fills up ListView with user recipes
        retrieveUserRecipes(user);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SingleRecipeActivity.class);
                intent.putExtra("child", user);
                Globals g = Globals.getInstance();
                g.setRecipeName(names.get(i));
                startActivity(intent);
            }
        });

        Button addbtn = (Button) findViewById(R.id.recipe_add);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddRecipeActivity.class);
                intent.putExtra("child", user);
                startActivity(intent);
            }
        });

        Button updatebtn = (Button) findViewById(R.id.recipe_update_list);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayAdapter.clear();
                retrieveUserRecipes(user);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),HomePageActivity.class);
        startActivity(intent);
    }

    /**
     * Gets the complete list of recipes associated with the user
     * @param login_id the current users id number
     */
    private void retrieveUserRecipes(final String login_id) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_user_recipes.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RecipeActivity", response);
                if(response.contains("recipe_name")) {
                    String[] parsedData = parseRecipeData(response.toString());
                    setNames(parsedData);
                } else {
                    Toast.makeText(getApplicationContext(),"You currently have no recieps", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("recipeActivity",error.toString());
                Toast.makeText(getApplicationContext(),"Error while POSTing recipe name",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id", login_id);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "list_recipe_ingredients");
    }

    private String[] parseRecipeData(String data){
        String[] result;
        result = data.split(",");
        return result;
    }

    private void setNames(String[] data) {
        for (int i = 1; i < data.length; i = i + 2) {
            arrayAdapter.add(data[i]);
        }
    }
}
