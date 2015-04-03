package ru.egslava.tatar_dictionary.themes;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import ru.egslava.tatar_dictionary.db.DB;
import ru.egslava.tatar_dictionary.phrases.PhrasesActivity_;

/**
 * Created by egslava on 01/04/15.
 */

@EActivity
public class ChooseThemeActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    @Bean       DB          db;
    @Extra      String      language;
    public SimpleCursorAdapter adapter;
    public ListView listView;

    @Override   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
                null,
                new String[]{"russian", "foreign"},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);
        getSupportLoaderManager().initLoader(0, null, this);

        listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        setContentView(listView);
    }

    @Override   public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ThemesCursorLoader(this, db.db(), language);
    }

    @Override   public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override   public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor item = (Cursor)adapter.getItem(position);
        int themeId = item.getInt(item.getColumnIndex("_id"));
        PhrasesActivity_.intent(this)
                .languageName(language)
                .themeId(themeId)
                .start();
    }
}
