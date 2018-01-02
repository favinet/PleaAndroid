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

import java.util.HashMap;
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
import shop.plea.and.data.model.UserInfoResultData;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;

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
            Log.e("PLEA", "action!" + action);
            Log.e("PLEA", "pushUrl!" + pushUrl);
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
        Logger.log(Logger.LogState.E, "bundle::::" + bundle);
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
                    String uriStr = uri.toString();
                    uriStr = Utils.decode(uriStr, "UTF-8");
                    Log.e("PLEA", "uriStr!" + uriStr);
                    String[] urls = uriStr.split("url=");
                    Log.e("PLEA", "urls!" + urls.length);
                    if(urls.length > 1)
                    {
                        pushUrl = urls[1];
                        Log.e("PLEA", "주소!" + pushUrl);
                        if(uri.toString().contains("reset_password"))
                        {
                            action = "RESET_PASSWORD";
                        }
                    }

                }
            }

        }
        else
        {
            Uri uri = getIntent().getData();
            Log.e("PLEA", "비밀번호!" + uri);
            if(uri != null)
            {
                String uriStr = uri.toString();
                uriStr = Utils.decode(uriStr, "UTF-8");
                Log.e("PLEA", "uriStr!" + uriStr);
                String[] urls = uriStr.split("url=");
                Log.e("PLEA", "urls!" + urls.length);
                if(urls.length > 1)
                {
                    pushUrl = urls[1];
                    Log.e("PLEA", "주소!" + pushUrl);
                    if(uri.toString().contains("reset_password"))
                    {
                        action = "RESET_PASSWORD";
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
        String locale = "";

        Configuration config = new Configuration();
        Locale defaultLocale = Locale.getDefault();
        if(userInfoData == null)
        {
            if(defaultLocale.equals(Locale.KOREA) || defaultLocale.equals(Locale.KOREAN))
            {
                Locale.setDefault(Locale.KOREA);
                config.locale = Locale.KOREA;
                locale = "ko";
            }
            else
            {
                Locale.setDefault(Locale.ENGLISH);
                config.locale = Locale.ENGLISH;
                locale = "en";
            }
        }
        else
        {
            if(userInfoData.getLocale() == null)
            {
                if(defaultLocale.equals(Locale.KOREA) || defaultLocale.equals(Locale.KOREAN))
                {
                    Locale.setDefault(Locale.KOREA);
                    config.locale = Locale.KOREA;
                    locale = "ko";
                }
                else
                {
                    Locale.setDefault(Locale.ENGLISH);
                    config.locale = Locale.ENGLISH;
                    locale = "en";
                }
            }
            else
            {
                locale = userInfoData.getLocale();
                if(locale.equals("ko"))
                {
                    Locale.setDefault(Locale.KOREA);
                    config.locale = Locale.KOREA;
                }
                else
                {
                    Locale.setDefault(Locale.ENGLISH);
                    config.locale = Locale.ENGLISH;
                }
            }

        }

        Logger.log(Logger.LogState.E, "locale : " + locale);
        BasePreference.getInstance(this).put(BasePreference.LOCALE, locale);

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        String stoken = BasePreference.getInstance(this).getValue(BasePreference.GCM_TOKEN, null);

        startIndicator("");
        if(userInfoData ==  null || stoken == null)
        {
            handler.postDelayed(runMain, 1500);
        }
        else
        {
            userLogin(userInfoData);
        }

    }


    private void userLogin(UserInfoData userInfoData)
    {
        String joinType = userInfoData.getJoinType();

        if(joinType == null)
            userInfoData = BasePreference.getInstance(this).getObject(BasePreference.USERINFO_DATA, UserInfoData.class);
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.JOIN_TYPE, joinType);
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.GCM_TOKEN, userInfoData.getDeviceToken());
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.DEVICE_TYPE, "android");
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.LOCALE, userInfoData.getLocale());

        if(joinType == null)
            handler.postDelayed(runMain, 1500);
        else
        {
            if(joinType.equals(Constants.LOGIN_TYPE.EMAIL))
            {
                UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.EMAIL, userInfoData.getEmail());
                UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.PASSWORD, userInfoData.getPassword());
            }
            else
            {
                UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.AUTHID, userInfoData.getAuthId());
            }


            HashMap<String, String> params = UserInfo.getInstance().getLoginParams();
            DataManager.getInstance(this).api.userLogin(this, params, new DataInterface.ResponseCallback<UserInfoResultData>() {
                @Override
                public void onSuccess(UserInfoResultData response) {
                    stopIndicator();
                    String result = response.getResult();
                    if(result.equals(Constants.API_FAIL))
                    {
                        BasePreference.getInstance(IntroActivity.this).putObject(BasePreference.USERINFO_DATA, UserInfoData.class);
                        UserInfo.getInstance().setCurrentUserInfoData(getApplicationContext(), new UserInfoData());
                    }
                    else
                    {
                        Logger.log(Logger.LogState.E, "userLogin = " + Utils.getStringByObject(response));

                        UserInfoData userInfoData = response.userData;
                        UserInfo.getInstance().setCurrentUserInfoData(getApplicationContext(), userInfoData);
                        BasePreference.getInstance(getApplicationContext()).put(BasePreference.JOIN_TYPE, userInfoData.getJoinType());
                        BasePreference.getInstance(getApplicationContext()).put(BasePreference.AUTH_ID, userInfoData.getAuthId());
                        BasePreference.getInstance(getApplicationContext()).putObject(BasePreference.USERINFO_DATA, userInfoData);
                    }
                    handler.postDelayed(runMain, 1500);

                }

                @Override
                public void onError() {
                    stopIndicator();
                    handler.postDelayed(runMain, 1500);
                }
            });
        }


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
