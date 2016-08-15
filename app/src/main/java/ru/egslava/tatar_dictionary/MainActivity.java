package ru.egslava.tatar_dictionary;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import ru.egslava.tatar_dictionary.R;

public class MainActivity extends AppCompatActivity {

    DBHelper                db;
    private ProgressDialog progressDialog;

    public DBHelper db(){
        if (db == null){
            db = new DBHelper(this);
        }
        return db;
    }

    // ---> injects...
    PagerSlidingTabStrip    tabs;
    ViewPager               pager;

    String[]    dicts;
    // <--- injects

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // res
        dicts               = getResources().getStringArray(R.array.dicts);

        setContentView(R.layout.activity_main);

        FrameLayout     ad          = (FrameLayout)findViewById(R.id.ad);
        AdView          adView      = (AdView) findViewById(R.id.adView);
                        tabs        = (PagerSlidingTabStrip)findViewById(R.id.tabs);
                        pager       = (ViewPager)findViewById(R.id.pager);

        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), getString(R.string.first_time_load), true, false);
        adView.loadAd(new AdRequest.Builder().build());
        loadDb();
    }

    void loadDb(){
        new AsyncTask<Void, Void, Void>(){
            @Override protected Void doInBackground(Void... params) {
                db().getReadableDatabase();
                return null;
            }

            @Override protected void onPostExecute(Void aVoid) {
                initPager();
            }
        }.execute();
    }

    void initPager(){
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override public int getCount() { return 2; }
            @Override public Fragment getItem(int i) {
                switch(i){
                    case 0: return DictionaryFragment.newInstance("from_rus", null);
                    case 1: return DictionaryFragment.newInstance("to_rus", getResources().getStringArray(R.array.letters) );
                }
                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return dicts[position];
            }
        });
        tabs.setViewPager(pager);

        progressDialog.cancel();
    }
}
