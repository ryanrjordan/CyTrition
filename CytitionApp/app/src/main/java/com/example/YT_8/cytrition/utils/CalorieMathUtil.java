package com.example.YT_8.cytrition.utils;

/** Class to store functions involved in calculating user goal values bases on user's stats.
 * @author Ryan */

public class CalorieMathUtil {

    /**
     * Currently calculates how many calories a user can have in a day, if they want to maintain their weight with very little exercise.
     * @param stats user's physical stats
     * @return recommended caloric intake for one day
     */
    static public int getDailyLimit(int[] stats) {
        int gender = stats[0];
        int height = stats[1];
        int weight = stats[2];
        int age = stats[3];
        int dailyCals;
        if(gender==1) {
            double tempCals = ((10/2.20462)*weight) + ((6.25/0.393701)*height) - (5*age) + 5;
            dailyCals = (int) tempCals;
        } else { // gender ==0
            double tempCals = ((10/2.20462)*weight) + ((6.25/0.393701)*height) - (5*age) - 161;
            dailyCals = (int) tempCals;
        }
        // *1.2 for little to no exercise
        // *1.375 for light amount of exercise
        // *1.55 for moderate amount of exercise
        return (int) (1.2 * dailyCals);
    }

    /**
     * Currently calculates how many calories a user can have in a week, if they want to maintain their weight with little exercise.
     * @param stats user's physical stats
     * @return recommended caloric intake for one week
     */
    static public int getWeeklyLimit(int[] stats) {
        return 7* getDailyLimit(stats);
    }
}
