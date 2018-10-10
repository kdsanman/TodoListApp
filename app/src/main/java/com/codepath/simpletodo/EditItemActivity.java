package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.codepath.simpletodo.MainActivity.ITEM_POSITION;
import static com.codepath.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    // track edit text being used
    EditText etItemText;
    // track the position of the item in the list
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // need to resolve the edit text from the layout
        etItemText = findViewById(R.id.etItemText);

        // set the value of the editText to the string description of the item being edited
        // get the value from the intent
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));// updates the value of the editText

        //update the position from the intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);

        // update the title bar of the action
        getSupportActionBar().setTitle("Editing Item");
    }

    // handler for save button
    public void onSaveItem(View v) {
        // prepare new intent for result
        Intent i = new Intent();

        //pass updated item text as extra
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());

        //pass original position as extra
        i.putExtra(ITEM_POSITION, position);

        // set the intent as the result of the activity
        setResult(RESULT_OK, i);

        //close the activity and redirect to main
        finish();
    }
}
