package com.example.myapplication3;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.BaseColumns;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText Login;
    EditText Password;
    Database db;
final String TAG ="lifecycle";

Button button,button_load,button_save,button_auth,button_change;
    Intent intent;
     Boolean authorization=false;
    Toast toast,auth,change;

   /* private void handleAuthorization(List<User> usersList) {
        authorization = !usersList.isEmpty();

        if (authorization) {
            auth = Toast.makeText(MainActivity.this, "Вы успешно авторизовались", Toast.LENGTH_SHORT);
            auth.setGravity(Gravity.TOP, 0, 160);
            auth.show();
        } else {
            Toast.makeText(MainActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
        }
    }*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SQLiteDatabase database = getBaseContext().openOrCreateDatabase("Users.db", MODE_PRIVATE, null);
        db = new Database(database, this);
        authorization = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.next);
        button_change = (Button) findViewById(R.id.change);
        button_change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickChange(v);
            }
        });
        // button_load = (Button) findViewById(R.id.load);

        button_save = (Button) findViewById(R.id.save);
        button_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickAdd(v);
            }
        });
        button_auth = (Button) findViewById(R.id.auth);
        button_auth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickEnter(v);
            }
        });
        intent = new Intent(this, ListActivity.class);
       Login = findViewById(R.id.login);
        Password = findViewById(R.id.password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = Login.getText().toString();
                String password = Password.getText().toString();

                intent.putExtra("login", login);
                if (authorization)
                    startActivity(intent);
                    else
                    Toast.makeText(MainActivity.this, "Вы не авторизовались", Toast.LENGTH_SHORT).show();
                authorization = false;
            }
        });
    }
        final Looper looper = Looper.getMainLooper();
        final Message message = Message.obtain();

        final Handler handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.sendingUid == 2) {
                    if(msg.obj == TRUE){
                    Toast.makeText(MainActivity.this, "Пользователь добавлен", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Ошибка добавления пользователя", Toast.LENGTH_SHORT).show();
                    }
                }
                if (msg.sendingUid == 1){
                    if(msg.obj == TRUE){
                        authorization = true;
                        auth = Toast.makeText(MainActivity.this, "Вы успешно авторизовались", Toast.LENGTH_SHORT);
                        auth.setGravity(Gravity.TOP, 0, 160);
                        auth.show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Неверно ввели данные", Toast.LENGTH_SHORT).show();
                    }

                }


                if (msg.sendingUid == 3) {
                    if(msg.obj == TRUE){
                        Toast.makeText(MainActivity.this, "Пароль изменён", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Неверно ввели данные", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

       /* button_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getusersList = new ArrayList<User>();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getusersList = db.getUsers(new User(Login.getText().toString(), Password.getText().toString()));

                        // После завершения запроса, выполните действия в основном потоке
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleAuthorization(getusersList);
                            }
                        });
                    }
                });

                thread.start();
            }
        });*/

    public void onClickEnter(View v) {
        String user = Login.getText().toString();
        String password = Password.getText().toString();
        Database.User usera = new Database.User(user,password);

        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Введи данные", Toast.LENGTH_SHORT).show();
            return;
        }
        new ThreadTask(handler).TryLogIn(db,usera);
//       if(TryCheckLogin(usera)) {
//           Intent intent = new Intent(this, List.class);
//           intent.putExtra("user", user);
//           startActivity(intent);
//       }
    }


    public void onClickChange(View v) {
        String user = Login.getText().toString();
        String password = Password.getText().toString();
        Database.User usera = new Database.User(user,password);

        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Укажите логин пользователя и его новый пароль", Toast.LENGTH_SHORT).show();
            return;
        }
        if (authorization==false)
        {
            Toast.makeText(this, "Вы не авторизовались", Toast.LENGTH_SHORT).show();
            return;
        }
        new ThreadTask(handler).TryChangePasswd(db,usera);
//        if(Database.dbh.ChangePass(usera)) {
//            Toast.makeText(this, "Пароль изменён", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Toast.makeText(this, "Такого пользователя не существует", Toast.LENGTH_SHORT).show();

    }
    public void onClickAdd(View v) {
        String user = Login.getText().toString();
        String password = Password.getText().toString();
        Database.User usera = new Database.User(user,password);
        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Укажите логин пользователя и его  пароль", Toast.LENGTH_SHORT).show();
            return;
        }


        new ThreadTask(handler).TryAddUser(db,usera);
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