package com.example.YT_8.cytrition;

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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.app.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/** User can input desired account profile data and submit it to be added to user_profile_table in database.
 * @author Ryan
 */
public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_id = ((EditText) findViewById(R.id.login_id_ET)).getText().toString();
                String password = ((EditText) findViewById(R.id.pass_ET)).getText().toString();
                String email = ((EditText) findViewById(R.id.emailET)).getText().toString();
                String name = ((EditText) findViewById(R.id.nameET)).getText().toString();
                String gender = ((EditText) findViewById(R.id.genderET)).getText().toString();
                String height = ((EditText) findViewById(R.id.heightET)).getText().toString();
                String weight = ((EditText) findViewById(R.id.weightET)).getText().toString();
                String user_type = ((EditText) findViewById(R.id.userTypeET)).getText().toString();
                sendSpringData(login_id,password,email,name,gender,height,weight,user_type);
            }
        });
    }

    /** Sends all inputted user data to local server spring file to be added to database.
     * @param inLogin desired login_id
     * @param inPass desired password
     * @param inEmail desired email
     * @param inName desired real name
     * @param inGender desired gender
     * @param inHeight desired height
     * @param inWeight desired weight
     * @param inUserType desired user_type(parent,admin,etc.)
     */
    private void sendSpringData(final String inLogin, final String inPass, final String inEmail, final String inName, final String inGender, final String inHeight,
                                final String inWeight, final String inUserType) {
        String url = "http://10.31.5.176:8080/cy/add/";
        url = url + inLogin + "/" + inPass + "/" + inEmail + "/" + inName + "/" + inGender + "/" + inHeight + "/" + inWeight + "/" + inUserType + "/" + Integer.toString(29);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("Saved")) {
                    Toast.makeText(getApplicationContext(), "Success! (Probably) Try loggin in.", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(loginIntent);
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
        AppController.getInstance().addToRequestQueue(stringRequest,"tag_string_signup_spring");
    }
}
