package com.example.YT_8.cytrition.personal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

/**
 * Page for users to view and edit profile data.
 * @author Ryan
 */
public class EditProfileActivity extends AppCompatActivity {
    private String user = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent i = getIntent();
        user = i.getStringExtra("loginID");

        /*Places user's loginID at the top of the page. */
        TextView loginidDisplay = (TextView) findViewById(R.id.loginidTV);
        loginidDisplay.setText(user);

        /*Sets up dropdown menu for user types. */
        final Spinner userTypeSpinnner = (Spinner) findViewById(R.id.userTypeSpinner);
        @SuppressWarnings("Convert2Diamond") ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.user_types));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinnner.setAdapter(typeAdapter);

        retrieveUserData();

        /*This button push sends set data values from EditTexts to be updated in database. */
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialization of all EditTexts
                EditText passwordET = (EditText) findViewById(R.id.passwordET);
                EditText emailET = (EditText) findViewById(R.id.emailET);
                EditText nameET = (EditText) findViewById(R.id.nameET);
                EditText genderET = (EditText) findViewById(R.id.genderET);
                EditText heightET = (EditText) findViewById(R.id.heightET);
                EditText weightET = (EditText) findViewById(R.id.weightET);
                EditText ageET = (EditText) findViewById(R.id.ageET);

                updateDatabasePHP(user,passwordET.getText().toString(),emailET.getText().toString(),nameET.getText().toString(),
                        genderET.getText().toString(),heightET.getText().toString(),weightET.getText().toString(),Integer.toString(userTypeSpinnner.getSelectedItemPosition()),
                        ageET.getText().toString());
//                updateDatabaseSpring(user,passwordET.getText().toString(),emailET.getText().toString(),nameET.getText().toString(),
//                        genderET.getText().toString(),heightET.getText().toString(),weightET.getText().toString(),Integer.toString(userTypeSpinnner.getSelectedItemPosition()));

                Intent homepage = new Intent(getApplicationContext(),HomePageActivity.class);
                homepage.putExtra("updated",true);
                startActivity(homepage);
            }
        });
    }

    /** Volley request to server-side php file sending login_id and getting response in form of all user data from user_profile_table.
     * @see #parseUserData(String)
     * @see #displayUserData(String[]) */
    private void retrieveUserData() {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_user_data.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("EditProfileActivity", response);
                if(response.contains("password")) {
                    String[] parsedData = parseUserData(response);
                    displayUserData(parsedData);
                } else {
                    Toast.makeText(getApplicationContext(),"Error finding loginID in database", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while loading user data",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",user);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_retrieve");
    }

    /** Used to parse single string output from php file with commas separating each value. */
    private String[] parseUserData(String data){
        String[] result;
        result = data.split(",");
        return result;
    }

    /** Updates values in EditTexts to show values in columns from database.
     * @param data stores array of user data to be displayed in EditTexts. */
    private void displayUserData(String[] data) {
        if(data.length<10) return;
        //initialize all EditTexts
        EditText passwordET = (EditText) findViewById(R.id.passwordET);
        EditText emailET = (EditText) findViewById(R.id.emailET);
        EditText nameET = (EditText) findViewById(R.id.nameET);
        EditText genderET = (EditText) findViewById(R.id.genderET);
        EditText heightET = (EditText) findViewById(R.id.heightET);
        EditText weightET = (EditText) findViewById(R.id.weightET);
        EditText ageET = (EditText) findViewById(R.id.ageET);
        //set text to be value in database
        passwordET.setText(data[1]);
        emailET.setText(data[3]);
        nameET.setText(data[5]);
        genderET.setText(data[7]);
        heightET.setText(data[9]);
        weightET.setText(data[11]);
        ageET.setText(data[15]);
        Spinner userTypeSpinner = (Spinner) findViewById(R.id.userTypeSpinner);
        userTypeSpinner.setSelection(Integer.parseInt(data[13]));
    }

    /** Volley request to server-side php file that updates data in user_profile_table for logged in user.
     *  Has no output. Just displays a toast and sends user back to homepage upon successful update.
     * @param login_id user's login_id
     * @param password user's password
     * @param email user's email address
     * @param name user's name (full or first)
     * @param gender user's gender (must be 0 or 1 for calculations)
     * @param height user's height
     * @param weight user's weight
     * @param type user's user_type (parent, admin, etc.)
     * @param age user's age
     */
    private void updateDatabasePHP(final String login_id, final String password, final String email, final String name, final String gender, final String height, final String weight, final String type, final String age) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/update_user_data.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("EditProfileActivity", response);
                if(response.contains("success")) {
                    Toast.makeText(getApplicationContext(),"User data updated for user: " + user, Toast.LENGTH_SHORT).show();
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
                params.put("login_id",login_id);
                params.put("password",password);
                params.put("email",email);
                params.put("name",name);
                params.put("gender",gender);
                params.put("height",height);
                params.put("weight",weight);
                params.put("user_type",type);
                params.put("age",age);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_update");
    }

    /**
     * Volley request to server-side Spring file that updates data in user_profile_table for logged in user.
     * Has no output. Just displays a toast and sends user back to homepage upon successful update.
     * @param inLogin user's login_id
     * @param inPass user's password
     * @param inEmail user's email address
     * @param inName user's actual name (full or first)
     * @param inGender user's gender (must be 0 or 1 for calculations
     * @param inHeight user's height
     * @param inWeight user's weight
     * @param inUserType user's user_type (parent, admin, etc.)
     */
    private void updateDatabaseSpring(final String inLogin, final String inPass, final String inEmail, final String inName, final String inGender, final String inHeight,
                                final String inWeight, final String inUserType) {
        String url = "http://10.31.30.177:8080/cy/update/" + user;
        url = url + "/" + inLogin + "/" + inPass + "/" + inEmail + "/" + inName + "/" + inGender + "/" + inHeight + "/" + inWeight + "/" + inUserType;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("Updated")) {
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Abnormal response.",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return null;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest,"string_req_update2");
    }
}