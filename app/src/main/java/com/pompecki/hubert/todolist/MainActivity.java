package com.pompecki.hubert.todolist;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private final static String FILENAME = "items.dat";
    public final static String EXTRA_EDIT_POSITION = "extraEditPosition";
    public final static String EXTRA_EDIT_ITEM = "extraEditItem";
    public final static String EXTRA_ITEM = "extraItem";
    private final static int NEW_TASK_REQUEST = 1;
    private final static int EDIT_TASK_REQUEST = 2;

    ListView itemsList;
    List<ToDoItem> toDoItems;
    ToDoItemAdapter adapter = new ToDoItemAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadItems();
        itemsList = (ListView) findViewById(R.id.items_list);
        itemsList.setAdapter(adapter);
        registerForContextMenu(itemsList);

        Button newItemButton = (Button) findViewById(R.id.new_item_button);
        newItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewItem.class);
                startActivityForResult(intent, NEW_TASK_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == NEW_TASK_REQUEST) {
            toDoItems.add((ToDoItem) data.getSerializableExtra(EXTRA_ITEM));
            adapter.notifyDataSetChanged();
        } else if (resultCode == RESULT_OK && requestCode == EDIT_TASK_REQUEST) {
            toDoItems.set(data.getIntExtra(EXTRA_EDIT_POSITION, -1),
                    (ToDoItem) data.getSerializableExtra(EXTRA_EDIT_ITEM));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onPause() {
        super.onPause();
        ObjectOutputStream os = null;
        File file = new File(getFilesDir(), FILENAME);
        try {
            os = new ObjectOutputStream(new FileOutputStream(file));
            for (ToDoItem item : toDoItems) {
                os.writeObject(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) { ex.printStackTrace(); }
            }
        }
    }

    public void loadItems() {
        toDoItems = new ArrayList<>();
        ObjectInputStream is = null;
        File file = new File(getFilesDir(), FILENAME);
        try {
            is = new ObjectInputStream(new FileInputStream(file));
            Object tmp;
            while ((tmp = is.readObject()) != null) {
                toDoItems.add((ToDoItem) tmp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) { ex.printStackTrace(); }
            }
        }
        //newItemButton.setText(newItemButton.getText().toString() + toDoItems.size());
    }

    class ToDoItemAdapter extends BaseAdapter {

        public int getCount() {
            return toDoItems.size();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout item = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.to_do_item,
                    parent, false);

            final int itemNo = position;
            ToDoItem thisItem = (ToDoItem) getItem(position);
            TextView itemTitle = (TextView) item.findViewById(R.id.item_title);
            TextView itemDescription = (TextView) item.findViewById(R.id.item_description);
            TextView itemPriority = (TextView) item.findViewById(R.id.item_priority);

            itemTitle.setText(thisItem.getTitle());
            itemDescription.setText(thisItem.getDescription());
            itemPriority.setText("Priority: " + thisItem.getPriority());

            return item;
        }

        public long getItemId(int position) {
            return 0;
        }

        public Object getItem(int position) {
            return toDoItems.get(position);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        switch (item.getItemId()) {
            case R.id.context_menu_edit:
                Intent intent = new Intent(this, EditItem.class);
                intent.putExtra(EXTRA_EDIT_ITEM, toDoItems.get(position))
                        .putExtra(EXTRA_EDIT_POSITION, position);
                startActivityForResult(intent, EDIT_TASK_REQUEST);
                return true;
            case R.id.context_menu_delete:
                toDoItems.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
