package shop.plea.and.common.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import shop.plea.and.R;
import shop.plea.and.common.dialog.ProgressDialog;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.data.tool.LocaleChage;
import shop.plea.and.ui.fragment.LoginFragment;
import shop.plea.and.ui.fragment.ResetPasswordFragment;
import shop.plea.and.ui.fragment.SignUpFragment;
import shop.plea.and.ui.fragment.SignUpInfoFragment;
import shop.plea.and.ui.listener.UpdateListener;

/**
 * Created by shimtaewoo on 2017-10-02.
 */

public class BaseActivity extends AppCompatActivity implements UpdateListener{

    private static volatile Activity mCurrentActivity = null;
    private ProgressDialog progressDlg;
    private long backKeyPressedTime;
    protected Activity context;
    public IntentData inData = new IntentData();
    protected Fragment curFragment;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment signUpFragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(Constants.FRAGMENT_MENUID.SINGUP));
        Fragment signUpInfoFragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(Constants.FRAGMENT_MENUID.SIGN_INFO));
        Fragment loginFragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(Constants.FRAGMENT_MENUID.LOGIN));

        if (signUpFragment != null) {
            ((SignUpFragment) signUpFragment).onActivityResult(requestCode, resultCode, data);
        }
        if (signUpInfoFragment != null) {
            ((SignUpInfoFragment) signUpInfoFragment).onActivityResult(requestCode, resultCode, data);
        }
        if (loginFragment != null) {
            ((LoginFragment) loginFragment).onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        // 세로만 지원
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Intent
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().containsKey(Constants.INTENT_DATA_KEY)) {
            inData = intent.getParcelableExtra(Constants.INTENT_DATA_KEY);
        }

        progressDlg = new ProgressDialog(this, R.style.Theme_CustomProgressDialog);
        progressDlg.setContentView(R.layout.progress_dialog_material);

        /*
        ImageView gif_loading = (ImageView)findViewById(R.id.gif_loading);
        Glide.with(context)
                .load("http://www.favinet.co.kr/download/loading.gif")
                .into(gif_loading);
                */

        startTransition();
   }

    public void setLocale(String locale)
    {


        Logger.log(Logger.LogState.E, "BASE = " + Utils.getStringByObject(locale));
        BasePreference.getInstance(this).put(BasePreference.LOCALE, locale);

      //  getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());


       // LocaleChage.wrap(this, locale);

        /*
        Locale localeOri = (locale.equals("en")) ? Locale.ENGLISH : Locale.KOREA;
        Logger.log(Logger.LogState.E, "BASE = " + Utils.getStringByObject(localeOri.getLanguage()));

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(localeOri);
            getApplicationContext().createConfigurationContext(configuration);
        }
        else{
            configuration.locale = localeOri;
            resources.updateConfiguration(configuration,displayMetrics);
        }
        */

    }

    public boolean backPressed()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if(count > 0)
        {
            fragmentManager.popBackStack();
            return true;
        }
        else
            return false;
    }

    @Override
    public void finish() {
        super.finish();

        endTransition();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void startIndicator(final String msg) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            if(!progressDlg.isShowing() && !isFinishing())
                {
                    if (msg.length() > 0) {
//						message.setText(msg);
                        progressDlg.setCancelable(false);
                    } else {
//						message.setText("");
                        progressDlg.setCancelable(true);
                    }
                    progressDlg.show();
                }
            }
        });
    }

    public void stopIndicator() {

        if(!isFinishing() && progressDlg != null && progressDlg.isShowing())
        {
            progressDlg.dismiss();
        }
    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        mCurrentActivity = currentActivity;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityManager actM = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> listm = actM.getRunningTasks(1);
            int iNumActivity = listm.get(0).numActivities;

            if (iNumActivity == 1) {
                if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
                    backKeyPressedTime = System.currentTimeMillis();
                    Toast.makeText(this, getString(R.string.app_end), Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }

                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    protected void startTransition() {
        switch (inData.aniType) {
            case Constants.VIEW_ANIMATION.ANI_END_ENTER:
                this.overridePendingTransition(R.anim.end_enter, R.anim.hold);
                break;
            case Constants.VIEW_ANIMATION.ANI_FADE:
                this.overridePendingTransition(R.anim.fadein, R.anim.hold);
                break;
            case Constants.VIEW_ANIMATION.ANI_FLIP:
                this.overridePendingTransition(R.anim.slide_left_in, R.anim.fadeout);
                break;
            case Constants.VIEW_ANIMATION.ANI_SLIDE_DOWN_IN:
                this.overridePendingTransition(R.anim.slide_up_in, R.anim.hold);
                break;
            case Constants.VIEW_ANIMATION.ANI_SLIDE_UP_IN:
                this.overridePendingTransition(R.anim.slide_down_in, R.anim.hold);
                break;

            case Constants.VIEW_ANIMATION.ANI_SLIDE_LEFT_IN:
                this.overridePendingTransition(R.anim.slide_left_in, R.anim.hold);
                break;

            case Constants.VIEW_ANIMATION.ANI_SLIDE_RIGHT_IN:
                this.overridePendingTransition(R.anim.slide_right_in, R.anim.fadeout);
                break;
        }
    }
    protected void endTransition() {
        switch (inData.aniType) {
            case Constants.VIEW_ANIMATION.ANI_END_ENTER:
                this.overridePendingTransition(R.anim.hold, R.anim.fadeout);
                break;
            case Constants.VIEW_ANIMATION.ANI_FADE:
                this.overridePendingTransition(R.anim.fadeout, R.anim.hold);
                break;
            case Constants.VIEW_ANIMATION.ANI_FLIP:
                this.overridePendingTransition(R.anim.fadein, R.anim.slide_right_out);
                break;

            case Constants.VIEW_ANIMATION.ANI_SLIDE_DOWN_IN:
                this.overridePendingTransition(R.anim.slide_down_out, R.anim.slide_down_out);
                break;

            case Constants.VIEW_ANIMATION.ANI_SLIDE_LEFT_IN:
                this.overridePendingTransition(R.anim.hold, R.anim.slide_right_out);
                break;

            case Constants.VIEW_ANIMATION.ANI_SLIDE_RIGHT_IN:
                this.overridePendingTransition(R.anim.fadein, R.anim.slide_left_out);
                break;
        }
    }

    // font type factofy
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    protected static Typeface getCacheAsset(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    //Logger.log(Logger.LogState.E, "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }

    public void openDrawer()
    {

    }

    public void closeDrawer()
    {

    }

    @Override
    public void addFragment(int menuId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(menuId == Constants.FRAGMENT_MENUID.SINGUP)
        {
            curFragment = SignUpFragment.newInstance(menuId);
            fragmentTransaction.replace(R.id.content, curFragment, String.valueOf(menuId));
        }
        else if(menuId == Constants.FRAGMENT_MENUID.LOGIN)
        {
            curFragment = LoginFragment.newInstance(menuId);
            fragmentTransaction.replace(R.id.content, curFragment, String.valueOf(menuId));
        }
        else if(menuId == Constants.FRAGMENT_MENUID.RESET_PASSWORD)
        {
            curFragment = ResetPasswordFragment.newInstance(menuId);
            fragmentTransaction.replace(R.id.content, curFragment, String.valueOf(menuId));
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void fragmentBackPressed() {
        backPressed();
    }

    @Override
    public void addFragment(Fragment fragment, int menuid) {
        curFragment = fragment;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations()
        fragmentTransaction.add(R.id.content, fragment, String.valueOf(menuid));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearReferences();
    }

    private void clearReferences() {
        Activity currActivity = getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            setCurrentActivity(null);
        }
    }
}
