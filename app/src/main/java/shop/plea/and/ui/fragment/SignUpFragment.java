package shop.plea.and.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.ui.sns.SNSHelper;

/**
 * Created by kwon7575 on 2017-10-17.
 */

public class SignUpFragment extends BaseFragment{

    public static final String ARG_PAGE_NUM = "ARG_PAGE_NUM";
    private Listner mListner;
    @BindView(R.id.facebook_login_btn) com.facebook.login.widget.LoginButton facebook;
    @BindView(R.id.login_facebook) Button btn_facebook;
    @BindView(R.id.login_google) Button btn_google;
    @BindView(R.id.login_email) Button btn__email;
    @BindView(R.id.txt_login)TextView txt_login;


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
            setTextSpan();
        }

        return mView;
    }

    public void initScreen()
    {

        btn_facebook.setOnClickListener(mListner);
        btn_google.setOnClickListener(mListner);
        btn__email.setOnClickListener(mListner);

        helper = new SNSHelper((BaseActivity) getActivity(), facebook);
    }

    private void setTextSpan()
    {
        Spannable spannable = (Spannable) txt_login.getText();
        String text = spannable.toString();

        int start = text.indexOf(txt_login.getText().toString());
        int end = start + txt_login.getText().toString().length();

        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                mUpdateListenerCallBack.addFragment(Constants.FRAGMENT_MENUID.LOGIN);
            }

        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_login.setText(spannable);
        txt_login.setTextColor(Color.WHITE);
        txt_login.setMovementMethod(new LinkMovementMethod());
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
                case R.id.login_email :
                    UserInfo.getInstance().clearParams();

                    EmailSignUpFragment emailSignUpFragment = new EmailSignUpFragment().newInstance(Constants.FRAGMENT_MENUID.SIGN_EMAIL);
                    mUpdateListenerCallBack.addFragment(emailSignUpFragment, Constants.FRAGMENT_MENUID.SIGN_EMAIL);

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
