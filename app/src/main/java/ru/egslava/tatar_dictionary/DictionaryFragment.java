package ru.egslava.tatar_dictionary;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;


public class DictionaryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        FilterQueryProvider, SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    public static final int URL_LOADER = 0;

    MenuItem searchItem;

    ListView                        list;

    private SimpleCursorAdapter     adapter;
    private Cursor cursor;

    MainActivity ac;
    private SQLiteDatabase db;
    private SearchView actionSearch;
    private Uri uri;

    private View.OnClickListener letterInserter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (actionSearchEditText == null) return;   //is not inflated yet

            if (actionSearch.isIconified()){
                MenuItemCompat.expandActionView(searchItem);
            }
            actionSearchEditText.getText().insert(actionSearchEditText.getSelectionStart(), ((Button) v).getText());
        }
    };

    private EditText actionSearchEditText;

    String[]    letters;
    String      tableName;

    public static DictionaryFragment newInstance(String tableName, String[] letters){
        Bundle bundle = new Bundle();
        bundle.putString("table", tableName);
        bundle.putStringArray("letters", letters);
        DictionaryFragment frag = new DictionaryFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        letters = args.getStringArray("letters");
        tableName = args.getString("table");

    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);

                    list            = (ListView) view.findViewById(R.id.list);
        ViewGroup   lettersLayout   = (ViewGroup)view.findViewById(R.id.letters);

        uri = Uri.parse("content://" + tableName);

        ac = (MainActivity)getActivity();
        db = ac.db().getReadableDatabase();

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.item_word, null,
                new String[]{"word", "definition"},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);

        list.setTextFilterEnabled(true);
        adapter.setFilterQueryProvider(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        // Sets up lettersLayout
        lettersLayout.setVisibility(letters == null ? View.GONE : View.VISIBLE);
        if(letters != null)
        for(String letter: letters){

            Button letterButton = (Button)inflater.inflate(R.layout.view_button_letter, lettersLayout, false);
            letterButton.setText(letter);
            letterButton.setOnClickListener(letterInserter);
            lettersLayout.addView(letterButton);
        }

        getLoaderManager().initLoader(URL_LOADER, null, this);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.search);

        actionSearch = (SearchView) MenuItemCompat.getActionView(searchItem);
        actionSearch.setOnQueryTextListener(this);

        actionSearchEditText = (EditText) actionSearch.findViewById(android.support.v7.appcompat.R.id.search_src_text);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch(loaderId){
            case URL_LOADER:
                if (bundle == null) return new DictionaryCursorLoader(ac, uri, null, null, null, null);

                String filter = bundle.getString("filter");
                if (filter != null && !filter.trim().isEmpty()){
                    return new DictionaryCursorLoader(ac, uri, null, "word LIKE(?)", new String[]{filter + "%"}, null);
                }else{
                    return new DictionaryCursorLoader(ac, uri, null, null, null, null);
                }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (this.cursor == null){       //first time
            adapter.swapCursor(cursor);
            return;
        }else{
            this.cursor = cursor;
            list.setFilterText(actionSearch.getQuery().toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) { adapter.changeCursor( null ); }

    @Override public Cursor runQuery(CharSequence constraint) { return cursor; }

    @Override
    public boolean onQueryTextSubmit(String query) { return false; }

    @Override
    public boolean onQueryTextChange(String newText) {
        Bundle params = new Bundle();
        params.putString("filter", newText);
        getLoaderManager().restartLoader(URL_LOADER, params, this);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor item = (Cursor)adapter.getItem(position);
        String word = item.getString(item.getColumnIndex("word"));
        String definition = item.getString(item.getColumnIndex("definition"));
        ((MainApplication)getActivity().getApplication()).getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("translation")
                .setAction(tableName)
                .setLabel(word).build());
        new AlertDialog.Builder(getActivity())
                .setTitle(word)
                .setMessage(definition)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
