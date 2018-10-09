package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Declaring variables
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //invoked in order when the app is created/built
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems(); //replaces line below
        //items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems = findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        //mock data
        //items.add("First item");
        //items.add("Second item");

        setupListViewListener();

    }

    public void onAddItem(View v) {
        //need to resolve the edit text. this is a reference to etNewItem
        EditText etNewItem = findViewById(R.id.etNewItem);

        //get value of edit text as a string
        String itemText = etNewItem.getText().toString();

        //add it to our list
        itemsAdapter.add(itemText);

        //clear field to add another item
        etNewItem.setText("");

        //when something is added
        writeItems();

        //Alert user that item has been added to list
        Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();

    }

    private void setupListViewListener() {
        //not executed until there is a long click
        Log.i("MainActivity", "Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list: " + position);

                //remove the item
                items.remove(position);

                //Alert user that item has been added to list
                Toast.makeText(getApplicationContext(), "Item removed from: " + position, Toast.LENGTH_SHORT).show();

                //need the adapter to know the item was deleted
                itemsAdapter.notifyDataSetChanged();

                //when something is removed
                writeItems();

                //want to consume it, so return true
                return true;
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    //read from the file
    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            //initialize to an empty array, to avoid null pointer
            items = new ArrayList<>();
        }

    }

    //write everytime the model is changed
    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }
}
