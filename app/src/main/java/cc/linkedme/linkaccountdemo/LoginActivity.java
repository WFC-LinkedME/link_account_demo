package cc.linkedme.linkaccountdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cc.lkme.linkaccount.LinkAccount;

public class LoginActivity extends AppCompatActivity {

    private static final int REQ_READ_PHONE_STATE = 10001;

    private Button login, portraitActivity, portraitDialog, landscapeActivity, landscapeDialog;
    private EditText phone;


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
        // 先初始化LinkAccount监听，再调用预登录接口

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
                LinkAccount.getInstance().getMobileCode(5000);
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

}
