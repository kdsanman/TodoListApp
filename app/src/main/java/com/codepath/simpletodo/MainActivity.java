package com.codepath.simpletodo;

import android.content.Intent;
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

    //a numeric code that allows us to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;

    //declare keys to track
    //keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";

    //position of item in list
    public final static String ITEM_POSITION = "itemPosition";

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

        // set up item listener for edit (regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //only gets executed when an item gets clicked by the user
                //needs to create the new activity
                // the .this activity refers to our MainActivity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                //need to pass the data being edited and the position in the list
                //extras = extra values that can be included in the intent
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);

                //display the activity
                startActivityForResult(i, EDIT_REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // check that the activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            // extract updated items text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);

            //extract the original position
            int position = data.getExtras().getInt(ITEM_POSITION);

            //update the model with the new item value at the correct position
            items.set(position, updatedItem);

            //notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();

            //persis the changed model
            writeItems();

            //notify the user the operation completed OK
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
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
