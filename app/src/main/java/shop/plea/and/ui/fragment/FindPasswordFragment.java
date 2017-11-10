package shop.plea.and.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapAlert;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapLabel;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.ResponseData;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;

/**
 * Created by kwon7575 on 2017-10-20.
 */

public class FindPasswordFragment extends BaseFragment {

    public static final String ARG_PAGE_NUM = "ARG_PAGE_NUM";
    private FindPasswordFragment.Listner mListner;
    @BindView(R.id.btn_send_password_set_mail) Button btn_send_password_set_mail;
    @BindView(R.id.ed_email) EditText ed_email;
    @BindView(R.id.toolbar_header) Toolbar toolbar_header;

    public static FindPasswordFragment newInstance(int page)
    {
        FindPasswordFragment findPasswordFragment = new FindPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUM, page);
        findPasswordFragment.setArguments(bundle);

        return findPasswordFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null)
        {
            mListner = new FindPasswordFragment.Listner();
            mView = inflater.inflate(R.layout.fragment_password_find, container, false);
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

        ((TextView) toolbar_header.findViewById(R.id.toolbar_title)).setText("Create new password");

        Utils.changeStatusColor((BaseActivity) getActivity(), R.color.colorSubHeader);

    }

    public void initScreen()
    {
        toolbar_header.findViewById(R.id.toolbar_back).setOnClickListener(mListner);
        btn_send_password_set_mail.setOnClickListener(mListner);
    }

    private void userEmailCheck(String email)
    {
        if(Utils.checkEmail(email) && email.length() > 0)
        {
            startIndicator("");
            DataManager.getInstance(getActivity()).api.userEmailCheck(getActivity(), email, new DataInterface.ResponseCallback<ResponseData>() {
                @Override
                public void onSuccess(ResponseData response) {
                    if(response.getResult().equals(Constants.API_FAIL))
                    {
                        Toast.makeText(getActivity(), "이메일체크 실패!!" + response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        boolean flag = response.isFlag();
                        if(flag)
                        {
                            ed_email.setTextColor(Color.parseColor("#000000"));
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "이메일체크 사용 불가능!!" + flag, Toast.LENGTH_LONG).show();
                            ed_email.setTextColor(Color.parseColor("#E83636"));

                        }
                    }

                    stopIndicator();
                }

                @Override
                public void onError() {
                    Toast.makeText(getActivity(), "이메일 체크 실패!", Toast.LENGTH_LONG).show();
                    stopIndicator();
                }
            });
        }
        else
        {
            Toast.makeText(getActivity(), "이메일 형식이 아닙니다.!!" + email, Toast.LENGTH_LONG).show();
        }

    }

    private class Listner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.toolbar_back :
                    mUpdateListenerCallBack.fragmentBackPressed();
                    break;
                case R.id.btn_send_password_set_mail :
                    String email = ed_email.getText().toString();
                    userEmailCheck(email);

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
