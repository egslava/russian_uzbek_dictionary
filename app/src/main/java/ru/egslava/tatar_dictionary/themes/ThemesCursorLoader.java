package ru.egslava.tatar_dictionary.themes;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import ru.egslava.tatar_dictionary.db.DBHelper;

/**
 * Created by egslava on 01/04/15.
 */
public class ThemesCursorLoader extends CursorLoader {

    public final DBHelper db;
    public final String language;

    public ThemesCursorLoader(Context context, DBHelper db, String language) {
        super(context);
        this.db = db;
        this.language = language;
    }

    @Override public Cursor loadInBackground() {
        return db.getReadableDatabase().query(language + DBHelper.THEMES, null, null, null, null, null, null);
    }
}
