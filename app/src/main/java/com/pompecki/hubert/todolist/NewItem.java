package com.pompecki.hubert.todolist;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class NewItem extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        RadioButton radioButton = (RadioButton) findViewById(R.id.low_priority_button);
        radioButton.setChecked(true);

        Button button = (Button) findViewById(R.id.add_item_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleField = (EditText) findViewById(R.id.title_edit_text);
                EditText descriptionField = (EditText) findViewById(R.id.description_edit_text);
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);

                // create and initialize a new task
                ToDoItem newItem = new ToDoItem();
                newItem.setTitle(titleField.getText().toString());
                newItem.setDescription(descriptionField.getText().toString());

                ToDoItem.Priority newPriority = null;
                switch(radioGroup.getCheckedRadioButtonId()) {
                    case R.id.low_priority_button:
                        newPriority = ToDoItem.Priority.LOW;
                        break;
                    case R.id.medium_priority_button:
                        newPriority = ToDoItem.Priority.MEDIUM;
                        break;
                    case R.id.high_priority_button:
                        newPriority = ToDoItem.Priority.HIGH;
                        break;
                }
                newItem.setPriority(newPriority);

                // send the task back to main activity
                Intent intent = getIntent();
                intent.putExtra(MainActivity.EXTRA_ITEM, newItem);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
