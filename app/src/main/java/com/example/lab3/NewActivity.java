package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class NewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        EditText textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        if (intent.hasExtra("name")) {
            String itemValue = intent.getStringExtra("name");
            textView.setText(itemValue);
        }

        SQLiteDatabase db=openOrCreateDatabase("ListsDb", Context.MODE_PRIVATE, null);
        EditText editText = findViewById(R.id.editTextText);

        intent = getIntent();
        if (intent.hasExtra("ID")) {
            String id = intent.getStringExtra("ID");

            String sqlQuery = "SELECT List FROM lists WHERE ID = " + id ;

            Cursor cursor = db.rawQuery(sqlQuery, null);
            if (cursor.moveToFirst()) {
                // Extract the value from the cursor
                try{
                    String list = cursor.getString(cursor.getColumnIndex("list"));
                    editText.setText(list);
                    Toast.makeText(getApplicationContext(), "Clicked: " + list, Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "error " + id, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Error " + id, Toast.LENGTH_SHORT).show();
            }
        }
        Button saveB = findViewById(R.id.Save);

        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveList(); // Call the saveList method when the button is clicked
            }
        });

        }

    private void saveList() {
        EditText editText = findViewById(R.id.editTextText);
        String newList = editText.getText().toString();

        EditText editText2 = findViewById(R.id.textView);
        String newName = editText2.getText().toString();

        SQLiteDatabase db = openOrCreateDatabase("ListsDb", Context.MODE_PRIVATE, null);

        Intent intent = getIntent();
        if (intent.hasExtra("ID")) {
            String id = intent.getStringExtra("ID");

            String updateQuery = "UPDATE lists SET LIST = ?, Name = ? WHERE ID = ?";
            db.execSQL(updateQuery, new String[]{newList, newName, id});
        }
        //new list
        else {
            Toast.makeText(getApplicationContext(), "Else ", Toast.LENGTH_SHORT).show();
            //db.execSQL("INSERT INTO lists (name, list) VALUES (newName, newList);");
            db.execSQL("INSERT INTO lists (name, list) VALUES (?, ?);", new String[]{newName, newList});
        }

        db.close();

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}