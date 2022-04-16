package com.example.languages;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Translate extends AppCompatActivity {
    private LanguageTranslator translateWord;
    private String selected;
    private String languageName;
    private Spinner spinner;
    Database myDB;
    private ListView listView4;
    private Button btnTranslate, btnPronounce;
    private TextView answerWord;
    private String code;
    private String tWord;

    private StreamPlayer player = new StreamPlayer();
    private TextToSpeech textService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        btnTranslate = findViewById(R.id.btnTrans);
        btnPronounce = findViewById(R.id.btnPron);
        spinner = findViewById(R.id.spinner);
        myDB = new Database(this);
        listView4 = findViewById(R.id.listView4);
        answerWord = findViewById(R.id.answerWord);


        ArrayList<String> subLan = new ArrayList<>();
        Cursor sub = myDB.getSubLan();

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectWord(selected);
            }
        });
        btnPronounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textService = initTextToSpeechService();
                new SynthesisTask().execute(tWord);
            }
        });

        if (sub.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (sub.moveToNext()) {

                subLan.add(sub.getString(0));
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subLan);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
            }
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + languageName, Toast.LENGTH_LONG).show();
                Cursor codeId = myDB.getCode(languageName);
                System.out.println(languageName);

                while (codeId.moveToNext()) {
                    code = codeId.getString(1);
                }

                System.out.println(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


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
                listView4.setAdapter(listAdapter);
                listView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Toast.makeText(Translate.this, "Selected " + theList.get(i), Toast.LENGTH_SHORT).show();
                        selected = theList.get(i);
                    }
                });
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

    //fpr translate
    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator("gJPlRYASKOe_uG6a6KcttiR7louYb-EK0LfqYpwxfuuA");
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl("https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/8044a31b-ddfb-4ff4-8a2a-2231c62069e5");
        return service;
    }

    private class TranslationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            TranslateOptions translateOptions = new TranslateOptions.Builder()
                    .addText(params[0])
                    .source(Language.ENGLISH)
                    .target(code)
                    .build();
            TranslationResult result = translateWord.translate(translateOptions).execute().getResult();
            String firstTranslation = result.getTranslations().get(0).getTranslation();
            return firstTranslation;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //set answer to text view
            answerWord.setText(s);
            tWord = s;
        }

    }

    //get selected word and translate it
    public void selectWord(String word) {
        translateWord = initLanguageTranslatorService();
        new TranslationTask().execute(word);

    }

    //for pronounce
    private TextToSpeech initTextToSpeechService() {
        Authenticator authenticator = new IamAuthenticator("YLFrRWHG1BXPSwticsb-eKcX6Rn4Q0geIZhbn7xhrxdT");
        TextToSpeech service = new TextToSpeech(authenticator);
        service.setServiceUrl("https://api.eu-gb.text-to-speech.watson.cloud.ibm.com/instances/e08ab862-33eb-4b72-99c4-4e8c30f7f3da");
        return service;
    }

    private class SynthesisTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
                    .text(params[0])
                    .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                    .accept(HttpMediaType.AUDIO_WAV)
                    .build();
            player.playStream(textService.synthesize(synthesizeOptions).execute().getResult());
            return "Did synthesize";
        }


    }
}
    

