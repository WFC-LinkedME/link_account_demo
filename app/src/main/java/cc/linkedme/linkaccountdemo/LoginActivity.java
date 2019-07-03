package cc.linkedme.linkaccountdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cc.lkme.linkaccount.AuthUIConfig;
import cc.lkme.linkaccount.LinkAccount;
import cc.lkme.linkaccount.callback.AbilityType;
import cc.lkme.linkaccount.callback.TokenResult;
import cc.lkme.linkaccount.callback.TokenResultListener;

public class LoginActivity extends AppCompatActivity {

    private static final int REQ_READ_PHONE_STATE = 10001;

    private boolean firstEnter = true;

    private Button login;
    private TextView register;
    private EditText phone;
    private ProgressDialog mProgressDialog;


    private String token;
    private String authCode;
    private String platform;
    private String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        phone = findViewById(R.id.phone);

        initLinkAccount();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, REQ_READ_PHONE_STATE);
            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoadingDialog("正在认证");
                // 号码认证
                LinkAccount.getInstance().getMobileCode(5000);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在一键登录");
                // 一键登录
                LinkAccount.getInstance().getLoginToken(5000);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstEnter) {
            LinkAccount.getInstance().preLogin(5000);
        }

    }

    private void initLinkAccount() {
        LinkAccount.getInstance().setTokenResultListener(new TokenResultListener() {
            @Override
            public void onSuccess(@AbilityType final int resultType, final TokenResult tokenResult, final String originResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("preLogin====" + tokenResult);
                        ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cbm.setPrimaryClip(ClipData.newPlainText("tokenResult", tokenResult.toString()));
                        Toast.makeText(LoginActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                        switch (resultType) {
                            case AbilityType.ABILITY_ACCESS_CODE:

                                break;
                            case AbilityType.ABILITY_TOKEN:
                                LinkAccount.getInstance().quitAuthActivity();
                                token = tokenResult.getAccessToken();
                                authCode = tokenResult.getGwAuth();
                                platform = tokenResult.getPlatform();
                                operator = tokenResult.getOperatorType();
                                break;
                            case AbilityType.ABILITY_MOBILE_TOKEN:
                                LinkAccount.getInstance().quitAuthActivity();
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
                        hideLoadingDialog();
                        switch (resultType) {
                            case AbilityType.ABILITY_ACCESS_CODE:
                                break;
                            case AbilityType.ABILITY_TOKEN:
                                if (info.contains("\"resultCode\":10011")) {
                                    LinkAccount.getInstance().preLogin(500);
                                }
                                break;
                            case AbilityType.ABILITY_MOBILE_TOKEN:
                                if (info.contains("\"resultCode\":10011")) {
                                    LinkAccount.getInstance().preLogin(500);
                                }
                                break;
                        }
                    }
                });

            }
        });
        // 预取号
        login.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinkAccount.getInstance().preLogin(5000);
                firstEnter = false;
            }
        }, 1000);
        AuthUIConfig.Builder builder = new AuthUIConfig.Builder();
        builder.setNavText("LinkAccount");
        builder.setSwitchClicker(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "switch", Toast.LENGTH_SHORT).show();
            }
        });
        LinkAccount.getInstance().setAuthUIConfig(builder.create());
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
