package ru.egslava.tatar_dictionary;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    DBHelper                db;
    private ProgressDialog progressDialog;
    private Utils utils;

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

        utils = Utils.getInstance(this);
        // res
        dicts               = getResources().getStringArray(R.array.dicts);

        setContentView(R.layout.activity_main);

        FrameLayout     ad          = (FrameLayout)findViewById(R.id.ad);
//        AdView          adView      = (AdView) findViewById(R.id.adView);
                        tabs        = (PagerSlidingTabStrip)findViewById(R.id.tabs);
                        pager       = (ViewPager)findViewById(R.id.pager);

        progressDialog = ProgressDialog.show(this, getString(R.string.please_wait), getString(R.string.first_time_load), true, false);

//        adView.loadAd(new AdRequest.Builder().build());
        utils.initAd(ad);
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
            @Override public int getCount() { return Constants.tables.length; }
            @Override public Fragment getItem(int i) {
                // only second tab has special letters
                return DictionaryFragment.newInstance(Constants.tables[i], i == 1? Constants.letters : null);
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
