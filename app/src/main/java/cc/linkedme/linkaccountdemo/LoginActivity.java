package cc.linkedme.linkaccountdemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cc.lkme.linkaccount.LinkAccount;
import cc.lkme.linkaccount.callback.AbilityType;
import cc.lkme.linkaccount.callback.TokenResult;
import cc.lkme.linkaccount.callback.TokenResultListener;

public class LoginActivity extends AppCompatActivity {

    private static final int REQ_READ_PHONE_STATE = 10001;

    private Button login, portraitActivity, portraitDialog, landscapeActivity, landscapeDialog;
    private EditText phone;
    private String token;
    private String authCode;
    private String platform;
    private String operator;
    private boolean accessCodeSucceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.login);
        portraitActivity = findViewById(R.id.portrait_activity);
        portraitDialog = findViewById(R.id.portrait_dialog);
        landscapeActivity = findViewById(R.id.landscape_activity);
        landscapeDialog = findViewById(R.id.landscape_dialog);
        phone = findViewById(R.id.phone);
        // ************先初始化LinkAccount监听，再调用预登录接口*************
        LinkAccount.getInstance().setTokenResultListener(new TokenResultListener() {
            @Override
            public void onSuccess(@AbilityType final int resultType, final TokenResult tokenResult, final String originResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cbm.setPrimaryClip(ClipData.newPlainText("tokenResult", tokenResult.toString()));
                        Toast.makeText(LoginActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                        switch (resultType) {
                            case AbilityType.ABILITY_ACCESS_CODE:
                                accessCodeSucceed = false;
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
                    }
                });

            }
        });

        // 预登录
        LinkAccount.getInstance().preLogin(5000);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (accessCodeSucceed) {
                    LinkAccount.getInstance().getMobileCode(5000);
                    accessCodeSucceed = false;
                } else {
                    Toast.makeText(LoginActivity.this, "请调用预取号接口！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        portraitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkAccount.getInstance().setAuthUIConfig(LinkAccountAuthUIUtil.getPortraitActivity(LoginActivity.this));
                startTransferActivity(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
        portraitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkAccount.getInstance().setAuthUIConfig(LinkAccountAuthUIUtil.getPortraitDialog(LoginActivity.this));
                startTransferActivity(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
        landscapeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkAccount.getInstance().setAuthUIConfig(LinkAccountAuthUIUtil.getLandscapeActivity(LoginActivity.this));
                startTransferActivity(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });
        landscapeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkAccount.getInstance().setAuthUIConfig(LinkAccountAuthUIUtil.getLandscapeDialog(LoginActivity.this));
                startTransferActivity(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });
    }

    private void startTransferActivity(int orientation) {
        Intent intent = new Intent(LoginActivity.this, TransferActivity.class);
        intent.putExtra("orientation", orientation);
        startActivity(intent);
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

}
