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
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import shop.plea.and.R;
import shop.plea.and.common.preference.BasePreference;
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

    private String action;
    private String pushUrl;
    Handler handler = new Handler();
    Runnable runMain = new Runnable() {
        @Override
        public void run() {

            stopIndicator();
            IntentData indata = new IntentData();
            indata.aniType = Constants.VIEW_ANIMATION.ANI_FLIP;
            indata.link = Constants.BASE_URL;
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("action", action);
            intent.putExtra("pushUrl", pushUrl);
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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            action = bundle.getString("action");
            if(action != null)
            {
                if(action.equals("URL_PUSH"))
                {
                    pushUrl = Utils.decode(bundle.getString("url"), "UTF-8");
                    Logger.log(Logger.LogState.E, "pushUrl::::" + pushUrl);
                }
            }
            else
            {
                Uri uri = getIntent().getData();
                Log.e("PLEA", "비밀번호!" + uri);
                if(uri != null)
                {
                    pushUrl = Utils.queryToMap(Utils.decode(uri.toString(), "UTF-8")).get("url");
                    Log.e("PLEA", "주소!" + pushUrl);
                    if(uri.toString().contains("reset_password"))
                    {
                        action = "URL_PUSH";
                    }
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

        String token = FirebaseInstanceId.getInstance().getToken();
        Logger.log(Logger.LogState.E, "start token: " + token);
        BasePreference.getInstance(getApplicationContext()).put(BasePreference.GCM_TOKEN, token);

        UserInfoData userInfoData = BasePreference.getInstance(this).getObject(BasePreference.USERINFO_DATA, UserInfoData.class);
        String locale = (userInfoData == null) ? "en" : userInfoData.getLocale();

        Configuration config = new Configuration();
        Locale.setDefault(Locale.ENGLISH);
        config.locale = Locale.ENGLISH;

        Logger.log(Logger.LogState.E, "locale : " + locale);

        if(locale.equals("ko"))
        {
            Locale.setDefault(Locale.KOREA);
            config.locale = Locale.KOREA;
        }


        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        handler.postDelayed(runMain, 1500);
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
