package shop.plea.and.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapAlert;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapLabel;

import java.util.HashMap;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.tool.Logger;
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
    private int emailLength = 0;
    private InputMethodManager inputMethodManager;

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

        ((TextView) toolbar_header.findViewById(R.id.toolbar_title)).setText(getString(R.string.create_new_password));

        Utils.changeStatusColor((BaseActivity) getActivity(), R.color.colorSubHeader);

    }

    public void initScreen()
    {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        toolbar_header.findViewById(R.id.toolbar_back).setOnClickListener(mListner);
        btn_send_password_set_mail.setOnClickListener(mListner);
        ed_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    view.setBackgroundResource(R.drawable.edit_focus_round_stroke);
                }
                else
                {
                    view.setBackgroundResource(R.drawable.custom_editview);
                }
            }
        });

        ed_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                emailLength = ed_email.getText().toString().length();
                Log.e("PLEA", "emailLength : " + String.valueOf(emailLength));
                if(emailLength > 0)
                {
                    btn_send_password_set_mail.setBackgroundResource(R.drawable.round_stroke_corner_focus);
                    btn_send_password_set_mail.setTextColor(Color.WHITE);
                }
                if(emailLength == 0)
                {
                    btn_send_password_set_mail.setBackgroundResource(R.drawable.round_stroke_corner);
                    btn_send_password_set_mail.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void sendPasswordMail(String email)
    {
        if(Utils.checkEmail(email) && email.length() > 0)
        {
            startIndicator("");
            DataManager.getInstance(getActivity()).api.userSendPasswordMail(getActivity(), email, new DataInterface.ResponseCallback<ResponseData>() {
                @Override
                public void onSuccess(ResponseData response) {
                    Logger.log(Logger.LogState.E, "response : " + Utils.getStringByObject(response));
                    if(response.getResult().equals(Constants.API_FAIL))
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle(R.string.app_name).setMessage(response.getMessage()).setPositiveButton(getString(R.string.yes), null).create().show();
                    }
                    else
                    {

                        String result = response.getResult();
                        if(result.equals(Constants.API_SUCCESS)) {

                                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                dialog.setTitle(R.string.app_name).setMessage(getString(R.string.email_send_ok)).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mUpdateListenerCallBack.fragmentBackPressed();
                                        mUpdateListenerCallBack.addFragment(Constants.FRAGMENT_MENUID.LOGIN);
                                    }
                                }).create().show();
                        }
                    }
                    stopIndicator();
                }

                @Override
                public void onError() {
                    stopIndicator();
                }
            });
        }
        else
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle(R.string.app_name).setMessage(getString(R.string.email_pattern_error)).setPositiveButton(getString(R.string.yes), null).create().show();
        }

    }

    private class Listner implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.toolbar_back :
                    inputMethodManager.hideSoftInputFromWindow(ed_email.getWindowToken(), 0);
                    mUpdateListenerCallBack.fragmentBackPressed();
                    break;
                case R.id.btn_send_password_set_mail :
                    String email = ed_email.getText().toString();
                    sendPasswordMail(email);
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
