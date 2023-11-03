package com.example.myapplication3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
final String TAG ="lifecycle";
Button button,button_load,button_save,button_auth,button_change;
    Intent intent;
    Boolean authorization;
    Toast toast,auth,change;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        authorization=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.next);
        button_change=(Button) findViewById(R.id.change);
        button_load = (Button) findViewById(R.id.load);
        button_save = (Button) findViewById(R.id.save);
        button_auth = (Button) findViewById(R.id.auth);
        intent =new Intent(this,ListActivity.class);
        EditText Login = findViewById(R.id.login);
        EditText Password = findViewById(R.id.password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = Login.getText().toString();
                String password=Password.getText().toString();

                intent.putExtra("login", login);
                if (authorization==true)
                startActivity(intent);
                authorization=false;
            }
        });

        button_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> usersList = new ArrayList<User>();
                usersList=db.getUsers(new User(Login.getText().toString(), Password.getText().toString()));
                authorization=true;
                if (usersList.isEmpty()){
                    authorization=false;
                };
                if (authorization==true){
                    auth = Toast.makeText(MainActivity.this,"Вы успешно авторизовались" ,Toast.LENGTH_SHORT);
                    auth.setGravity(Gravity.TOP, 0,160);
                    auth.show();
                };
            }
        });
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authorization==true){
                    db.ChangePassword(new User(Login.getText().toString(), Password.getText().toString()));
                    change = Toast.makeText(MainActivity.this,"Пароль изменён" ,Toast.LENGTH_SHORT);
                    change.setGravity(Gravity.TOP, 0,160);
                    change.show();
                };
            }
        });
        button_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> users = db.getAllUsers();
                for (User usr : users) {
                    String log = "Id: "+usr.getID()+" ,Login: " + usr.getLogin() + " ,Password: " + usr.getPass();
                    toast = Toast.makeText(MainActivity.this,log ,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0,160);
                    toast.show();
                    Log.v("Loading...", log);

                }


            }
        });
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addUser(new User(Login.getText().toString(), Password.getText().toString()));
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
        db.close();
    }
    public class User {

        int _id;
        String _login;
        String _pass;

        public User(){
        }
        public User(int id, String login, String pass){
            this._id = id;
            this._login = login;
            this._pass = pass;
        }
        public User(String login, String pass){
            this._login = login;
            this._pass = pass;
        }

        public int getID(){
            return this._id;
        }
        public void setID(int id){
            this._id = id;
        }

        public String getLogin(){
            return this._login;
        }
        public void setLogin(String login){
            this._login = login;
        }

        public String getPass(){
            return this._pass;
        }
        public void setPass(String pass){
            this._pass = pass;
        }
    }


    public static final class DBContract {

        private DBContract() {
        }

        public static class UserEntry implements BaseColumns {
            public static final String TABLE_NAME = "users";
            public static final String COLUMN_NAME_KEY_ID = "id";
            public static final String COLUMN_NAME_LOGIN  = "login";
            public static final String COLUMN_NAME_PASS  = "pass";
        }
    }
    public class DatabaseHandler extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Users.db";

        public DatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_USERS_TABLE = "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + "("
                    + DBContract.UserEntry.COLUMN_NAME_KEY_ID + " INTEGER PRIMARY KEY," +
                    DBContract.UserEntry.COLUMN_NAME_LOGIN + " TEXT," + DBContract.UserEntry.COLUMN_NAME_PASS + " TEXT" + ")";

            db.execSQL(CREATE_USERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME);
            onCreate(db);
        }

        public void addUser(User user) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBContract.UserEntry.COLUMN_NAME_LOGIN, user.getLogin());
            values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.getPass());

            db.insert(DBContract.UserEntry.TABLE_NAME, null, values);
            db.close();
        }
        public List<User> getAllUsers() {
            List<User> usersList = new ArrayList<User>();
            String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setID(Integer.parseInt(cursor.getString(0)));
                    user.setLogin(cursor.getString(1));
                    user.setPass(cursor.getString(2));
                    usersList.add(user);
                } while (cursor.moveToNext());
            }
            return usersList;
        }

        public List<User> getUsers(User user) {
            List<User> usersList = new ArrayList<User>();
            String selectQuery = "SELECT  * FROM " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_NAME_LOGIN + " = '" + user.getLogin() + "'"+ " AND " + DBContract.UserEntry.COLUMN_NAME_PASS + " = '" + user.getPass() + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    User user_res = new User();
                    user_res.setID(Integer.parseInt(cursor.getString(0)));
                    user_res.setLogin(cursor.getString(1));
                    user_res.setPass(cursor.getString(2));
                    usersList.add(user_res);
                } while (cursor.moveToNext());
            }
            return usersList;
        }

        public void ChangePassword(User user) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.getPass());
            db.update(DBContract.UserEntry.TABLE_NAME, values, DBContract.UserEntry.COLUMN_NAME_LOGIN + "='" +  user.getLogin() + "'", null);
            db.close();
        }
    }

}