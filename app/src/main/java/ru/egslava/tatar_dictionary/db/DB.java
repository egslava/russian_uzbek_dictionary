package ru.egslava.tatar_dictionary.db;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;

/**
 * Init DB in background, manages ProgressDialog and runs callback
 * Created by egslava on 01/04/15.
 */
@EBean
public class DB {

    @Background void loadDb(){
        db().getWritableDatabase(); // slow op
        cancelProgressDialog();
        dbLoaded = true;
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void cancelProgressDialog() {   progressDialog.cancel();    }

    private ProgressDialog progressDialog;

    @StringRes  String  please_wait, first_time_load;

    @RootContext        Context     context;
                        boolean     dbLoaded;

    Runnable        onDbReadyListener;
    public void setOnDbReadyListener( Runnable callback ) {
        if (dbLoaded) callback.run();
        onDbReadyListener = callback;
    }

    @UiThread(propagation = UiThread.Propagation.REUSE) void onDbReady(){
        if (onDbReadyListener == null) return;
        onDbReadyListener.run();
    }

    @AfterViews void init() {
        progressDialog = ProgressDialog.show(context, please_wait, first_time_load, true, false);
        loadDb();
    }

    private DBHelper db;
    public  DBHelper db(){
        if (db == null){
            db = new DBHelper(context);
        }
        return db;
    }

    public SQLiteDatabase getReadableDatabase() {
        return db.getReadableDatabase();
    }
}
