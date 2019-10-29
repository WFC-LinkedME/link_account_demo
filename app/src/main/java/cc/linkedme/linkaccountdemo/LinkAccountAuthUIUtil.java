package cc.linkedme.linkaccountdemo;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.lkme.linkaccount.AuthUIConfig;
import cc.lkme.linkaccount.v4.content.ContextCompat;

public class LinkAccountAuthUIUtil {

    public static AuthUIConfig getPortraitActivity(final Context context) {
        AuthUIConfig.Builder builder = new AuthUIConfig.Builder();

        builder.setStatusBarLight(true)
                .setBackgroundColor(R.color.red)
                .setBackgroundImgDrawable(ContextCompat.getDrawable(context, R.drawable.linkaccount_dialog_bg))
                .setNavText("LinkAccount")
                .setNavBackOffset(14, 14, null, null)

                .setLogoOffset(null, 10, null, null)

                .setNumberOffset(null, 116, null, null)
//                .setNumberColor(R.color.white)

                .setSloganOffset(null, 152, null, null)

                .setLogBtnWidth(250)
                .setLogBtnOffset(null, 184, null, null)
                .setLogBtnBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.linkaccount_login))

                .setSwitchOffset(null, 246, null, null)
                .setSwitchHidden(false)
//                .setSwitchOffsetY(10)
//                .setSwitchOffsetBottomY(10)
//                .setSwitchOffsetX(10)
//                .setSwitchOffsetRightX(10)

                .setAppPrivacyOne("LinkedME", "http://www.linkedme.cc")
                .setPrivacyOffset(16, 16, 16, 6)
                .setPrivacyDecorator("登录即同意", "和", "、", "");


        //其他方式登录
        TextView wechat = new TextView(context);
        wechat.setText("使用微信登录");
        RelativeLayout.LayoutParams wechatLP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        wechatLP.setMargins(0, AndroidUtils.convertDpToPixels(context, 276), 0, 0);
        wechatLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
        wechat.setLayoutParams(wechatLP);

        builder.addCustomView(wechat, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "onclick", Toast.LENGTH_SHORT).show();
            }
        });

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        progressBar.setBackgroundColor(context.getResources().getColor(R.color.black));
        builder.setLoadingView(progressBar);


        builder.setCheckboxDrawable("linkaccount_check");
        builder.setSwitchClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "switch", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }

    public static AuthUIConfig getPortraitDialog(final Context context) {
        AuthUIConfig.Builder builder = new AuthUIConfig.Builder();
        WindowManager manager = ((Activity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int dialogWidth = (int) (outMetrics.widthPixels * 0.8);
        int dialogHeight = (int) (outMetrics.heightPixels * 0.7);

        builder.setShowDialog(true)

                .setDialogSize(dialogWidth, dialogHeight)
//                .setDialogDimAmount(0.3f)

                .setStatusBarLight(true)
                .setBackgroundColor(R.color.red)
                .setBackgroundImgDrawable(ContextCompat.getDrawable(context, R.drawable.linkaccount_dialog_bg))
//                .setStatusBarColor(R.color.translucent)
                .setNavText("LinkAccount")
//                .setNavBackImgDrawable(getDrawable(R.drawable.linkaccount_close))
//                .setNavBackWidh(32)
//                .setNavBackHeight(32)
                .setNavBackOffset(null, 12, 12, null)
                .setNavHidden(true)

//                .setLogoHeight(96)
                .setLogoOffset(null, 10, null, null)

                .setNumberOffset(null, 116, null, null)
//                .setNumberColor(R.color.white)

                .setSloganOffset(null, 152, null, null)

                .setLogBtnWidth(250)
                .setLogBtnOffset(null, 184, null, null)
                .setLogBtnBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.linkaccount_login))

                .setSwitchOffset(null, 246, null, null)
                .setSwitchHidden(false)
//                .setSwitchOffsetY(10)
//                .setSwitchOffsetBottomY(10)
//                .setSwitchOffsetX(10)
//                .setSwitchOffsetRightX(10)

                .setAppPrivacyOne("LinkedME", "http://www.linkedme.cc")
                .setPrivacyOffset(16, 16, 16, 6)
                .setPrivacyDecorator("登录即同意", "和", "、", "");


        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        progressBar.setBackgroundColor(context.getResources().getColor(R.color.black));
        builder.setLoadingView(progressBar);


        builder.setCheckboxDrawable("linkaccount_check");
        builder.setSwitchClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "switch", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }

    public static AuthUIConfig getLandscapeActivity(final Context context) {
        AuthUIConfig.Builder builder = new AuthUIConfig.Builder();
        WindowManager manager = ((Activity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int dialogWidth = (int) (outMetrics.widthPixels * 0.8);
        int dialogHeight = (int) (outMetrics.heightPixels * 0.7);

        builder.setStatusBarLight(true)
                .setBackgroundColor(R.color.red)
                .setBackgroundImgDrawable(ContextCompat.getDrawable(context, R.drawable.linkaccount_dialog_bg))
//                .setStatusBarColor(R.color.translucent)
                .setNavText("LinkAccount")
//                .setNavBackImgDrawable(getDrawable(R.drawable.linkaccount_close))
//                .setNavBackWidh(32)
//                .setNavBackHeight(32)
                .setNavBackOffset(14, 14, null, null)
                .setNavHidden(false)

//                .setLogoHeight(96)
                .setLogoOffset(null, 10, null, null)

                .setNumberOffset(null, 80, null, null)
//                .setNumberColor(R.color.white)

                .setSloganOffset(null, 116, null, null)

                .setLogBtnWidth(250)
                .setLogBtnOffset(null, 146, null, null)
                .setLogBtnBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.linkaccount_login))

                .setSwitchOffset(null, 200, null, null)
                .setSwitchHidden(false)
//                .setSwitchOffsetY(10)
//                .setSwitchOffsetBottomY(10)
//                .setSwitchOffsetX(10)
//                .setSwitchOffsetRightX(10)

                .setAppPrivacyOne("LinkedME", "http://www.linkedme.cc")
                .setPrivacyOffset(16, 16, 16, 6)
                .setPrivacyDecorator("登录即同意", "和", "、", "");


        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        progressBar.setBackgroundColor(context.getResources().getColor(R.color.black));
        builder.setLoadingView(progressBar);


        builder.setCheckboxDrawable("linkaccount_check");
        builder.setSwitchClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "switch", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }

    public static AuthUIConfig getLandscapeDialog(final Context context) {
        AuthUIConfig.Builder builder = new AuthUIConfig.Builder();
        WindowManager manager = ((Activity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int dialogWidth = (int) (outMetrics.widthPixels * 0.8);
        int dialogHeight = (int) (outMetrics.heightPixels * 0.7);

        builder.setShowDialog(true)

                .setDialogSize(dialogHeight, dialogWidth)

                .setStatusBarLight(true)
                .setBackgroundColor(R.color.red)
                .setBackgroundImgDrawable(ContextCompat.getDrawable(context, R.drawable.linkaccount_dialog_bg))
//                .setStatusBarColor(R.color.translucent)
                .setNavText("LinkAccount")
//                .setNavBackImgDrawable(getDrawable(R.drawable.linkaccount_close))
//                .setNavBackWidh(32)
//                .setNavBackHeight(32)
                .setNavBackOffset(null, 12, 12, null)
                .setNavHidden(true)

//                .setLogoHeight(96)
                .setLogoOffset(null, 10, null, null)

                .setNumberOffset(null, 86, null, null)
//                .setNumberColor(R.color.white)

                .setSloganOffset(null, 122, null, null)

                .setLogBtnWidth(250)
                .setLogBtnOffset(null, 154, null, null)
                .setLogBtnBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.linkaccount_login))

                .setSwitchOffset(null, 216, null, null)
                .setSwitchHidden(false)
//                .setSwitchOffsetY(10)
//                .setSwitchOffsetBottomY(10)
//                .setSwitchOffsetX(10)
//                .setSwitchOffsetRightX(10)

                .setAppPrivacyOne("LinkedME", "http://www.linkedme.cc")
                .setPrivacyOffset(16, 16, 16, 6)
                .setPrivacyDecorator("登录即同意", "和", "、", "");


        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        progressBar.setBackgroundColor(context.getResources().getColor(R.color.black));
        builder.setLoadingView(progressBar);


        builder.setCheckboxDrawable("linkaccount_check");
        builder.setSwitchClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "switch", Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}
