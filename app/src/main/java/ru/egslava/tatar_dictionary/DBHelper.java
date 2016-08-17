package ru.egslava.tatar_dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by egslava on 29/11/14.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static String createTable(String tableName) {
        return String.format(
                "CREATE TABLE %s ( `_id` INTEGER, `word` CHAR(255), `definition` TEXT, PRIMARY KEY(_id) );",
                tableName);
    }

    public static String insertWord(String tableName) {
        return String.format( "INSERT INTO %s (word,definition) VALUES (?, ?)",
                tableName);
    }


    private final Context context;

    public DBHelper(Context context) {
        super(context, "db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        fillWords(db, "from_rus", R.raw.from_rus);
        fillWords(db, "to_rus", R.raw.to_rus);
    }

    private void fillWords(SQLiteDatabase db, String tableName, int wordFileResId) {
        try {
            db.execSQL( createTable(tableName) );

            InputStream inputStream = context.getResources().openRawResource( wordFileResId );
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader buf = new BufferedReader(reader);

            String s;
            while( buf.ready() ){
                s = buf.readLine();
                db.execSQL(insertWord( tableName), s.split("%"));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
