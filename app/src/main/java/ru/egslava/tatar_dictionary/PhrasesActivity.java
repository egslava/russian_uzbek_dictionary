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
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

import ru.egslava.tatar_dictionary.db.DB;
import ru.egslava.tatar_dictionary.db.DBHelper;

@EActivity(R.layout.activity_main)
public class PhrasesActivity extends ActionBarActivity {

    @ViewById   PagerSlidingTabStrip    tabs;
    @ViewById   FrameLayout             ad;
    @ViewById   AdView                  adView;
    @ViewById   ViewPager               pager;

    @StringArrayRes     String[] dicts;

    @Bean       DB                      db;

    @AfterViews void init(){
        adView.loadAd(new AdRequest.Builder().build());
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void initPager(){
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override public int getCount() { return 2; }
            @Override public Fragment getItem(int i) {
                switch(i){
                    case 0: return DictionaryFragment_.builder()
                            .languageName("uz")
                            .build();
                    case 1: return DictionaryFragment_.builder()
                            .languageName("uz")
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
    }
}
