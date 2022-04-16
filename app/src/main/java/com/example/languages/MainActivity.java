package com.example.languages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnAdd, viewBtn, btnEditPh, btnLang, btnTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.addPharses);
        viewBtn = findViewById(R.id.displayPharses);
        btnEditPh = findViewById(R.id.editPhrases);
        btnLang = findViewById(R.id.langSub);
        btnTranslate = findViewById(R.id.btnTranslate);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, AddPhrases.class);
                startActivity(intent1);
            }
        });
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, DisplayPhrases.class);
                startActivity(intent2);
            }
        });
        btnEditPh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, EditPhrases.class);
                startActivity(intent3);
            }
        });
        btnLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(MainActivity.this, LanguageSubscription.class);
                startActivity(intent4);
            }
        });

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(MainActivity.this, Translate.class);
                startActivity(intent5);
            }
        });
    }
}
