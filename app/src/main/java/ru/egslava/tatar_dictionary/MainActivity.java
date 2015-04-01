package ru.egslava.tatar_dictionary;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

import ru.egslava.tatar_dictionary.R;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

    @ViewById   PagerSlidingTabStrip    tabs;
    @ViewById   FrameLayout ad;
    @ViewById   AdView  adView;
    @ViewById   ViewPager               pager;

    private ProgressDialog progressDialog;

    private DBHelper                db;
    public DBHelper db(){
        if (db == null){
            db = new DBHelper(this);
        }
        return db;
    }

    @StringArrayRes     String[] dicts;

    @StringRes          String  please_wait, first_time_load;

    @AfterViews
    void init(){
        progressDialog = ProgressDialog.show(this, please_wait, first_time_load, true, false);
        adView.loadAd(new AdRequest.Builder().build());
        loadDb();
    }

    @Background void loadDb(){ db().getReadableDatabase(); initPager();}

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void initPager(){
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override public int getCount() { return 2; }
            @Override public Fragment getItem(int i) {
                switch(i){
                    case 0: return DictionaryFragment_.builder()
                            .tableName("rus_tatar")
                            .build();
                    case 1: return DictionaryFragment_.builder()
                            .tableName("tatar_rus")
                            .letters(new String[]{"ә", "җ", "ң", "ө", "ү", "h"})
                            .build();
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
