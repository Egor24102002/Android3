package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayAdapter<String> TextAdapter;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> selected = new ArrayList<String>();
    ListView textList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        textList = findViewById(R.id.textList);

        TextAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        textList.setAdapter(TextAdapter);
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