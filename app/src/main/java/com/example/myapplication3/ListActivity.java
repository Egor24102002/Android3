package com.example.myapplication3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ListActivity extends AppCompatActivity {

    final String TAG ="lifecycle2";
    ArrayAdapter<String> TextAdapter;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> selected = new ArrayList<String>();
    ArrayList<String> save = new ArrayList<String>();
    ListView textList;
    SharedPreferences settings ;
    public static final String APP_PREFERENCES = "User";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"ListActivity создано");
        settings = getApplicationContext().getSharedPreferences(APP_PREFERENCES, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        textList = findViewById(R.id.textList);

        TextAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        textList.setAdapter(TextAdapter);
        LoadPreferences();
        Bundle arguments = getIntent().getExtras();
        String login = arguments.get("login").toString();

        TextAdapter.add(login);
        TextAdapter.notifyDataSetChanged();
        textList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String item = TextAdapter.getItem(position);
                if(textList.isItemChecked(position))
                    selected.add(item);
                else
                    selected.remove(item);
            }
        });
    }
    protected void SavePreferences(){
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < TextAdapter.getCount(); i++) {
           set.add(TextAdapter.getItem(i));
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet("Users", set);
        editor.commit();
    }

    protected void LoadPreferences(){
        SharedPreferences data = this.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        Set<String> set = data.getStringSet("Users", null);
        if (Objects.equals(set, null)) return;
        TextAdapter.addAll(set);
        TextAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"ListActivity остановлено");
        SavePreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"ListActivity уничтожено");
        SavePreferences();
    }

    public void add (View view){

        EditText text = findViewById(R.id.editText);
        String user = text.getText().toString();
        if (!user.isEmpty()) {
            TextAdapter.add(user);
            text.setText("");
            TextAdapter.notifyDataSetChanged();
        }
    }
    public void remove (View view){
        for (int i = 0; i < selected.size(); i++) {
            TextAdapter.remove(selected.get(i));
        }
        textList.clearChoices();
        selected.clear();
        TextAdapter.notifyDataSetChanged();
    }
}