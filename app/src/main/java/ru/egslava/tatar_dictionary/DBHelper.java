package ru.egslava.tatar_dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ru.egslava.tatar_dictionary.R;

/**
 * Created by egslava on 29/11/14.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String PHRASES = "phrases", THEMES = "themes";

    public static String createTablePhrases(String languageName) {
        return String.format(
                "CREATE TABLE %s ( `_id` INTEGER, `russian` TEXT, `foreign` TEXT, `id_theme` INTEGER, PRIMARY KEY(_id) );",
                languageName + PHRASES);
    }

    public static String createTableThemes(String languageName) {
        return String.format(
                "CREATE TABLE %s ( `_id` INTEGER, `russian` TEXT, `foreign` TEXT, PRIMARY KEY(_id) );",
                languageName + THEMES);
    }

    public static String insertPhrase(String languageName) {
        return String.format(
                "INSERT INTO %s (_id, russian, foreign, id_theme) VALUES (?, ?, ?, ?);",
                languageName + PHRASES);
    }

    public static String insertTheme( String languageName ) {
        return String.format(
                "INSERT INTO %s (_id, russian, foreign) VALUES(?, ?, ?);",
                languageName + THEMES);
    }

    private final Context context;

    public DBHelper(Context context) {
        super(context, "db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        fill(db, "uz", R.raw.uz_phrases, R.raw.uz_themes);
    }

    private void fill(SQLiteDatabase db, String languageName, int phrasesFileId, int themesFileId){
        fillPhrases (db, languageName, phrasesFileId);
        fillThemes  (db, languageName, themesFileId);
    }

    private void fillPhrases(SQLiteDatabase db, String languageName, int phrasesFileResId) {
        try {
            db.execSQL( createTablePhrases(languageName));

            InputStream inputStream = context.getResources().openRawResource( phrasesFileResId );
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader buf = new BufferedReader(reader);

            String s;
            while( buf.ready() ){
                s = buf.readLine();
                db.execSQL(insertPhrase(languageName), StringUtils.split(s, '%'));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillThemes(SQLiteDatabase db, String languageName, int themesFileResId) {
        try {
            db.execSQL( createTableThemes(languageName));

            InputStream inputStream = context.getResources().openRawResource( themesFileResId );
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader buf = new BufferedReader(reader);

            String s;
            while( buf.ready() ){
                s = buf.readLine();
                db.execSQL(insertTheme( languageName ), StringUtils.split(s, '%'));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
