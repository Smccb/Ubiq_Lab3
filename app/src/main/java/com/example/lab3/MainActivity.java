package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db=openOrCreateDatabase("ListsDb", Context.MODE_PRIVATE, null);

        //db.execSQL("DROP TABLE IF EXISTS lists"); // Drops the table if it exists, to remove all lists already added
        db.execSQL("CREATE TABLE IF NOT EXISTS lists(ID INTEGER PRIMARY KEY, NAME VARCHAR, list VARCHAR);");

        Button newListB = findViewById(R.id.newList);

        newListB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewList(v); // Call the createNewList method when the button is clicked
            }
        });


        // Insert sample data
        //db.execSQL("INSERT INTO lists (name, list) VALUES ('shopping list', 'eggs, milk, chocolate');");

        // Query to retrieve list names
        Cursor cursor = db.rawQuery("SELECT ID, Name FROM lists", null);

        ArrayList<String> listNames = new ArrayList<>();
        listNames.clear();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("NAME"));
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ID")));
            listNames.add(id+ ", " + name);
        }

        Button newList = findViewById(R.id.newList);
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItemText = (String) parent.getItemAtPosition(position);


                String[] items = clickedItemText.split(",");

                Toast.makeText(getApplicationContext(), "Clicked: " +items[1] , Toast.LENGTH_SHORT).show();
                // Create an Intent to start the NewActivity
                Intent intent = new Intent(getApplicationContext(), NewActivity.class);
                String trimmedName = items[1].trim();

                // Pass data to the NewActivity
                intent.putExtra("ID", items[0]);
                intent.putExtra("name", trimmedName);
                startActivity(intent);
            }
        });

        reloadAllLists();

        cursor.close();
        db.close();// close db connection
    }

    private void reloadAllLists() {
        SQLiteDatabase db = openOrCreateDatabase("ListsDb", Context.MODE_PRIVATE, null);

        // Query to retrieve list names
        Cursor cursor = db.rawQuery("SELECT ID, NAME FROM lists", null);

        ArrayList<String> listNames = new ArrayList<>();
        listNames.clear();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("NAME"));
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("ID")));
            listNames.add(id + ", " + name);
        }

        cursor.close();
        db.close();

        // Update the ListView with the refreshed list of names
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listNames);
        listView.setAdapter(adapter);
    }

    public void createNewList(View view) {
        // Create an Intent to start the NewActivity
        Intent intent = new Intent(this, NewActivity.class);

        // Start the NewActivity
        startActivity(intent);
    }
}