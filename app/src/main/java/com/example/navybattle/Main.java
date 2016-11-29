//********VERSION 0.98************
//*******---Ship Placement activity added
//*******---
//*******---

package com.example.navybattle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.orhanobut.hawk.Hawk;

public class Main extends Activity {

	Button btn,btn_dell,btn_dellBD;
	final String LOG_TAG = "myLogs";
	DBHelper dbHelper;
	Button btn_add,rating_btn;
	ImageButton set_btn;
	String cUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Hawk.init(this).build();
		cUser=Hawk.get("cUser");
		dbHelper = new DBHelper(this);
		set_btn = (ImageButton)findViewById(R.id.set_btn) ;




		rating_btn =(Button)findViewById(R.id.rating_btn);
		rating_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// создаем объект для данных
				ContentValues cv = new ContentValues();


				// подключаемся к БД
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				Log.d(LOG_TAG, "--- Rows in mytable: ---");
				// делаем запрос всех данных из таблицы mytable, получаем Cursor
				Cursor c = db.query(cUser+"Rating", null, null, null, null, null, null);

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
						Log.d("d"," winEasy="  +c.getInt(winEasyColIndex)
						          +"winNormal="+c.getInt(winNormalColIndex)
								  +"winHard="  +c.getInt(winHardColIndex)
								+"winVeryHard="+c.getInt(winVeryHardColIndex)
						        +"looseEasy="  +c.getInt(looseEasyColIndex)
						       +"looseNormal=" +c.getInt(looseNormalColIndex)
						         +"looseHard=" +c.getInt(looseHardColIndex)
						     +"looseVeryHard=" +c.getInt(looseVeryHardColIndex));


					} while (c.moveToNext());
				} else
					Log.d("er", "0 rows");
				c.close();

			}
		});

		btn_dellBD = (Button)findViewById(R.id.btn_dellBD);
		btn_dellBD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dbHelper.close ();
				deleteDatabase("myDB");

			}
		});

		btn =(Button)findViewById(R.id.btn);
		btn_dell =(Button) findViewById(R.id.btn_dell);
		btn_dell.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				// создаем объект для данных
				ContentValues cv = new ContentValues();


				// подключаемся к БД
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				Log.d(LOG_TAG, "--- Rows in mytable: ---");
				// делаем запрос всех данных из таблицы mytable, получаем Cursor
				Cursor c = db.query(cUser+"table", null, null, null, null, null, null);

				Log.d(LOG_TAG, "--- Clear mytable: ---");
				// удаляем все записи
				int clearCount = db.delete(cUser+"table", null, null);
				Log.d(LOG_TAG, "deleted rows count = " + clearCount);
			}
		});

		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				// создаем объект для данных
				ContentValues cv = new ContentValues();


				// подключаемся к БД
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				Log.d(LOG_TAG, "--- Rows in mytable: ---");
				// делаем запрос всех данных из таблицы mytable, получаем Cursor
				Cursor c = db.query(cUser+"table", null, null, null, null, null, null);

				Log.d(LOG_TAG, "--- Insert in mytable: ---");
				// подготовим данные для вставки в виде пар: наименование столбца - значение

				cv.put("c1", "sdas");
				cv.put("c2", "sdasd");
				cv.put("c3", "sdasd");
				cv.put("c4", "sdasd");
				cv.put("c5", "sdasd");
				cv.put("c6", "sdasd");
				cv.put("c7", "sdasd");
				cv.put("c8", "sdasd");
				cv.put("c9", "sdasd");
				cv.put("c10", "sdasd");
				// вставляем запись и получаем ее ID
				long rowID = db.insert(cUser+"table", null, cv);
				Log.d(LOG_TAG, "row inserted, ID = " + rowID);
			}
		});



		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				// создаем объект для данных
				ContentValues cv = new ContentValues();


				// подключаемся к БД
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				Log.d(LOG_TAG, "--- Rows in mytable: ---");
				// делаем запрос всех данных из таблицы mytable, получаем Cursor
				Cursor c = db.query(cUser+"table", null, null, null, null, null, null);

				// ставим позицию курсора на первую строку выборки
				// если в выборке нет строк, вернется false
				if (c.moveToFirst()) {

					// определяем номера столбцов по имени в выборке
					int idColIndex = c.getColumnIndex("id");
					int с1ColIndex = c.getColumnIndex("c1");
					int с2ColIndex = c.getColumnIndex("c2");
					int с3ColIndex = c.getColumnIndex("c3");
					int с4ColIndex = c.getColumnIndex("c4");
					int с5ColIndex = c.getColumnIndex("c5");
					int с6ColIndex = c.getColumnIndex("c6");
					int с7ColIndex = c.getColumnIndex("c7");
					int с8ColIndex = c.getColumnIndex("c8");
					int с9ColIndex = c.getColumnIndex("c9");
					int с10ColIndex = c.getColumnIndex("c10");

					do {
						// получаем значения по номерам столбцов и пишем все в лог
						Log.d(LOG_TAG,
								"ID = " + c.getInt(idColIndex) +
										", с1 = " + c.getString(с1ColIndex) +
										", с2 = " + c.getString(с2ColIndex) +
										", с3 = " + c.getString(с3ColIndex) +
										", с4 = " + c.getString(с4ColIndex) +
										", с5 = " + c.getString(с5ColIndex) +
										", с6 = " + c.getString(с6ColIndex) +
										", с7 = " + c.getString(с7ColIndex) +
										", с8 = " + c.getString(с8ColIndex) +
										", с9 = " + c.getString(с9ColIndex) +
										", с10 = " + c.getString(с10ColIndex));
						// переход на следующую строку
						// а если следующей нет (текущая - последняя), то false - выходим из цикла
					} while (c.moveToNext());
				} else
					Log.d(LOG_TAG, "0 rows");
				c.close();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void NewGame(View view)	{
		
		Intent newgame = new Intent (this,ShipPlacement.class);
		startActivity(newgame);
		
	}
	
	public void prefs (View view)	{
		Intent prefs = new Intent (this,Settings.class);
		startActivity(prefs);
	}

	public  void toStat (View view){

		Intent toStat = new Intent (this,Stat.class);
		startActivity(toStat);

	}

}
