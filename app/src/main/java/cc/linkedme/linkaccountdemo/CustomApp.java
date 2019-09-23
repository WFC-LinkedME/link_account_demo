package cc.linkedme.linkaccountdemo;

import android.app.Application;

import cc.lkme.linkaccount.LinkAccount;


public class CustomApp extends Application {

    private String appKey = "7e289a2484f4368dbafbd1e5c7d06903";

    @Override
    public void onCreate() {
        super.onCreate();
        LinkAccount.getInstance(getApplicationContext(), appKey);
        if (BuildConfig.DEBUG) {
            LinkAccount.getInstance().setDebug(true);
        }
    }
}
