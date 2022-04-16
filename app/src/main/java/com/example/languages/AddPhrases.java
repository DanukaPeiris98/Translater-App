package com.example.languages;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class AddPhrases extends AppCompatActivity {

    Database myDB;
    Button btnAddPh;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);

        editText = findViewById(R.id.editText);
        btnAddPh = findViewById(R.id.btnSave);
        myDB = new Database(this);

        //Add Button
        btnAddPh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get User input
                String newEntry = editText.getText().toString();
                final ArrayList<String> theList = new ArrayList<>();
                Cursor data = myDB.getWordList();
                while (data.moveToNext()) {
                    //add all words in database to the list
                    theList.add(data.getString(1));
                }
                //Check user input word already in database or not
                if (!(theList.contains(newEntry))) {
                    if (editText.length() != 0) {
                        AddData(newEntry);
                        editText.setText("");
                    } else {
                        Toast.makeText(AddPhrases.this, "You must put something in the text field", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AddPhrases.this, "This word already in database", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void AddData(String newEntry) {

        boolean inputData = myDB.addData(newEntry);

        if (inputData == true) {
            Toast.makeText(this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong :-", Toast.LENGTH_LONG).show();
        }
    }
}
