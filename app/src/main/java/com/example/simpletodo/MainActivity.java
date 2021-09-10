package com.example.simpletodo;

import android.content.Context;
import org.apache.commons.io.FileUtils;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static  final String KEY_ITEM_POSITION = "item_position";
    public static final  int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdaptor itemsAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvitems);

        loadItems();

        ItemsAdaptor.OnLongClickListener onLongClickListener = new ItemsAdaptor.OnLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter
                itemsAdaptor.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Text was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdaptor.OnClickListener onClickListener = new ItemsAdaptor.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position");
                //create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemsAdaptor =  new ItemsAdaptor(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdaptor);
        rvItems.setLayoutManager(new LinearLayoutManager( this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoitem = etItem.getText().toString();
                //Add items to the model
                items.add(todoitem);
                //Notify adapter that an item is inserted
                itemsAdaptor.notifyItemInserted(items.size() -1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Text was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    //handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //Retreive the update test value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //Extract position of the orignal position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            //update the model at the right position with new item text
            items.set(position, itemText);
            //notify the adapter
            itemsAdaptor.notifyItemChanged(position);
            //persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity","Unknown call to onActivityResult");
                }
            }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // This function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}