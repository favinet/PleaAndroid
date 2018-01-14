package shop.plea.and.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapAlert;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.data.model.UserInfoResultData;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;
import shop.plea.and.ui.activity.MainPleaListActivity;
import shop.plea.and.ui.sns.SNSHelper;

/**
 * Created by kwon7575 on 2017-10-20.
 */

public class LoginFragment extends BaseFragment{

    public static final String ARG_PAGE_NUM = "ARG_PAGE_NUM";
    private LoginFragment.Listner mListner;
    @BindView(R.id.facebook_login_btn) com.facebook.login.widget.LoginButton facebook;
    @BindView(R.id.btn_login) Button btn_login;
    @BindView(R.id.login_facebook) ImageButton btn_facebook;
    @BindView(R.id.login_google) ImageButton btn_google;
    @BindView(R.id.ed_email) EditText ed_email;
    @BindView(R.id.ed_password) EditText ed_password;
    @BindView(R.id.txt_find_password) TextView txt_find_password;
    @BindView(R.id.txt_regist) TextView txt_regist;

    private SNSHelper helper;
    private int emailLength = 0;
    private int passLength = 0;

    public static LoginFragment newInstance(int page)
    {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUM, page);
        loginFragment.setArguments(bundle);

        return loginFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(mView == null)
        {
            mListner = new LoginFragment.Listner();
            mView = inflater.inflate(R.layout.fragment_login, container, false);
            init(container);
            initScreen();
            setTextSpan();
           // userLogin();
        }

        return mView;
    }

    public void initScreen()
    {

        facebook.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));
        btn_facebook.setOnClickListener(mListner);
        btn_google.setOnClickListener(mListner);
        btn_login.setOnClickListener(mListner);
        helper = new SNSHelper((BaseActivity) getActivity(), facebook, true);


        ed_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    view.setBackgroundResource(R.drawable.round_stroke_login_corner_focus);
                else
                    view.setBackgroundResource(R.drawable.custom_editview);
            }
        });

        ed_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    view.setBackgroundResource(R.drawable.round_stroke_login_corner_focus);
                else
                    view.setBackgroundResource(R.drawable.custom_editview);
            }
        });


        ed_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                formStatusCheck();
                if(emailLength == 0)
                {
                    btn_login.setBackgroundResource(R.drawable.round_corner);
                    btn_login.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        ed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                formStatusCheck();
                if(passLength == 0)
                {
                    btn_login.setBackgroundResource(R.drawable.round_corner);
                    btn_login.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void formStatusCheck()
    {
        emailLength = ed_email.getText().toString().length();
        passLength = ed_password.getText().toString().length();
        if(emailLength > 0 && passLength > 0)
        {
            btn_login.setBackgroundResource(R.drawable.btn_round_stroke_corner);
            btn_login.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void setTextSpan()
    {
        Spannable spannablePass = (Spannable) txt_find_password.getText();
        String textPass = spannablePass.toString();

        int start = textPass.indexOf(txt_find_password.getText().toString());
        int end = start + txt_find_password.getText().toString().length();

        spannablePass.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                mUpdateListenerCallBack.addFragment(FindPasswordFragment.newInstance(Constants.FRAGMENT_MENUID.FIND_PASSWORD), Constants.FRAGMENT_MENUID.FIND_PASSWORD);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannablePass.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_find_password.setText(spannablePass);
        txt_find_password.setMovementMethod(new LinkMovementMethod());

        Spannable spannableReg = (Spannable) txt_regist.getText();
        String textReg = spannableReg.toString();

        int start2 = textReg.indexOf(txt_regist.getText().toString());
        int end2 = start + txt_regist.getText().toString().length();

        spannableReg.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                mUpdateListenerCallBack.addFragment(Constants.FRAGMENT_MENUID.SINGUP);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, start2, end2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableReg.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_regist.setText(spannableReg);
        txt_regist.setMovementMethod(new LinkMovementMethod());
    }

    private void userLogin()
    {

        String email = ed_email.getText().toString();
        if(Utils.checkEmail(email)) {

            startIndicator("");

            String joinType = Constants.LOGIN_TYPE.EMAIL;
            String gcmToken = BasePreference.getInstance(getActivity()).getValue(BasePreference.GCM_TOKEN, "");
            String locale = BasePreference.getInstance(getActivity()).getValue(BasePreference.LOCALE, null);

            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.JOIN_TYPE, joinType);
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.EMAIL, ed_email.getText().toString());
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.PASSWORD, ed_password.getText().toString());
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.GCM_TOKEN, gcmToken);
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.DEVICE_TYPE, "android");
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.LOCALE, locale);

            HashMap<String, String> params = UserInfo.getInstance().getLoginParams();

            DataManager.getInstance(getActivity()).api.userLogin(getActivity(), params, new DataInterface.ResponseCallback<UserInfoResultData>() {
                @Override
                public void onSuccess(UserInfoResultData response) {
                    stopIndicator();

                    String result = response.getResult();
                    if(result.equals(Constants.API_FAIL))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle(R.string.app_name).setMessage(response.getMessage()).setPositiveButton(getString(R.string.yes), null).create().show();
                    }
                    else
                    {
                        UserInfoData userInfoData = response.userData;
                        UserInfo.getInstance().setCurrentUserInfoData(getActivity(), userInfoData);
                        BasePreference.getInstance(getActivity()).put(BasePreference.JOIN_TYPE, userInfoData.getJoinType());
                        BasePreference.getInstance(getActivity()).put(BasePreference.AUTH_ID, userInfoData.getAuthId());
                        BasePreference.getInstance(getActivity()).put(BasePreference.LOCALE, userInfoData.getLocale());
                        BasePreference.getInstance(getActivity()).put(BasePreference.ID, userInfoData.getId());
                        BasePreference.getInstance(getActivity()).putObject(BasePreference.USERINFO_DATA, userInfoData);

                        String locale = BasePreference.getInstance(getActivity()).getValue(BasePreference.LOCALE, null);
                        //((BaseActivity)getActivity()).setLocale(locale);

                        IntentData indata = new IntentData();
                        indata.isRegist = false;
                        indata.link = String.format(Constants.MAIN_URL, userInfoData.getId());
                        Intent intent = new Intent(getActivity(), MainPleaListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(Constants.INTENT_DATA_KEY, indata);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }

                @Override
                public void onError() {
                    stopIndicator();
                }
            });

        }
        else
        {
            stopIndicator();
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(R.string.app_name).setMessage(getString(R.string.email_pattern_error)).setPositiveButton(getString(R.string.yes), null).create().show();
        }


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
                case R.id.btn_login :
                    UserInfo.getInstance().clearParams();
                    userLogin();
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
