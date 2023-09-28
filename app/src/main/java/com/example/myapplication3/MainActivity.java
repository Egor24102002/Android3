package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
final String TAG ="lifecycle";
Button button;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.next);
        intent =new Intent(this,ListActivity.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = findViewById(R.id.login);
                String login = text.getText().toString();
                intent.putExtra("login", login);
startActivity(intent);

            }
        });
        Log.d(TAG,"MainActivity создано");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"MainActivity стартовало");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"MainActivity получает фокус");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"MainActivity приостановлено");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"MainActivity остановлено");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"MainActivity уничтожено");
    }


}