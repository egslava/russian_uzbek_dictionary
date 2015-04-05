package ru.egslava.tatar_dictionary;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.res.StringArrayRes;

import ru.egslava.tatar_dictionary.db.DB;
import ru.egslava.tatar_dictionary.themes.ChooseThemeActivity_;

/**
 * Created by egslava on 01/04/15.
 */
@EActivity
public class ChooseLanguageActivity extends ActionBarActivity {

//    public ListView listView;
//    public ArrayAdapter<String> adapter;

    public static final String[]    languagesCodes = {"uz", "tj", "kg", "kz", "az"};
    @Bean           DB              db;         // init db with data

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setTitle(R.string.choose_language);
//
//        listView = new ListView(this);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, languages);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);
//
//        setContentView(listView);
        ChooseThemeActivity_.intent(this).language( Flavor.language ).start();
        finish();
    }
//
//    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        ChooseThemeActivity_.intent(this).language(languagesCodes[position]).start();
//    }
}

// Must have regexes for data convertion:
//^\d+\%[^%.]+\%[^%]+$          less fields than expected
//        ^\d+\%[^%.]+$         less fields than expected
//
//        \%$ -> \%1            ends with just % without any group id
//        $\n(\D) -> ' $1'      same shit
//        \n(\D) ->  $1         fucken line-breakings in places they aren't needed