package ru.egslava.tatar_dictionary;

import android.app.Application;

//import com.google.analytics.tracking.android.GoogleAnalytics;
//import com.google.analytics.tracking.android.Tracker;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import ru.egslava.tatar_dictionary.R;

/**
 * Created by egslava on 20/12/14.
 */

public class MainApplication extends Application {

    Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        getTracker();
    }

//        tracker.send(new HitBuilders.ScreenViewBuilder().build());
//        tracker.send(new HitBuilders.AppViewBuilder().build());

    synchronized Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.app_tracker);
        }
        return tracker;
    }
}
