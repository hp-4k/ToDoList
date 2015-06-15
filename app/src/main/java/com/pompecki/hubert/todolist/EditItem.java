package com.pompecki.hubert.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


public class EditItem extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        final ToDoItem item = (ToDoItem) getIntent().getSerializableExtra(MainActivity.EXTRA_EDIT_ITEM);

        Button button = (Button) findViewById(R.id.edit_item_button);

        final EditText titleField = (EditText) findViewById(R.id.title_edit_text);
        titleField.setText(item.getTitle());

        final EditText descriptionField = (EditText) findViewById(R.id.description_edit_text);
        descriptionField.setText(item.getDescription());

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        switch(item.getPriority()) {
            case LOW:
                radioGroup.check(R.id.low_priority_button);
                break;
            case MEDIUM:
                radioGroup.check(R.id.medium_priority_button);
                break;
            case HIGH:
                radioGroup.check(R.id.high_priority_button);
                break;
            default:
                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setTitle(titleField.getText().toString());
                item.setDescription(descriptionField.getText().toString());

                switch(radioGroup.getCheckedRadioButtonId()) {
                    case R.id.low_priority_button:
                        item.setPriority(ToDoItem.Priority.LOW);
                        break;
                    case R.id.medium_priority_button:
                        item.setPriority(ToDoItem.Priority.MEDIUM);
                        break;
                    case R.id.high_priority_button:
                        item.setPriority(ToDoItem.Priority.HIGH);
                        break;
                }

                // send the task back to main activity
                Intent intent = getIntent();
                intent.putExtra(MainActivity.EXTRA_ITEM, item);
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
