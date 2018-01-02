package shop.plea.and.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.prefs.Preferences;

import shop.plea.and.R;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.ui.fragment.LoginFragment;
import shop.plea.and.ui.fragment.SignUpFragment;
import shop.plea.and.ui.fragment.SignUpInfoFragment;
import shop.plea.and.ui.listener.FragmentListener;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class LoginActivity extends PleaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String action = null;
        String pushUrl = null;

        Log.e("PLEA", "getIntent() : " + getIntent().getExtras());

        if(getIntent().getExtras() != null)
        {
            action = getIntent().getExtras().getString("action", null);
            pushUrl = getIntent().getExtras().getString("pushUrl", null);
        }

        String token = FirebaseInstanceId.getInstance().getToken();
        Logger.log(Logger.LogState.E, "start token: " + token);
        BasePreference.getInstance(getApplicationContext()).put(BasePreference.GCM_TOKEN, token);

        IntentData indata = new IntentData();
        UserInfoData userInfoData = BasePreference.getInstance(this).getObject(BasePreference.USERINFO_DATA, UserInfoData.class);
        if(userInfoData == null)
        {
            if(action != null && pushUrl != null)           //비번찾기
            {
                Intent intent = new Intent(this, InAppWebView.class);
                indata.link = pushUrl;
                indata.screenType = Constants.SCREEN_TYPE.PUSH;
                indata.title = getString(R.string.menu_reset_password);
                intent.putExtra("action", action);
                intent.putExtra("pushUrl", pushUrl);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(Constants.INTENT_DATA_KEY, indata);
                startActivity(intent);
                finish();
            }
            else
            {
                addFragment(Constants.FRAGMENT_MENUID.LOGIN);
            }
        }
        else                                                        //noti 통해서 오는 case
        {
            Intent intent = new Intent(this, MainPleaListActivity.class);
            indata.isRegist = false;
            if(action != null && pushUrl != null)
            {
                indata.link = pushUrl;
                intent.putExtra("action", action);
                intent.putExtra("pushUrl", pushUrl);
            }
            else
                indata.link = String.format(Constants.MAIN_URL, userInfoData.getId());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Constants.INTENT_DATA_KEY, indata);
            startActivity(intent);
            finish();
        }

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
