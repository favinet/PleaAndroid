package shop.plea.and.ui.activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopIndicator();
                finish();
                IntentData indata = new IntentData();
                indata.aniType = Constants.VIEW_ANIMATION.ANI_FLIP;
                Intent intent = new Intent(getApplicationContext(), TransFormerActivity.class);
                startActivity(intent);

            }
        }, 3000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        stopIndicator();
        super.onPause();
    }

    @Override
    public void onResume() {
        startIndicator("");
        super.onResume();
    }
}
