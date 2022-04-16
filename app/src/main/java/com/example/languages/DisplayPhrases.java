package com.example.languages;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DisplayPhrases extends AppCompatActivity {

    Database myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);

        final ListView listView = findViewById(R.id.listView);
        myDB = new Database(this);

        final ArrayList<String> theList = new ArrayList<>();
        //get word list to words
        Cursor words = myDB.getWordList();

        if (words.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (words.moveToNext()) {

                theList.add(words.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                //set words to listView
                listView.setAdapter(listAdapter);

                //Set alphabetical Order
                Collections.sort(theList, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareToIgnoreCase(s2);
                    }
                });

            }
        }


    }
}
