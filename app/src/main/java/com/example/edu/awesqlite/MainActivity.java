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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyDBOpenHelper dbHelper;
    SQLiteDatabase mdb;
    EditText countryEditText, cityEditText;
    Button btn_insert, btn_read, btn_update, btn_delete;
    TextView txt_content;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDBOpenHelper(this, "awe.db", null,1);
        mdb = dbHelper.getWritableDatabase();

        countryEditText = (EditText)findViewById(R.id.country);
        cityEditText = (EditText)findViewById(R.id.city);
        btn_insert = (Button)findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(this);
        btn_read = (Button)findViewById(R.id.btn_read);
        btn_read.setOnClickListener(this);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        txt_content = (TextView)findViewById(R.id.txt_content);
    }

    @Override
    public void onClick(View v) {
        String country = countryEditText.getText().toString();
        String city = cityEditText.getText().toString();
        int id;
        String str = "";
        Cursor cursor;
        switch (v.getId()){
            case R.id.btn_insert:
                mdb.execSQL("INSERT INTO awe_country  VALUES( null, '" + country + "', '" + city + " ');");
                countryEditText.setText("");
                cityEditText.setText("");
                break;

            case R.id.btn_read:
                query = "SELECT * FROM awe_country";
                cursor = mdb.rawQuery(query,null);

                while (cursor.moveToNext()){
                    id = cursor.getInt(0);
                    country = cursor.getString(cursor.getColumnIndex("country"));
                    city = cursor.getString(2);
                    str +=(id + ":" + country + "-" + city + "\n");
                }
                txt_content.setText(str);
                txt_content.setMovementMethod(new ScrollingMovementMethod());
                break;

            case R.id.btn_update:
                mdb.execSQL("UPDATE awe_country SET capital='seoul' WHERE country='eee';");
                query = "SELECT * FROM awe_country ";
                cursor = mdb.rawQuery(query,null);

                while (cursor.moveToNext()){
                    id = cursor.getInt(0);
                    country = cursor.getString(cursor.getColumnIndex("country"));
                    city = cursor.getString(2);
                    str +=(id + ":" + country + "-" + city + "\n");
                }
                txt_content.setText(str);
                break;

            case R.id.btn_delete:
                mdb.execSQL("delete from awe_country where country='ggg';");
                query = "SELECT * FROM awe_country order by _id desc ";
                cursor = mdb.rawQuery(query,null);

                while (cursor.moveToNext()){
                    id = cursor.getInt(0);
                    country = cursor.getString(cursor.getColumnIndex("country"));
                    city = cursor.getString(2);
                    str +=(id + ":" + country + "-" + city + "\n");
                }
                txt_content.setText(str);
                break;
        }



    }
}
