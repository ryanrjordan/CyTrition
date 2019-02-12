package com.example.YT_8.cytrition;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.app.AppController;
import com.example.YT_8.cytrition.app.Globals;
import com.example.YT_8.cytrition.test.SpringTestActivity;
import com.example.YT_8.cytrition.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * User login page. Can login existing users and sign-up new ones.
 * @author Ryan
 * @author Ravi
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Button used to trigger login attempt. */
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameEditText = (EditText) findViewById(R.id.usernameTextView);
                EditText passwordEditText = (EditText) findViewById(R.id.passwordTextView);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(!username.isEmpty() && !password.isEmpty()) {
                    verifyLoginData(username,password);
//                    loginWithSpring(username,password);
                }
            }
        });

        /* Button used to go to sign-up page. */
        Button signupButton = (Button) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameEditText = (EditText) findViewById(R.id.usernameTextView);
                EditText passwordEditText = (EditText) findViewById(R.id.passwordTextView);
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                userSignUp(username,password);

//                Intent signUpIntent = new Intent(getApplicationContext(),SignUpActivity.class);
//                startActivity(signUpIntent);
            }
        });

        /* Button used to go to Spring test page for easier testing. */
        Button springTestButton = (Button) findViewById(R.id.springTestButton);
        springTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testIntent = new Intent(getApplicationContext(), SpringTestActivity.class);
                startActivity(testIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    /** Volley request sending inputted login_id and password to server file that verifies data with database.
     * @param inputUsername corresponds to login_id
     * @param inputPassword corresponds to password
     */
    private void verifyLoginData(final String inputUsername, final String inputPassword) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/login_php.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LoginActivity", response);
                if(response.contains("success")) {
                    Globals.getInstance().setLoginID(inputUsername);
                    setUserID(inputUsername);
                    Intent login = new Intent(getApplicationContext(),HomePageActivity.class);
                    startActivity(login);
                } else {
                    Toast.makeText(getApplicationContext(),"Wrong loginID or password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Connection error while POSTing login data",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",inputUsername);
                params.put("password",inputPassword);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_login");
    }

    /* Volley request sending inputted login_id and password to server file that inserts data into user_profile_table if there are no conflicts.
     * Only used when not connect to local server that Spring is on.
     *  May be replaced by new activity and Spring function.
     * @param inputUsername corresponds to login_id
     * @param inputPassword corresponds to password
     */
    private void userSignUp(final String inputUsername, final String inputPassword) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/signup_php.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MainActivity2", response);
                if(response.contains("success")) {
                    Toast.makeText(getApplicationContext(),"Account created successully! Now click login.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"LoginID already in use.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Connection error while POSTing new login data",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",inputUsername);
                params.put("password",inputPassword);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_signup");
    }

    /**
     * Called after user successfully verifies login information. Sets singleton to contain correct user_id.
     * @param login_id login_id of user logging in
     */
    private void setUserID(final String login_id) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_user_id.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LoginActivity_SetID", response);
                if(!response.contains("C")) {
                    Globals.getInstance().setUserID(Integer.parseInt(response));
                } else {
                    Toast.makeText(getApplicationContext(),"Error finding user in database", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error while loading user ID",Toast.LENGTH_SHORT).show();
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
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_set_id");
    }

    private void loginWithSpring(final String inputUsername, final String inputPassword) {
        String url = "http://10.31.5.176:8080/cy/login/"+inputUsername+"/"+inputPassword;

        StringRequest stringRequest = new StringRequest(Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LoginActivity/loginWithSpring", response);
                if(response.contains("success")) {
                    Globals.getInstance().setLoginID(inputUsername);
                    setUserID(inputUsername);
                    Intent login = new Intent(getApplicationContext(),HomePageActivity.class);
                    startActivity(login);
                } else {
                    Toast.makeText(getApplicationContext(),"Wrong loginID or password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LoginActivity/loginWithSpring",error.toString());
                Toast.makeText(getApplicationContext(),"Connection error while POSTing login data",Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_login_spring");
    }
}