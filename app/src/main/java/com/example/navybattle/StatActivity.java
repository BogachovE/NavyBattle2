package com.example.navybattle;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class StatActivity extends Activity {
    
    TableLayout tablelayout;
    TableRow tablerow;
    TextView textview[];
    ArrayList<Integer> fromBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat2);

        Intent fromAgainst = getIntent();
        fromBD = new ArrayList<>();
        fromBD = fromAgainst.getIntegerArrayListExtra("fromBD");

        tablelayout = (TableLayout)findViewById(R.id.tablelayout);
        tablerow = (TableRow)findViewById(R.id.tablerow);
        textview = new TextView[100];

        if(fromBD!=null) {
            int k = 0;
            int m = 0;
            for (int i = 0; i < 10; i++) {
                TableRow tR = new TableRow(this);
                for (int j = 0; j < 10; j++) {
                    textview[k] = new TextView(this);
                    textview[k].setText(fromBD.get(j + m).toString()+"  ");
                    tR.addView(textview[k]);
                    k++;
                }
                m = m + 10;
                tablelayout.addView(tR);
            }
        }else Toast.makeText(this, "You have not any statictic", Toast.LENGTH_SHORT).show();







    }
}
