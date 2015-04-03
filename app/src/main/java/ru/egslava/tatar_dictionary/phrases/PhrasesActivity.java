package ru.egslava.tatar_dictionary.phrases;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import ru.egslava.tatar_dictionary.ChooseLanguageActivity;
import ru.egslava.tatar_dictionary.R;
import ru.egslava.tatar_dictionary.db.DB;

@EActivity(R.layout.activity_main)
public class PhrasesActivity extends ActionBarActivity {

    @ViewById   PagerSlidingTabStrip    tabs;
    @ViewById   FrameLayout             ad;
    @ViewById   AdView                  adView;
    @ViewById   ViewPager               pager;

    @Extra      int                     themeId;
    @Extra      String                  languageName;

    @StringArrayRes String[]            directTranslation, reverseTranslation;

    @Bean       DB                      db;

    @AfterViews void init(){
        adView.loadAd(new AdRequest.Builder().build());
        initPager();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void initPager(){
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override public int getCount() { return 2; }
            @Override public Fragment getItem(int i) {
                switch(i){
                    case 0: return PhrasesFragment_.builder()
                            .languageName( languageName )
                            .themeId(themeId)
                            .build();
                    case 1: return PhrasesFragment_.builder()
                            .languageName( languageName )
                            .themeId(themeId)
                            .letters(new String[]{"ә", "җ", "ң", "ө", "ү", "h"})
                            .build();
                }
                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                int languageCode = ArrayUtils.indexOf(
                        ChooseLanguageActivity.languagesCodes,
                        languageName);

                switch (position){
                    case 0: return directTranslation[languageCode];
                    default: return reverseTranslation[languageCode];
                }
            }
        });
        tabs.setViewPager(pager);
    }
}
