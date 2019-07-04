package cc.linkedme.linkaccountdemo;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cc.linkedme.linkaccountdemo.logger.Log;
import cc.linkedme.linkaccountdemo.logger.LogFragment;
import cc.linkedme.linkaccountdemo.logger.LogWrapper;
import cc.linkedme.linkaccountdemo.logger.MessageOnlyLogFilter;
import cc.lkme.linkaccount.AuthUIConfig;
import cc.lkme.linkaccount.LinkAccount;
import cc.lkme.linkaccount.callback.AbilityType;
import cc.lkme.linkaccount.callback.TokenResult;
import cc.lkme.linkaccount.callback.TokenResultListener;

public class DeveloperActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DeveloperActivity.class.getSimpleName();
    private static final int REQ_READ_PHONE_STATE = 10001;
    private Button accessCodeBtn, login, mobile;

    private String token;
    private String authCode;
    private String platform;
    private String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        initializeLogging();
        initView();
        initListener();
        // 先初始化LinkAccount监听，再调用预登录接口
        initLinkAccount();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // 请求权限
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQ_READ_PHONE_STATE);
            } else {
                // 预登录
                LinkAccount.getInstance().preLogin(5000);
            }
        }
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
                        Toast.makeText(DeveloperActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
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
                                operator = tokenResult.getOperatorType();
                                break;
                            case AbilityType.ABILITY_MOBILE_TOKEN:
                                Log.i("LinkAccountDemo", "getMobileToken tokenResult == " + tokenResult.toString());
                                token = tokenResult.getAccessToken();
                                authCode = tokenResult.getGwAuth();
                                platform = tokenResult.getPlatform();
                                operator = tokenResult.getOperatorType();
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
                        switch (resultType) {
                            case AbilityType.ABILITY_ACCESS_CODE:
                                Log.i("LinkAccountDemo", "preLogin failedResult == " + info);
                                break;
                            case AbilityType.ABILITY_TOKEN:
                                Log.i("LinkAccountDemo", "getLoginToken failedResult == " + info);
                                break;
                            case AbilityType.ABILITY_MOBILE_TOKEN:
                                Log.i("LinkAccountDemo", "getMobileToken failedResult == " + info);
                                break;
                        }
                    }
                });

            }
        });
        AuthUIConfig.Builder builder = new AuthUIConfig.Builder();
        builder.setNavText("LinkAccount");
        builder.setCheckboxDrawable("linkaccount_check");
        builder.setSwitchClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DeveloperActivity.this, "switch", Toast.LENGTH_SHORT).show();
            }
        });
        LinkAccount.getInstance().setAuthUIConfig(builder.create());
    }

    private void initView() {
        accessCodeBtn = findViewById(R.id.access_code);
        login = findViewById(R.id.login);
        mobile = findViewById(R.id.mobile);
    }

    private void initListener() {
        accessCodeBtn.setOnClickListener(this);
        login.setOnClickListener(this);
        mobile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.access_code:
                // 预取号
                LinkAccount.getInstance().preLogin(5000);
                break;
            case R.id.login:
                // 一键登录
                LinkAccount.getInstance().getLoginToken(5000);
                break;
            case R.id.mobile:
                // 号码认证
                LinkAccount.getInstance().getMobileCode(5000);
                break;
            default:
                break;
        }
    }

    /**
     * Set up targets to receive log data
     */
    public void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}