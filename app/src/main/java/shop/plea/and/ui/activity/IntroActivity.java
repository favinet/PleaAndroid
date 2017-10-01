package shop.plea.and.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import shop.plea.and.R;

/**
 * Created by shimtaewoo on 2017-10-02.
 */

public class IntroActivity extends PleaActivity {

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
        super.onDestroy();
    }

}
