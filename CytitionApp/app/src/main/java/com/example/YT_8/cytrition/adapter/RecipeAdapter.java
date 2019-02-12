package com.example.YT_8.cytrition.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.model.Recipe;

import java.util.ArrayList;

/**
 * Created by Ravi on 3/8/2018.
 */

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    public RecipeAdapter(Context context, ArrayList<Recipe> recipe) {
        super(context, 0, recipe);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }

        TextView rowName = (TextView) convertView.findViewById(R.id.rowName);
        rowName.setText(recipe.recipeName);

        Button rowDelete = (Button) convertView.findViewById(R.id.rowDelete);
        rowDelete.setTag(position);
        rowDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
            }
        });

        return convertView;
    }
}
