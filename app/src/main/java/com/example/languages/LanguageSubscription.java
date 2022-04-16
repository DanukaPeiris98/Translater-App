package com.example.languages;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;


public class LanguageSubscription extends AppCompatActivity {
    Database myDB;


    private Button btnUpdate;
    private ListView listView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);

        btnUpdate = findViewById(R.id.btnUpdate);
        listView3 = findViewById(R.id.listView3);
        myDB = new Database(this);


        final ArrayList<String> langList = new ArrayList<>();
        final Cursor data = myDB.getLanList();

        if (data.getCount() == 0) {
            Languages languages = new Languages();
            languages.execute();

            while (data.moveToNext()) {

                langList.add(data.getString(0));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, langList);
                listView3.setAdapter(listAdapter);
                listView3.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listAdapter = new ArrayAdapter(LanguageSubscription.this, android.R.layout.simple_list_item_multiple_choice, langList);
                listView3.setAdapter(listAdapter);
            }
        } else {
            while (data.moveToNext()) {

                langList.add(data.getString(0));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, langList);
                listView3.setAdapter(listAdapter);
                listView3.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listAdapter = new ArrayAdapter(LanguageSubscription.this, android.R.layout.simple_list_item_multiple_choice, langList);
                listView3.setAdapter(listAdapter);
            }
        }
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(LanguageSubscription.this, "Selected " + langList.get(i), Toast.LENGTH_SHORT).show();
                //get selected language

            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor result = myDB.checkresult();

                if (result.getCount() == 0) {
                    //get subscription languages
                    int choices = listView3.getCount();
                    SparseBooleanArray sparseBooleanArray = listView3.getCheckedItemPositions();

                    for (int i = 0; i < choices; i++) {
                        if (sparseBooleanArray.get(i)) {

                            addSubLan(listView3.getItemAtPosition(i).toString());
                        }
                    }
                } else {

                    myDB.deleteSelected();
                    //get subscription languages
                    int choices = listView3.getCount();
                    SparseBooleanArray sparseBooleanArray = listView3.getCheckedItemPositions();

                    for (int i = 0; i < choices; i++) {
                        if (sparseBooleanArray.get(i)) {

                            addSubLan(listView3.getItemAtPosition(i).toString());
                        }
                    }
                }
            }
        });

    }


    private class Languages extends AsyncTask<Void, Void, List<IdentifiableLanguage>> {
        private List<IdentifiableLanguage> getLanguages;

        @Override
        protected List<IdentifiableLanguage> doInBackground(Void... voids) {

            IamAuthenticator authenticator = new IamAuthenticator("gJPlRYASKOe_uG6a6KcttiR7louYb-EK0LfqYpwxfuuA");
            LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
            languageTranslator.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/8044a31b-ddfb-4ff4-8a2a-2231c62069e5");
            IdentifiableLanguages languages = languageTranslator.listIdentifiableLanguages().execute().getResult();
            System.out.println(languages);
            getLanguages = languages.getLanguages();
            return getLanguages;
        }

        @Override
        protected void onPostExecute(List<IdentifiableLanguage> listLanguages) {
            super.onPostExecute(listLanguages);
            for (IdentifiableLanguage fL : listLanguages) {
                String code = fL.getLanguage();
                String words = fL.getName();
                myDB.addLang(words, code);

            }
        }
    }

    public void addSubLan(String addedLan) {

        boolean includeLan = myDB.addSubsLan(addedLan);

        if (includeLan == true) {
            Toast.makeText(this, "Data Successfully Inserted", LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong", LENGTH_LONG).show();
        }
    }


}



