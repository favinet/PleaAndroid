package shop.plea.and.common.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Hashtable;
import java.util.List;

import shop.plea.and.R;
import shop.plea.and.common.dialog.ProgressDialog;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.ui.fragment.FindPasswordFragment;
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

        Logger.log(Logger.LogState.E, "onActivityResult Main requestCode : " + requestCode);
        Logger.log(Logger.LogState.E, "onActivityResult Main resultCode: " + resultCode);
        if(curFragment != null)
            Logger.log(Logger.LogState.E, "onActivityResult Main : " + curFragment.getTag());

        Fragment signUpFragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(Constants.FRAGMENT_MENUID.SINGUP));
        Fragment signUpInfoFragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(Constants.FRAGMENT_MENUID.SIGN_INFO));
        Fragment loginFragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(Constants.FRAGMENT_MENUID.LOGIN));

        Logger.log(Logger.LogState.E, "onActivityResult Main signUpFragment : " + signUpFragment);
        Logger.log(Logger.LogState.E, "onActivityResult Main signUpInfoFragment : " + signUpInfoFragment);
        Logger.log(Logger.LogState.E, "onActivityResult Main : loginFragment " + loginFragment);

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
            Logger.log(Logger.LogState.D, intent.getParcelableExtra(Constants.INTENT_DATA_KEY).toString());
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
        Logger.log(Logger.LogState.E, "BASE stopIndicator!");
        Logger.log(Logger.LogState.E, "BASE stopIndicator! isFinishing + " + isFinishing());
        Logger.log(Logger.LogState.E, "BASE stopIndicator! progressDlg + " + progressDlg);
        Logger.log(Logger.LogState.E, "BASE stopIndicator! progressDlg + " + progressDlg.isShowing());
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
        Logger.log(Logger.LogState.E, "BASE onKeyDown!");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityManager actM = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> listm = actM.getRunningTasks(1);
            int iNumActivity = listm.get(0).numActivities;

            if (iNumActivity == 1) {
                if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
                    backKeyPressedTime = System.currentTimeMillis();
                    Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
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
