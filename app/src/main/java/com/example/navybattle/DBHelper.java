package com.example.navybattle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Bogachov on 09.11.16.
 */

 class DBHelper extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table mytable ("
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


        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        Log.d(LOG_TAG, "--- Insert in mytable: ---");
        // подготовим данные для вставки в виде пар: наименование столбца - значение

        for (int i=0;i<10;i++) {
            cv.put("c1", 0);
            cv.put("c2", 0);
            cv.put("c3", 0);
            cv.put("c5", 0);
            cv.put("c4", 0);
            cv.put("c6", 0);
            cv.put("c7", 0);
            cv.put("c8", 0);
            cv.put("c9", 0);
            cv.put("c10", 0);
            // вставляем запись и получаем ее ID
            long rowID = db.insert("mytable", null, cv);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
