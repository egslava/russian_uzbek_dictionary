package ru.egslava.tatar_dictionary;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

import ru.egslava.tatar_dictionary.db.DB;
import ru.egslava.tatar_dictionary.db.DBHelper;

/**
 * Created by egslava on 01/04/15.
 */
@EActivity
public class ChooseLanguageActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public ListView listView;
    public ArrayAdapter<String> adapter;

                    String[]    languagesCodes = {"uz"};
    @StringArrayRes String[]    languages;
    @Bean           DB          db;         // init db with data

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.choose_language);

        listView = new ListView(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, languages);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        setContentView(listView);
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
