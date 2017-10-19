package shop.plea.and.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import shop.plea.and.R;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.data.config.Constants;
import shop.plea.and.ui.fragment.SignUpFragment;
import shop.plea.and.ui.listener.FragmentListener;
import shop.plea.and.ui.listener.UpdateListener;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class SignUpActivity extends PleaActivity implements UpdateListener{


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.log(Logger.LogState.E, "onActivityResult Main" + requestCode);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(Constants.FRAGMENT_MENUID.SINGUP));

        if (fragment != null) {
            ((SignUpFragment) fragment).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        addFragment(Constants.FRAGMENT_MENUID.SINGUP);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            try {
                FragmentListener fragmentListener = (FragmentListener) curFragment;
                if(fragmentListener != null)
                {
                    boolean check = fragmentListener.onBackPressed();

                    if(check) return check;
                }
            }
            catch (Exception ex)
            {

            }

            boolean check = backPressed();
            if(check) return check;
        }

        return super.onKeyDown(keyCode, event);
    }


}
