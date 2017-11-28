package shop.plea.and.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import shop.plea.and.R;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.PermissionHelper;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.data.parcel.IntentData;

/**
 * Created by shimtaewoo on 2017-10-02.
 */

public class IntroActivity extends PleaActivity {

    Handler handler = new Handler();
    Runnable runMain = new Runnable() {
        @Override
        public void run() {
            stopIndicator();
            IntentData indata = new IntentData();
            indata.aniType = Constants.VIEW_ANIMATION.ANI_FLIP;
            indata.link = Constants.BASE_URL;
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra(Constants.INTENT_DATA_KEY, indata);
            startActivity(intent);
            finish();
        }
    };

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_intro);

        this.context = this;

        if(getIntent() != null)
        {
            Uri uri = getIntent().getData();
            if(uri != null)
            {
                String action = uri.toString();
                if(action.contains("reset_password"))
                {
                    Logger.log(Logger.LogState.E, "::::" + Utils.getStringByObject(uri));
                    Toast.makeText(this, "비밀번호 변경 페이지 호출", Toast.LENGTH_SHORT).show();
                }
            }
        }

        // 네트워크 상태체크
        int networkStatus = Utils.getNetWorkType(context);

        if (networkStatus == Utils.NETWORK_NO) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(getString(R.string.app_name)).setMessage("네트워크 상태를 확인해 주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            }).create().show();

            return;
        }

        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            PermissionHelper.getInstance().setPermissionAndActivity(new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, (Activity) context);

            if(!PermissionHelper.getInstance().checkPermission()) {
                PermissionHelper.getInstance().requestPermission(0, new PermissionHelper.PermissionCallback() {
                    @Override
                    public void onPermissionResult(String[] permissions, int[] grantResults) {
                        int size = permissions.length;

                        if(size > 0 && permissions[0].equals(Manifest.permission.READ_PHONE_STATE))
                        {
                            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
                            {
                                System.exit(0);
                                return;
                            }
                            else
                            {
                                start();
                            }
                        }
                        else
                        {
                            start();
                        }
                    }
                });
            }
            else
            {
                start();
            }
        }
        else
        {
            start();
        }
    }

    private void start() {
        UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(this);
        Configuration config = new Configuration();
        Locale.setDefault(Locale.ENGLISH);
        if (userInfoData.getLocale() == null)
            config.locale = Locale.ENGLISH;
        else
        {
            if(userInfoData.getLocale().equals("en"))
                config.locale = Locale.ENGLISH;
            else
            {
                Locale.setDefault(Locale.KOREA);
                config.locale = Locale.KOREA;
            }
        }

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        handler.postDelayed(runMain, 3000);
        startIndicator("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        stopIndicator();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(runMain);
        stopIndicator();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
