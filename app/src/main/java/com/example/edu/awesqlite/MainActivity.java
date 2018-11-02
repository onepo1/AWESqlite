package com.example.edu.awesqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyDBOpenHelper dbHelper;
    SQLiteDatabase mdb;
    EditText countryEditText, cityEditText;
    Button btn_insert, btn_read, btn_update, btn_delete, btn_search, btn_addVisited;
    TextView txt_content,txt_PKID,txt_visitTotal;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDBOpenHelper(this, "awe.db", null,1);
        mdb = dbHelper.getWritableDatabase();

        countryEditText = (EditText)findViewById(R.id.country);
        cityEditText = (EditText)findViewById(R.id.city);


        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        btn_addVisited = (Button)findViewById(R.id.btn_addVisited);
        btn_addVisited.setOnClickListener(this);

        btn_insert = (Button)findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(this);
        btn_read = (Button)findViewById(R.id.btn_read);
        btn_read.setOnClickListener(this);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        txt_content = (TextView)findViewById(R.id.txt_content);
        txt_PKID = (TextView)findViewById(R.id.txt_PKID);
        txt_visitTotal = (TextView)findViewById(R.id.txt_visitTotal);
    }

    @Override
    public void onClick(View v) {
        String country = countryEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String strPKID = txt_PKID.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String pkid = format.format(new Date());
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        String datetime = format.format(new Date());
        String id;
        String str = "";
        Cursor cursor;
        switch (v.getId()){
            case R.id.btn_insert:
                mdb.execSQL("INSERT INTO awe_country(pkid, country, capital)  VALUES('" + pkid + "', '" + country + "', '" + city + " ');");
                countryEditText.setText("");
                cityEditText.setText("");
                break;

            case R.id.btn_read:
                query = "SELECT * FROM awe_country";
                cursor = mdb.rawQuery(query,null);

                while (cursor.moveToNext()){
                    id = cursor.getString(0);
                    country = cursor.getString(cursor.getColumnIndex("country"));
                    city = cursor.getString(2);
                    str +=(id + ":" + country + "-" + city + "\n");
                }
                txt_content.setText(str);
                txt_content.setMovementMethod(new ScrollingMovementMethod());
                break;

            case R.id.btn_update:
                mdb.execSQL("UPDATE awe_country SET capital='"+ city +"' WHERE country='"+ country +"';");
                query = "SELECT * FROM awe_country ";
                cursor = mdb.rawQuery(query,null);

                while (cursor.moveToNext()){
                    id = cursor.getString(0);
                    country = cursor.getString(cursor.getColumnIndex("country"));
                    city = cursor.getString(2);
                    str +=(id + ":" + country + "-" + city + "\n");
                }
                txt_content.setText(str);
                break;

            case R.id.btn_delete:
                mdb.execSQL("delete from awe_country where country='"+ country +"';");
                query = "SELECT * FROM awe_country order by _id desc ";
                cursor = mdb.rawQuery(query,null);

                while (cursor.moveToNext()){
                    id = cursor.getString(0);
                    country = cursor.getString(cursor.getColumnIndex("country"));
                    city = cursor.getString(2);
                    str +=(id + ":" + country + "-" + city + "\n");
                }
                txt_content.setText(str);
                break;

            case R.id.btn_search:
                query = "SELECT pkid as pkid, country, capital, count(fkid) visitedTotal FROM awe_country INNER JOIN awe_country_visitedcount ON pkid=fkid AND pkid='"+country+"'" ;
                cursor = mdb.rawQuery(query,null);
                txt_PKID.setText(pkid);
//                txt_visitTotal.setText();

//                if(cursor.getCount() > 0){
//                    cursor.moveToFirst();
//                    int visitedTotal = cursor.getInt(cursor.getColumnIndex("visitedTotal"));
//                    txt_visitTotal.setText(String.valueOf(visitedTotal));
//
//                }
                break;

            case R.id.btn_addVisited:
                query= "INSERT INTO awe_country_visitedcount(fkid) VALUES('" + strPKID + "');";
                mdb.execSQL(query);
                break;
        }



    }
}
