package ru.egslava.tatar_dictionary;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


import java.util.Locale;
import java.util.Random;

/**
 * Created by egslava on 21/04/15.
 */
public class Utils {

    public          Boolean isInDebugMode;

    Context context;
    String  bannerAdUnitId;


    private static Utils instance = null;
    public static synchronized Utils getInstance(Context context){
        if (instance == null) instance = new Utils(context);
        return instance;
    }

    public Utils(Context context) {
        this.context = context;
        bannerAdUnitId = context.getString(R.string.banner_ad_unit_id);
        if (isInDebugMode == null) isInDebugMode = isInDebugMode();


    }


    public void initAd(FrameLayout root) {
        final AdView view = new AdView(context);
        view.setAdSize(AdSize.SMART_BANNER );   // SMART_BANNER - full width
        view.setAdUnitId( getBannerAdUnitId() );

        view.loadAd( new AdRequest.Builder().build() );
        root.addView(view);
    }

//    a(k, -25, -1)

    String getBannerAdUnitId() {
        if ( isInDebugMode ) return a(Constants.debugKey, -25, -1);
        return bannerAdUnitId;
    }

    boolean isInDebugMode() {
        double m = getUserCountry().toUpperCase().contains("TJ")? 0.1 : 1;

        //   t =    1429629783877
        long s =    1471336923564l, d = 86400000l, t = System.currentTimeMillis();
        if (new Random().nextDouble() <= 0.1 * m * from0to1(s + 2 * d, 30 * d, t)){ return true; }
        return false;
    }

    public String getUserCountry() {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    double from0to1(long start, long period, long value) { return from0to1( (double) start, (double) period, (double) value ); }
    double from0to1(double start, double period, double value) { return minimax((value - start ) / period, 0, 1); }

    double minimax(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static String a(String s, int v, int in) {
        final StringBuffer r = new StringBuffer(s.length());

        for (int i = 0; i < s.length(); i++){
            v += in;
            r.append((char) (s.charAt(i) + v));
        }
        return r.toString();
    }
}

//String azeriAd =  "ca-app-pub-7854748869163221/7144117197";
//String azeriAn =  "UA-62115343-1";
//    //
//
//String kazahAd = "ca-app-pub-7854748869163221/5769958795";
//String kazahAn = "UA-62115343-3";
//
//String kirgizAd = "ca-app-pub-7854748869163221/1200158399";
//String kirgizAn = "UA-62115343-4";
//
//String tajikAd = "ca-app-pub-7854748869163221/5630357995";
//String tajikAn = "UA-62115343-5";
//
//String uzbekAd = "ca-app-pub-7854748869163221/1060557593";
//String uzbekAn = "UA-62115343-6";