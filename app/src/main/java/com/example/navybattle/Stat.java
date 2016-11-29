package com.example.navybattle;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

public class Stat extends Activity {
    SQLiteDatabase db;
    DBHelper dbHelper;
    Cursor c;
    TextView easywin,easyloose,normalwin,normalloose,hardwin,hardloose,veryhardwin,veryhardloose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        dbHelper = new DBHelper(this);

        easywin = (TextView)findViewById(R.id.easywin);
        easyloose = (TextView)findViewById(R.id.easyloose);
        normalwin = (TextView)findViewById(R.id.normalwin);
        normalloose = (TextView)findViewById(R.id.normalloose);
        hardwin = (TextView)findViewById(R.id.hardwin);
        hardloose = (TextView)findViewById(R.id.hardloose);
        veryhardwin = (TextView)findViewById(R.id.veryhardwin);
        veryhardloose = (TextView)findViewById(R.id.veryhardloose);

        Hawk.init(this).build();
        loadScore();

    }

    public void loadScore(){
        String cUser;

        cUser = Hawk.get("cUser");


        // создаем объект для данных
        ContentValues cv = new ContentValues();


        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //принимаем значения из БД


        c = db.query(cUser + "Rating", null, null, null, null, null, null);

        if (c.moveToFirst()) {



            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int winEasyColIndex = c.getColumnIndex("winEasy");
            int winNormalColIndex = c.getColumnIndex("winNormal");
            int winHardColIndex = c.getColumnIndex("winHard");
            int winVeryHardColIndex = c.getColumnIndex("winVeryHard");
            int looseEasyColIndex = c.getColumnIndex("looseEasy");
            int looseNormalColIndex = c.getColumnIndex("looseNormal");
            int looseHardColIndex = c.getColumnIndex("looseHard");
            int looseVeryHardColIndex = c.getColumnIndex("looseVeryHard");

            do {
                easywin.setText(c.getString(winEasyColIndex));
                easyloose.setText(c.getString(looseEasyColIndex));
                normalwin.setText(c.getString(winNormalColIndex));
                normalloose.setText(c.getString(looseNormalColIndex));
                hardwin.setText(c.getString(winHardColIndex));
                hardloose.setText(c.getString(looseHardColIndex));
                veryhardwin.setText(c.getString(winVeryHardColIndex));
                veryhardloose.setText(c.getString(looseVeryHardColIndex));
            } while (c.moveToNext());
        } else
            Log.d("er", "0 rows");

    }
}
