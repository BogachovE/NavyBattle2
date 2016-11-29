package com.example.navybattle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class Reg extends Activity {
    EditText name_e;
    Button ok_btn;
    ListView users_list;
    ArrayList<String> users;
    DBHelper dbHelper;
    String cUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);


        Hawk.init(this).build();



        //Find Views
        name_e = (EditText) findViewById(R.id.name_e);

        ok_btn = (Button) findViewById(R.id.ok_btn);
        users_list = (ListView) findViewById(R.id.users_list);

        dbHelper = new DBHelper(this);



        dbRead();



        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, users);

        // присваиваем адаптер списку
        users_list.setAdapter(adapter);

        users_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cUser = users.get(i);
                Hawk.put("cUser",cUser);
                Intent toMain = new Intent (getApplicationContext(),Main.class);
                startActivity(toMain);


            }
        });

    }

    //Onclisk method
    public void OK(View view)	{
       if(!name_e.getText().toString().equals("")) {

           SQLiteDatabase db = dbHelper.getWritableDatabase();
           cUser = name_e.getText().toString();


         db.execSQL("create table IF NOT EXISTS "+name_e.getText().toString()+"table ("
                   + "id integer primary key autoincrement,"
                   + "c1 integer,"
                   + "c2 integer,"
                   + "c3 integer,"
                   + "c4 integer,"
                   + "c5 integer,"
                   + "c6 integer,"
                   + "c7 integer,"
                   + "c8 integer,"
                   + "c9 integer,"
                   + "c10 integer" + ");");



           ContentValues cv = new ContentValues();


           Log.d("s", "--- Rows in mytable: ---");
           // делаем запрос всех данных из таблицы mytable, получаем Cursor
           Cursor c = db.query("listUser", null, null, null, null, null, null);

           Log.d("s", "--- Insert in mytable: ---");
           // подготовим данные для вставки в виде пар: наименование столбца - значение


               cv.put("name",cUser);

               // вставляем запись и получаем ее ID
               long rowID = db.insert("listUser", null, cv);
               Log.d("s", "row inserted, ID = " + rowID);

           db.execSQL("create table IF NOT EXISTS "+cUser+"Rating ("
                   + "id integer primary key autoincrement,"
                   + "winEasy integer,"
                   + "winNormal integer,"
                   + "winHard integer,"
                   + "winVeryHard integer,"
                   + "looseEasy integer,"
                   + "looseNormal integer,"
                   + "looseHard integer,"
                   + "looseVeryHard integer" + ");");

           cv.clear();
           cv.put("winEasy",0);
           cv.put("winNormal",0);
           cv.put("winHard",0);
           cv.put("winVeryHard",0);
           cv.put("looseEasy",0);
           cv.put("looseNormal",0);
           cv.put("looseHard",0);
           cv.put("looseVeryHard",0);
            rowID = db.insert(cUser+"Rating", null, cv);





           Hawk.put("gameCount",0);
           Hawk.put("cUser",name_e.getText().toString());
           Intent toMain = new Intent(this, Main.class);
           startActivity(toMain);
       }



    }

    public void dbRead(){



        dbHelper = new DBHelper(this);

        // создаем объект для данных
        ContentValues cv = new ContentValues();


        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("listUser", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        users = new ArrayList<>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");


            do {
                // получаем значения по номерам столбцов и пишем все в лог

                users.add(users.size(),c.getString(nameColIndex));

                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("er", "error");
        c.close();
    }





}
