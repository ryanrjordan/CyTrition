package com.example.YT_8.cytrition.tools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.YT_8.cytrition.R;
import com.example.YT_8.cytrition.bugs.ResolveBugActivity;

import java.util.ArrayList;

/**
 * This activity allows the user to create and modify an list of grocery items. They can add new items and delete list items by clicking on them.
 * @author Ryan
 */
public class GroceryListActivity extends AppCompatActivity {
    private ArrayList<String> groceryList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        /* Initialization */
        final EditText newItemET = (EditText) findViewById(R.id.newItemET);
        groceryList = new ArrayList<>();
        ListView groceryListView = (ListView) findViewById(R.id.listView);
        //noinspection Convert2Diamond
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,groceryList);
        groceryListView.setAdapter(arrayAdapter);

        /* When a item in the list is clicked on, it is removed. */
        groceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // strikethrough or delete from list
                groceryList.remove(i);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        /* Tries to add string item from EditText to list. (Duplicates are not allowed. */
        Button addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = newItemET.getText().toString();
                if(!groceryList.contains(item)) {
                    groceryList.add(item);
                    newItemET.setText("");
                    Toast.makeText(getApplicationContext(),item + " added to grocery list.",Toast.LENGTH_SHORT).show();
                    arrayAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(),"Item already in list.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
