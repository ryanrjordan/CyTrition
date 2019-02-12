package com.example.YT_8.cytrition.calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.app.AppController;
import com.example.YT_8.cytrition.app.Globals;
import com.example.YT_8.cytrition.utils.CalorieMathUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** Calendar display page. Links to activities to add meals and view past days' meals.
 * @author Ryan
 */
public class CalendarActivity extends AppCompatActivity {
    /* user may hold name of child. It always holds name of user whose calendar data is being modified,
        whether the parent is doing it or the actual user is */
    private String login_id = "";
    private int month;
    private int day;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Globals g = Globals.getInstance();
        Intent i = getIntent();
        if(i.hasExtra("child")) {
            login_id = i.getStringExtra("child");
        } else {
            login_id = g.getLoginID();
        }
        setTitle(login_id + "'s Calendar");

        CalendarView cv = (CalendarView) findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                month = i1+1;
                day = i2;
                year = i;
            }
        });
        getCurrentDate();
        getMealCount(login_id,month+"/"+day+"/"+year);
        loopPastWeek(login_id);

        /* Button to view meals from selected day on calendarview. */
        Button viewDayDataButton = (Button) findViewById(R.id.viewDayDataButton);
        viewDayDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curDate = month + "/" + day + "/" + year;
                Intent viewMealsIntent = new Intent(getApplicationContext(),ViewMealsActivity.class);
                viewMealsIntent.putExtra("userLogin",login_id);
                viewMealsIntent.putExtra("date",curDate);
                startActivity(viewMealsIntent);
            }
        });

        /* Button to go to add meal page for breakfast on selected day. */
        Button addBreakfastButton = (Button) findViewById(R.id.addBreakfastButton);
        addBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBreakfastIntent = new Intent(getApplicationContext(),AddCalendarMealActivity.class);
                addBreakfastIntent.putExtra("user", login_id);
                String date = month + "/" + day + "/" + year;
                addBreakfastIntent.putExtra("date", date);
                addBreakfastIntent.putExtra("mealType", "0"); //0 = breakfast
                startActivity(addBreakfastIntent);
            }
        });

        /* Button to go to add meal page for lunch on selected day. */
        Button addLunchButton = (Button) findViewById(R.id.addLunchButton);
        addLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBreakfastIntent = new Intent(getApplicationContext(),AddCalendarMealActivity.class);
                addBreakfastIntent.putExtra("user", login_id);
                String date = month + "/" + day + "/" + year;
                addBreakfastIntent.putExtra("date", date);
                addBreakfastIntent.putExtra("mealType", "1"); //1 = lunch
                startActivity(addBreakfastIntent);
            }
        });

        /* Button to go to add meal page for snack on selected day. */
        Button addSnackButton = (Button) findViewById(R.id.addSnackButton);
        addSnackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBreakfastIntent = new Intent(getApplicationContext(),AddCalendarMealActivity.class);
                addBreakfastIntent.putExtra("user", login_id);
                String date = month + "/" + day + "/" + year;
                addBreakfastIntent.putExtra("date", date);
                addBreakfastIntent.putExtra("mealType", "2"); //2 = snack
                startActivity(addBreakfastIntent);
            }
        });

        /* Button to go to add meal page for dinner on selected day. */
        Button addDinnerButton = (Button) findViewById(R.id.addDinnerButton);
        addDinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addBreakfastIntent = new Intent(getApplicationContext(),AddCalendarMealActivity.class);
                addBreakfastIntent.putExtra("user", login_id);
                String date = month + "/" + day + "/" + year;
                addBreakfastIntent.putExtra("date", date);
                addBreakfastIntent.putExtra("mealType", "3"); //3 = dinner
                startActivity(addBreakfastIntent);
            }
        });
    }

    /**
     * Sets instance variables (day/month/year) to current real date using currentTimeMillis() and converting. */
    private void getCurrentDate() {
        long epoch = System.currentTimeMillis();
        Date date = new Date(epoch);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String stringDate = sdf.format(date);
        String[] parsed = stringDate.split("/");
        month = Integer.parseInt(parsed[0]);
        day = Integer.parseInt(parsed[1]);
        year = Integer.parseInt(parsed[2]);
    }

    /** Retrieves user data using volley request to get number of meals logged for given day.
     * @param name representing login_id in database
     * @param date today's date
     * @see #updateSimpProgress(int)  */
    private void getMealCount(final String name, final String date) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_meal_count_for_date.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CalendarActivity", response);
                updateSimpProgress(Integer.parseInt(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Connection error.",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",name);
                params.put("date", date);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_retrieve_prog_data");
    }

    /** Takes string response from getMealCount' volley request and returns user's physical stats.
     * @param data sting response
     * @return personal physical stats in int array
     */
    private int[] getRelevantData(String data){
        String[] result = data.split(",");
        @SuppressWarnings("UnnecessaryLocalVariable") int[] relevant = {Integer.parseInt(result[7]),Integer.parseInt(result[9]),Integer.parseInt(result[11]),Integer.parseInt(result[15])};
        return relevant;        //Note: last item in int[] is age
    }

    /** Takes int array input containing physical stats and sets user's goals for calorie intake.
     * @param stats physical stats (height,weight,gender,age). */
    private void updateGoals(int[] stats) {
        if(!(stats[0]==0||stats[0]==1)) return;
        ProgressBar dailyProgBar = (ProgressBar) findViewById(R.id.dailyProgBar);
        ProgressBar weeklyProgBar = (ProgressBar) findViewById(R.id.weeklyProgBar);
        dailyProgBar.setMax(CalorieMathUtil.getDailyLimit(stats));
        weeklyProgBar.setMax(CalorieMathUtil.getWeeklyLimit(stats));
        dailyProgBar.setProgress(dailyProgBar.getMax()/3); //arbitrary values for progress
        weeklyProgBar.setProgress(3*(weeklyProgBar.getMax()/4));
    }

    /**
     * Takes int version of response from volley request tha represents the number of meals consumed today and sets daily progress bar to
     * num/4 and weekly bar to num/28.
     * @param numLogged number of meal events logged for given day
     * @see #getMealCount(String, String)
     */
    private void updateSimpProgress(int numLogged) {
        ProgressBar dailyProgBar = (ProgressBar) findViewById(R.id.dailyProgBar);
        ProgressBar weeklyProgBar = (ProgressBar) findViewById(R.id.weeklyProgBar);
        dailyProgBar.setMax(4);
        weeklyProgBar.setMax(28);
        dailyProgBar.setProgress(numLogged);
        weeklyProgBar.setProgress(numLogged);
    }

    /**
     * Loops through last six days to update weekly progress bar to show more than just current day's intake.
     * @see #getMealCount(String, String)
     * @param name login_id of calendar's owner
     */
    private void loopPastWeek(final String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        for(int i = -1; i>-7; i--) {
            int multiplier = i*24;
            Date d = new Date(System.currentTimeMillis()+multiplier*60*60*1000);
            String pastDate = sdf.format(d);
            if(pastDate.charAt(0) == '0') {
                pastDate = pastDate.substring(1);
            }
//            Toast.makeText(getApplicationContext(),pastDate,Toast.LENGTH_SHORT).show();
            getMealCountAlt(name,pastDate,i*-1);
        }
    }

    /**
     * Calls PHP file that returns number of rows in table matching input and sends that value to updateWeekProg().
     * @see #updateWeekProg(int)
     * @param name login_id of calendar's owner
     * @param date date of some day from past week
     * @param count value to add to end of queue tag
     */
    private void getMealCountAlt(final String name, final String date, final int count) {
        String url = "http://proj-309-yt-8.cs.iastate.edu/retrieve_meal_count_for_date.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CalendarActivityAlt"+count, response);
                updateWeekProg(Integer.parseInt(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Connection error.",Toast.LENGTH_SHORT).show();
            }
        }) {
            /*This is string data POSTed to server file with key-string format*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                @SuppressWarnings("Convert2Diamond") Map<String,String> params = new HashMap<String, String>();
                params.put("login_id",name);
                params.put("date", date);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "string_req_retrieve_prog_data" + count);
    }

    /**
     * Updates weekly progress bar to add meal count for one day.
     * @param num number of meals logged for a day in the past week
     */
    private void updateWeekProg(int num) {
        ProgressBar weeklyProgBar = (ProgressBar) findViewById(R.id.weeklyProgBar);
        int current = weeklyProgBar.getProgress();
        weeklyProgBar.setProgress(num + current);
//        Toast.makeText(getApplicationContext(),"Adding " + num + " to " + current,Toast.LENGTH_SHORT).show();
    }
}
