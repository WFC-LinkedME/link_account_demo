package cc.linkedme.linkaccountdemo;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import cc.lkme.linkaccount.LinkAccount;
import cc.lkme.linkaccount.callback.AbilityType;
import cc.lkme.linkaccount.callback.TokenResult;
import cc.lkme.linkaccount.callback.TokenResultListener;

public class TransferActivity extends AppCompatActivity {


    private String token;
    private String authCode;
    private String platform;
    private String operator;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        int orientation = getIntent().getIntExtra("orientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(orientation);
        initLinkAccount();
        LinkAccount.getInstance().getLoginToken(5000);
//        finish();
    }


    private void initLinkAccount() {
        LinkAccount.getInstance().setTokenResultListener(new TokenResultListener() {
            @Override
            public void onSuccess(@AbilityType final int resultType, final TokenResult tokenResult, final String originResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cbm.setPrimaryClip(ClipData.newPlainText("tokenResult", tokenResult.toString()));
                        Toast.makeText(TransferActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                        switch (resultType) {
                            case AbilityType.ABILITY_ACCESS_CODE:
                                Log.i("LinkAccountDemo", "preLogin tokenResult == " + tokenResult.toString());
                                break;
                            case AbilityType.ABILITY_TOKEN:
                                Log.i("LinkAccountDemo", "getLoginToken tokenResult == " + tokenResult.toString());
                                LinkAccount.getInstance().quitAuthActivity();
                                token = tokenResult.getAccessToken();
                                authCode = tokenResult.getGwAuth();
                                platform = tokenResult.getPlatform();
                                operator = getChannel(tokenResult.getOperatorType());
                                break;
                            case AbilityType.ABILITY_MOBILE_TOKEN:
                                Log.i("LinkAccountDemo", "getMobileToken tokenResult == " + tokenResult.toString());
                                token = tokenResult.getAccessToken();
                                authCode = tokenResult.getGwAuth();
                                platform = tokenResult.getPlatform();
                                operator = getChannel(tokenResult.getOperatorType());
                                break;
                        }
                    }
                });
            }

            @Override
            public void onFailed(final int resultType, final String info) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingDialog();
                        switch (resultType) {
                            case AbilityType.ABILITY_ACCESS_CODE:
                                Log.i("LinkAccountDemo", "preLogin failedResult == " + info);
                                break;
                            case AbilityType.ABILITY_TOKEN:
                                Log.i("LinkAccountDemo", "getLoginToken failedResult == " + info);
                                finish();
                                break;
                            case AbilityType.ABILITY_MOBILE_TOKEN:
                                Log.i("LinkAccountDemo", "getMobileToken failedResult == " + info);
                                break;
                            case AbilityType.ABILITY_NORMAL:
                                Log.i("LinkAccountDemo", "other error failedResult == " + info);
                                break;
                        }
                    }
                });

            }
        });
    }

    private String getChannel(String operatorType) {
        String channel = "0";
        switch (operatorType) {
            case "CT":
                channel = "1";
                break;
            case "CU":
                channel = "2";
                break;
        }
        return channel;

    }


    public void showLoadingDialog(String hint) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        mProgressDialog.setMessage(hint);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    public void hideLoadingDialog() {
        if (mProgressDialog != null)
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
    }

}
