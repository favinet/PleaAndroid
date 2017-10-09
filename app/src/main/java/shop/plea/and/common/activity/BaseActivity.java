package shop.plea.and.common.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.List;

import shop.plea.and.R;
import shop.plea.and.common.dialog.ProgressDialog;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.parcel.IntentData;

/**
 * Created by shimtaewoo on 2017-10-02.
 */

public class BaseActivity extends AppCompatActivity {

    //private Dialog materialDg;
    //private ProgressWheel progressWheel;
    private ProgressDialog progressDlg;
    private long backKeyPressedTime;

    protected Activity context;

    public IntentData inData = new IntentData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        // Intent
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.getExtras().containsKey(Constants.INTENT_DATA_KEY)) {
            inData = intent.getParcelableExtra(Constants.INTENT_DATA_KEY);
            //Logger.log(Logger.LogState.D, intent.getParcelableExtra(Constants.intentTitle).toString());
        }

        progressDlg = new ProgressDialog(this, R.style.Theme_CustomProgressDialog);
        progressDlg.setContentView(R.layout.progress_dialog_material);

        startTransition();
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
}
