package cc.linkedme.linkaccountdemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cc.linkedme.linkaccountdemo.logger.LogFragment;
import cc.linkedme.linkaccountdemo.logger.LogWrapper;
import cc.linkedme.linkaccountdemo.logger.MessageOnlyLogFilter;
import cc.lkme.linkaccount.LinkAccount;
import cc.lkme.linkaccount.callback.AbilityType;
import cc.lkme.linkaccount.callback.TokenResult;
import cc.lkme.linkaccount.callback.TokenResultListener;

public class DeveloperActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DeveloperActivity.class.getSimpleName();
    private static final int REQ_READ_PHONE_STATE = 10001;
    private Button accessCodeBtn, login, mobile, loginWhite;
    private EditText user_mobile;
    private String token;
    private String authCode;
    private String platform;
    private String operator;
    private boolean accessCodeSucceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        initializeLogging();
        initView();
        initListener();
        initLinkAccount();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initLinkAccount() {
        // ***********先初始化LinkAccount监听，再调用预登录接口***********
        LinkAccount.getInstance().setTokenResultListener(new TokenResultListener() {
            @Override
            public void onSuccess(@AbilityType final int resultType, final TokenResult tokenResult, final String originResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cc.linkedme.linkaccountdemo.logger.Log.d(TAG, resultType + " --- " + tokenResult);
                        ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cbm.setPrimaryClip(ClipData.newPlainText("tokenResult", tokenResult.toString()));
                        Toast.makeText(DeveloperActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                        switch (resultType) {
                            case AbilityType.ABILITY_ACCESS_CODE:
                                accessCodeSucceed = true;
                                break;
                            case AbilityType.ABILITY_TOKEN:
                                LinkAccount.getInstance().quitAuthActivity();
                                token = tokenResult.getAccessToken();
                                authCode = tokenResult.getGwAuth();
                                platform = tokenResult.getPlatform();
                                operator = getChannel(tokenResult.getOperatorType());
                                break;
                            case AbilityType.ABILITY_MOBILE_TOKEN:
                                LinkAccount.getInstance().quitAuthActivity();
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
                        switch (resultType) {
                            case AbilityType.ABILITY_ACCESS_CODE:
                                accessCodeSucceed = false;
                                break;
                            case AbilityType.ABILITY_TOKEN:
                                break;
                            case AbilityType.ABILITY_MOBILE_TOKEN:
                                break;
                        }
                        cc.linkedme.linkaccountdemo.logger.Log.d(TAG, info);
                    }
                });

            }
        });
        LinkAccount.getInstance().setDebug(true);
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

    private void initView() {
        accessCodeBtn = findViewById(R.id.access_code);
        login = findViewById(R.id.login);
        mobile = findViewById(R.id.mobile);
        loginWhite = findViewById(R.id.login_white);
    }

    private void initListener() {
        accessCodeBtn.setOnClickListener(this);
        login.setOnClickListener(this);
        mobile.setOnClickListener(this);
        loginWhite.setOnClickListener(this);
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
                if (accessCodeSucceed) {
                    LinkAccount.getInstance().setAuthUIConfig(LinkAccountAuthUIUtil.getPortraitActivity(DeveloperActivity.this));
                    LinkAccount.getInstance().useDefaultAuthActivity(true);
                    LinkAccount.getInstance().getLoginToken(5000);
                    accessCodeSucceed = false;
                } else {
                    Toast.makeText(this, "请调用预取号接口！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mobile:
                // 号码认证
                if (accessCodeSucceed) {
                    LinkAccount.getInstance().getMobileCode(5000);
                    accessCodeSucceed = false;
                } else {
                    Toast.makeText(this, "请调用预取号接口！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_white:
                // 一键登录
                if (accessCodeSucceed) {
                    LinkAccount.getInstance().useDefaultAuthActivity(false);
                    LinkAccount.getInstance().getLoginToken(5000);
                    accessCodeSucceed = false;
                } else {
                    Toast.makeText(this, "请调用预取号接口！", Toast.LENGTH_SHORT).show();
                }
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
        cc.linkedme.linkaccountdemo.logger.Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log);
        msgFilter.setNext(logFragment.getLogView());

        cc.linkedme.linkaccountdemo.logger.Log.i(TAG, "Ready");
    }
}