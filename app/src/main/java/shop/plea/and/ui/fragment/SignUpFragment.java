package shop.plea.and.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.ui.sns.SNSHelper;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class SignUpFragment extends BaseFragment{

    public static final String ARG_PAGE_NUM = "ARG_PAGE_NUM";
    private Listner mListner;
    private BootstrapButton btn_facebook;
    @BindView(R.id.facebook_login_btn) com.facebook.login.widget.LoginButton facebook;
    private BootstrapButton btn_google;
    private SNSHelper helper;


    public static SignUpFragment newInstance(int page)
    {
        SignUpFragment signUpFragment = new SignUpFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUM, page);
        signUpFragment.setArguments(bundle);

        return signUpFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(mView == null)
        {
            mListner = new Listner();
            mView = inflater.inflate(R.layout.fragment_signup, container, false);
            init(container);
            initScreen();
        }

        return mView;
    }

    public void initScreen()
    {
        btn_facebook = (BootstrapButton) mView.findViewById(R.id.login_facebook);
        btn_google = (BootstrapButton) mView.findViewById(R.id.login_google);

        btn_facebook.setOnClickListener(mListner);
        btn_google.setOnClickListener(mListner);

        helper = new SNSHelper((BaseActivity) getActivity(), facebook);
    }

    private class Listner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.login_facebook :
                    UserInfo.getInstance().clearParams();
                    helper.facebookLogin();
                    break;
                case R.id.login_google :
                    UserInfo.getInstance().clearParams();
                    helper.googleLogin();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.removeSNSCallback();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        helper.onActivityResult(requestCode, resultCode, data);
    }
}
