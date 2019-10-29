package cc.linkedme.linkaccountdemo;

import android.app.Application;

import cc.lkme.linkaccount.LinkAccount;


public class CustomApp extends Application {

    private String appKey = "";

    @Override
    public void onCreate() {
        super.onCreate();
        LinkAccount.getInstance(getApplicationContext(), appKey);
        if (BuildConfig.DEBUG) {
            LinkAccount.getInstance().setDebug(true);
        }
    }
}
