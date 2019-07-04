package cc.linkedme.linkaccountdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_READ_PHONE_STATE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LinkAccount.getInstance().preLogin(5000);
            } else {
                Toast.makeText(this, "无法使用一键登录及号码认证功能！", Toast.LENGTH_SHORT).show();
                login.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
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
                        hideLoadingDialog();
                        ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cbm.setPrimaryClip(ClipData.newPlainText("tokenResult", tokenResult.toString()));
                        Toast.makeText(LoginActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
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
                        hideLoadingDialog();
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
