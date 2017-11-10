package shop.plea.and.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;

/**
 * Created by kwon7575 on 2017-11-09.
 */

public class ResetPasswordFragment extends BaseFragment {

    public static final String ARG_PAGE_NUM = "ARG_PAGE_NUM";
    private ResetPasswordFragment.Listner mListner;
    @BindView(R.id.btn_finished) Button btn_finished;
    @BindView(R.id.btn_password_login) Button btn_password_login;
    @BindView(R.id.ed_password) EditText ed_password;
    @BindView(R.id.ed_password_confirm) EditText ed_password_confirm;
    @BindView(R.id.toolbar_header) Toolbar toolbar_header;

    public static ResetPasswordFragment newInstance(int page)
    {
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUM, page);
        resetPasswordFragment.setArguments(bundle);

        return resetPasswordFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null)
        {
            mListner = new ResetPasswordFragment.Listner();
            mView = inflater.inflate(R.layout.fragment_password_reset, container, false);
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

        ((TextView) toolbar_header.findViewById(R.id.toolbar_title)).setText("Reset password");

        Utils.changeStatusColor((BaseActivity) getActivity(), R.color.colorSubHeader);

    }

    public void initScreen()
    {
        toolbar_header.findViewById(R.id.toolbar_back).setOnClickListener(mListner);
        btn_finished.setOnClickListener(mListner);
        btn_password_login.setOnClickListener(mListner);
    }

    private class Listner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.toolbar_back :
                    mUpdateListenerCallBack.fragmentBackPressed();
                    break;
                case R.id.btn_finished :
                    getActivity().finish();
                    break;
                case R.id.btn_password_login :
                    mUpdateListenerCallBack.addFragment(Constants.FRAGMENT_MENUID.LOGIN);
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
