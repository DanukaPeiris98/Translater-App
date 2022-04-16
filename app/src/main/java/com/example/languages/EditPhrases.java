package com.example.languages;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class EditPhrases extends AppCompatActivity {
    Button btnSaveEd, btnEditSa;
    EditText editTextPhr;
    Database myDB;
    int selected;
    final ArrayList<String> theList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);

        final ListView listView = findViewById(R.id.listView2);
        btnSaveEd = findViewById(R.id.btnSave2);
        btnEditSa = findViewById(R.id.btnEdit2);
        editTextPhr = findViewById(R.id.editText3);

        myDB = new Database(this);
        //Edit Button
        btnEditSa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (selected == -1) {
                    Toast.makeText(EditPhrases.this, "You must select one field", Toast.LENGTH_LONG).show();
                } else {
                    //set selected word to edit Text
                    editTextPhr.setText(theList.get(selected));
                }
            }
        });
        //Save Button
        btnSaveEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get selected word
                String oldItem = theList.get(selected);
                //get new word
                String updateItem = editTextPhr.getText().toString();
                if (editTextPhr.length() != 0) {
                    updateData(oldItem, updateItem);
                    editTextPhr.setText("");
                    Cursor data = myDB.getWordList();
                    //clear previous list
                    theList.clear();
                    while (data.moveToNext()) {

                        theList.add(data.getString(1));
                        ListAdapter listAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, theList);
                        listView.setAdapter(listAdapter);
                        //add Radio Buttons
                        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        listAdapter = new ArrayAdapter(EditPhrases.this, android.R.layout.simple_list_item_single_choice, theList);
                        listView.setAdapter(listAdapter);
                        //alphabetical Order
                        Collections.sort(theList, new Comparator<String>() {
                            @Override
                            public int compare(String s1, String s2) {
                                return s1.compareToIgnoreCase(s2);
                            }
                        });

                    }

                } else {
                    Toast.makeText(EditPhrases.this, "You must put something in the text field!", Toast.LENGTH_LONG).show();
                }
            }
        });


        //get word list to data
        Cursor data = myDB.getWordList();

        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {

            while (data.moveToNext()) {

                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                listView.setAdapter(listAdapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listAdapter = new ArrayAdapter(EditPhrases.this, android.R.layout.simple_list_item_single_choice, theList);
                listView.setAdapter(listAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Toast.makeText(EditPhrases.this, "Selected " + theList.get(i), Toast.LENGTH_SHORT).show();
                        //get selected word
                        selected = i;
                    }
                });

                //set alphabetical Order
                Collections.sort(theList, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareToIgnoreCase(s2);
                    }
                });

            }
        }


    }

    public void updateData(String oldItem, String updateItem) {


        boolean isUpdate = myDB.updateData(oldItem, updateItem);
        if (isUpdate == true)
            Toast.makeText(EditPhrases.this, "Data Update", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(EditPhrases.this, "Data not Updated", Toast.LENGTH_LONG).show();
    }
}




