package shop.plea.and.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.ResponseData;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;

/**
 * Created by kwon7575 on 2017-10-25.
 */

public class EmailSignUpFragment extends BaseFragment{

    public static final String ARG_PAGE_NUM = "ARG_PAGE_NUM";
    private EmailSignUpFragment.Listner mListner;
    @BindView(R.id.ed_email) EditText ed_email;
    @BindView(R.id.ed_email_alert) TextView ed_email_alert;
    @BindView(R.id.ed_password) EditText ed_password;
    @BindView(R.id.ed_password_alert) TextView ed_password_alert;
    @BindView(R.id.btn_regist_next) Button btn_regist_next;
    @BindView(R.id.toolbar_header) Toolbar toolbar_header;

    public static EmailSignUpFragment newInstance(int page)
    {
        EmailSignUpFragment emailSignUpFragment = new EmailSignUpFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUM, page);
        emailSignUpFragment.setArguments(bundle);

        return emailSignUpFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null)
        {
            mListner = new EmailSignUpFragment.Listner();
            mView = inflater.inflate(R.layout.fragment_email_signup, container, false);
            init(container);
            initToobar();
            initScreen();
        }

        return mView;
    }

    private void initToobar()
    {
        toolbar_header.findViewById(R.id.toolbar_back).setVisibility(View.VISIBLE);
        toolbar_header.findViewById(R.id.toolbar_title).setVisibility(View.VISIBLE);
        toolbar_header.setBackgroundColor(getResources().getColor(R.color.colorSubHeader));
        ((TextView) toolbar_header.findViewById(R.id.toolbar_title)).setText("SIGN UP");

        Utils.changeStatusColor((BaseActivity) getActivity(), R.color.colorSubHeader);
    }

    public void initScreen()
    {
        btn_regist_next.setOnClickListener(mListner);
        ((BaseActivity)mContext).setSupportActionBar(toolbar_header);
        toolbar_header.findViewById(R.id.toolbar_back).setOnClickListener(mListner);
    }

    private void userPasswordCheck(String email)
    {
        String password = ed_password.getText().toString();
        if(Utils.validatePassword(password))
        {

            ed_password_alert.setVisibility(View.GONE);

            UserInfo.getInstance().clearParams();
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.JOIN_TYPE, Constants.LOGIN_TYPE.EMAIL);
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.EMAIL, email);
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.PASSWORD, password);
            SignUpInfoFragment signUpInfoFragment = new SignUpInfoFragment().newInstance(
                    Constants.FRAGMENT_MENUID.SIGN_INFO,
                    UserInfo.getInstance().getLoginParams()
            );
            mUpdateListenerCallBack.addFragment(signUpInfoFragment, Constants.FRAGMENT_MENUID.SIGN_INFO);
        }
        else
        {
            ed_password_alert.setVisibility(View.VISIBLE);
            return;
        }
    }

    private class Listner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_regist_next :

                    final String email = ed_email.getText().toString();
                    if(Utils.checkEmail(email) && email.length() > 0)
                    {
                        startIndicator("");
                        DataManager.getInstance(getActivity()).api.userEmailCheck(getActivity(), email, new DataInterface.ResponseCallback<ResponseData>() {
                            @Override
                            public void onSuccess(ResponseData response) {

                                if(response.getResult().equals(Constants.API_FAIL))
                                {
                                    stopIndicator();
                                    Toast.makeText(getActivity(), "Server Error!" + response.getMessage(), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                else
                                {
                                    stopIndicator();
                                    boolean flag = response.isFlag();
                                    if(flag)
                                    {
                                        ed_email_alert.setVisibility(View.GONE);
                                        userPasswordCheck(email);
                                    }
                                    else
                                    {
                                        ed_email_alert.setVisibility(View.VISIBLE);
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getActivity(), "userLogin onError", Toast.LENGTH_SHORT).show();
                                stopIndicator();
                            }
                        });
                    }
                    else
                    {
                        stopIndicator();
                        if(ed_email_alert.getVisibility() == View.GONE)
                            ed_email_alert.setVisibility(View.VISIBLE);
                        else
                            ed_email_alert.setVisibility(View.GONE);
                        return;
                    }
                    break;

                case R.id.toolbar_back :
                    mUpdateListenerCallBack.fragmentBackPressed();
                    break;

            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.changeStatusColor((BaseActivity) getActivity(), R.color.colorPrimary);
    }
}