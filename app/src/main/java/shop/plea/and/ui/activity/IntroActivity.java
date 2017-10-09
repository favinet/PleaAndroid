package shop.plea.and.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import shop.plea.and.R;
import shop.plea.and.data.config.Constants;
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
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            intent.putExtra(Constants.INTENT_DATA_KEY, indata);
            startActivity(intent);
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

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
        handler.postDelayed(runMain, 5000);
        startIndicator("");
        super.onResume();
    }
}
