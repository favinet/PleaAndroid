package shop.plea.and.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import shop.plea.and.R;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.data.config.Constants;
import shop.plea.and.ui.fragment.LoginFragment;
import shop.plea.and.ui.fragment.SignUpFragment;
import shop.plea.and.ui.fragment.SignUpInfoFragment;
import shop.plea.and.ui.listener.FragmentListener;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class LoginActivity extends PleaActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.log(Logger.LogState.E, "onActivityResult Main requestCode : " + requestCode);
        Logger.log(Logger.LogState.E, "onActivityResult Main resultCode: " + resultCode);
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
        setContentView(R.layout.activity_login);
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
