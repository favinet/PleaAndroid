package shop.plea.and.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.ResponseData;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.data.model.UserInfoResultData;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;
import shop.plea.and.data.tool.LocaleChage;
import shop.plea.and.ui.activity.InAppWebView;
import shop.plea.and.ui.activity.MainPleaListActivity;
import shop.plea.and.ui.adapter.SpinnerBirthAdapter;
import shop.plea.and.ui.view.CustomFontBtn;
import shop.plea.and.ui.view.CustomFontEditView;
import shop.plea.and.ui.view.CustomFontTextView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kwon7575 on 2017-10-20.
 */

public class SignUpInfoFragment extends BaseFragment{

    public static final String ARG_PAGE_NUM = "ARG_PAGE_NUM";
    public static final String ARG_EMAIL = "ARG_EMAIL";
    public static final String ARG_SNS_EMAIL = "ARG_SNS_EMAIL";
    public static final String ARG_PASSWORD = "ARG_PASSWORD";
    public static final String ARG_JOIN_TYP = "ARG_JOIN_TYP";
    public static final String ARG_AUTHID = "ARG_AUTHID";
    public static final String ARG_PROFILE_IMG = "ARG_PROFILE_IMG";

    private final static int INTENT_CALL_GALLERY = 3001;
    private List<FileInfo> fileInfoList = new ArrayList<>();
    private final String MULTI_PART = "multipart/form-data";

    private SignUpInfoFragment.Listner mListner;
    private int emailLength = 0;
    private int nickLength = 0;

    @BindView(R.id.ed_nickname) CustomFontEditView ed_nickname;
    @BindView(R.id.ed_nickname_alert) CustomFontTextView ed_nickname_alert;
    @BindView(R.id.ed_email) CustomFontEditView ed_email;

    @BindView(R.id.upload_profile) ImageView upload_profile;
    @BindView(R.id.btn_regist_end) CustomFontBtn btn_regist_end;
    @BindView(R.id.txt_agree_info) CustomFontTextView txt_agree_info;
    @BindView(R.id.img_profile) BootstrapCircleThumbnail img_profile;
    @BindView(R.id.toolbar_header) Toolbar toolbar_header;
    @BindView(R.id.spinner_birth) Spinner spinner_birth;
    @BindView(R.id.gender_m) CustomFontTextView gender_m;
    @BindView(R.id.gender_f) CustomFontTextView gender_f;
    @BindView(R.id.screen_sign_info) RelativeLayout screen_sign_info;


    private SpinnerBirthAdapter spinnerBirthAdapter;
    private String gender;
    private String birth;
    private InputMethodManager inputMethodManager;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == INTENT_CALL_GALLERY) { // 킷캣.
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();

                File file = Utils.getAlbum(getActivity(), result);
                if(file == null)
                {
                    stopIndicator();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle(R.string.app_name).setMessage(getString(R.string.gallery_error)).setPositiveButton(getString(R.string.yes), null).create().show();
                }
                else
                {
                    fileInfoList.add(new FileInfo(result, file));
                    refreshThums();
                }

                return;
            }
        }
    }

    //public static SignUpInfoFragment newInstance(int page, String email, String passwd, String typ)
    public static SignUpInfoFragment newInstance(int page, HashMap<String, String> params)
    {

        SignUpInfoFragment signUpInfoFragment = new SignUpInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUM, page);
        bundle.putString(ARG_EMAIL, params.get(Constants.API_PARAMS_KEYS.EMAIL));
        bundle.putString(ARG_SNS_EMAIL, params.get(Constants.API_PARAMS_KEYS.SNS_EMAIL));
        bundle.putString(ARG_PASSWORD, params.get(Constants.API_PARAMS_KEYS.PASSWORD));
        bundle.putString(ARG_JOIN_TYP, params.get(Constants.API_PARAMS_KEYS.JOIN_TYPE));
        bundle.putString(ARG_AUTHID, params.get(Constants.API_PARAMS_KEYS.AUTHID));
        bundle.putString(ARG_PROFILE_IMG, params.get(Constants.API_PARAMS_KEYS.PROFILE_IMG));
        signUpInfoFragment.setArguments(bundle);

        return signUpInfoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView == null)
        {
            mListner = new SignUpInfoFragment.Listner();
            mView = inflater.inflate(R.layout.fragment_signup_info, container, false);
            init(container);
            initToobar();
            initScreen();
            setTextSpan();
        }

        return mView;
    }

    private void initToobar()
    {
        toolbar_header.findViewById(R.id.toolbar_back).setVisibility(View.VISIBLE);
        toolbar_header.findViewById(R.id.toolbar_title).setVisibility(View.VISIBLE);
        toolbar_header.setBackgroundColor(getResources().getColor(R.color.colorSubHeader));
        ((TextView) toolbar_header.findViewById(R.id.toolbar_title)).setText(getString(R.string.SIGNUP));
        Utils.changeStatusColor((BaseActivity) getActivity(), R.color.colorSubHeader);
    }

    public void initScreen()
    {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        upload_profile.setOnClickListener(mListner);
        btn_regist_end.setOnClickListener(mListner);
        img_profile.setOnClickListener(mListner);
        gender_m.setOnClickListener(mListner);
        gender_f.setOnClickListener(mListner);
        screen_sign_info.setOnClickListener(mListner);
        toolbar_header.findViewById(R.id.toolbar_back).setOnClickListener(mListner);
        String email = (getJoinType().equals("email")) ? getEmail() : getSnsEmail();
        ed_email.setText(email);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int getTime = Integer.valueOf(sdf.format(date));

        //데이터
        final List<String> data = new ArrayList<>();
        data.add(getString(R.string.birth));
        for(int i = getTime; i >= getTime-97; i--)
        {
            data.add(String.valueOf(i));
        }

        //spinner_birth.setPrompt(getString(R.string.birth));

        //Adapter
        spinnerBirthAdapter = new SpinnerBirthAdapter(getActivity(), data);

        //Adapter 적용
        spinner_birth.setAdapter(spinnerBirthAdapter);
        /*
        spinner_birth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                spinner_birth.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spinner_birth.setScaleY(200F);
                    }
                }, 1000);

                return false;
            }
        });
        */

        spinner_birth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                birth = (String) spinner_birth.getSelectedItem();
                if(birth.equals(getString(R.string.birth))) birth = null;
                formStatusCheck();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

                formStatusCheck();
                if(emailLength == 0)
                {
                    btn_regist_end.setBackgroundResource(R.drawable.round_stroke_corner);
                    btn_regist_end.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        ed_nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        ed_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                formStatusCheck();
                if(nickLength == 0)
                {
                    btn_regist_end.setBackgroundResource(R.drawable.round_stroke_corner);
                    btn_regist_end.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                /*
                if(ed_nickname.getText().toString().replace(" ", "").equals("")){
                    String replaceNick = ed_nickname.getText().toString().replace(" ", "");
                    ed_nickname.setText(replaceNick);
                }
                */

            }
        });

    }

    private void formStatusCheck()
    {
        emailLength = ed_email.getText().toString().length();
        nickLength = ed_nickname.getText().toString().length();
        if(emailLength > 0 && nickLength > 0 && birth != null && gender != null)
        {
            btn_regist_end.setBackgroundResource(R.drawable.round_stroke_corner_focus);
            btn_regist_end.setTextColor(Color.WHITE);
        }
    }

    private String getEmail()
    {
        return (getArguments().getString(ARG_EMAIL) == null) ? ed_email.getText().toString() : getArguments().getString(ARG_EMAIL);
    }
    private String getSnsEmail()
    {
        return (getArguments().getString(ARG_SNS_EMAIL) == null) ? ed_email.getText().toString() : getArguments().getString(ARG_SNS_EMAIL);
    }
    private String getPassword()
    {
        return getArguments().getString(ARG_PASSWORD);
    }
    private String getJoinType()
    {
        return getArguments().getString(ARG_JOIN_TYP);
    }
    private String getAuthId()
    {
        return getArguments().getString(ARG_AUTHID);
    }
    private String getProfileImg()
    {
        return getArguments().getString(ARG_PROFILE_IMG);
    }

    private void setTextSpan()
    {
        Spannable spannable = (Spannable) txt_agree_info.getText();
        String text = spannable.toString();

        String term = (text.indexOf("Terms") > 0) ? "Terms" : "서비스 이용약관" ;
        int start = text.indexOf(term);
        int end = start + term.length();
        String privacy = (text.indexOf("Privacy Policy.") > 0) ? "Privacy Policy." : "개인정보 처리" ;
        int start2 = text.indexOf(privacy);
        int end2 = start2 + privacy.length();
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                IntentData indata = new IntentData();
                Configuration config = getResources().getConfiguration();
                String slocale = BasePreference.getInstance(getActivity()).getValue(BasePreference.LOCALE, LocaleChage.getSystemLocale(config).getLanguage());
                UserInfoData userInfoData = BasePreference.getInstance(getActivity()).getObject(BasePreference.USERINFO_DATA, UserInfoData.class);
                String locale = (userInfoData == null) ? slocale : userInfoData.getLocale();
                indata.link = String.format(Constants.MENU_LINKS.TERMS, locale);
                indata.title = getString(R.string.menu_term);
                indata.aniType = Constants.VIEW_ANIMATION.ANI_END_ENTER;
                indata.screenType = Constants.SCREEN_TYPE.INAPP;
                Intent intent = new Intent(getActivity(), InAppWebView.class);
                intent.putExtra(Constants.INTENT_DATA_KEY, indata);
                getActivity().startActivity(intent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
            }
        }, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                IntentData indata = new IntentData();
                Configuration config = getResources().getConfiguration();
                String slocale = BasePreference.getInstance(getActivity()).getValue(BasePreference.LOCALE, LocaleChage.getSystemLocale(config).getLanguage());
                UserInfoData userInfoData = BasePreference.getInstance(getActivity()).getObject(BasePreference.USERINFO_DATA, UserInfoData.class);
                String locale = (userInfoData == null) ? slocale : userInfoData.getLocale();
                indata.link = String.format(Constants.MENU_LINKS.POLICY, locale);
                indata.title = getString(R.string.menu_privacy);
                indata.aniType = Constants.VIEW_ANIMATION.ANI_END_ENTER;
                indata.screenType = Constants.SCREEN_TYPE.INAPP;
                Intent intent = new Intent(getActivity(), InAppWebView.class);
                intent.putExtra(Constants.INTENT_DATA_KEY, indata);
                getActivity().startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
            }
        }, start2, end2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_agree_info.setText(spannable);
        txt_agree_info.setMovementMethod(new LinkMovementMethod());
    }

    private void refreshThums()
    {
        img_profile.setVisibility(View.INVISIBLE);

        int i=0;
        for(FileInfo info : fileInfoList)
        {
            switch (i)
            {
                case 0:
                    img_profile.setImageURI(info.uri);
                    img_profile.setVisibility(View.VISIBLE);
                    break;
            }
            i++;
        }
    }

    private void registUser()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        if(gender == null)
        {
            dialog.setTitle(R.string.app_name).setMessage(getString(R.string.gener_error)).setPositiveButton(getString(R.string.yes), null).create().show();
        }
        else if(birth == null)
        {
            dialog.setTitle(R.string.app_name).setMessage(getString(R.string.birth_error)).setPositiveButton(getString(R.string.yes), null).create().show();
        }
        else
        {
            startIndicator("");
            Map<String, RequestBody> params = new HashMap<>();

            String joinType = getJoinType();
            String locale = BasePreference.getInstance(getActivity()).getValue(BasePreference.LOCALE, "en");
            File file = (fileInfoList.size() > 0) ? fileInfoList.get(0).file : null;

            RequestBody loginTypBody = RequestBody.create(MediaType.parse(MULTI_PART), joinType);
            RequestBody nickNameBody = RequestBody.create(MediaType.parse(MULTI_PART), ed_nickname.getText().toString());
            if(loginTypBody != null) params.put(Constants.API_PARAMS_KEYS.JOIN_TYPE, loginTypBody);
            if(nickNameBody != null) params.put(Constants.API_PARAMS_KEYS.NICKNAME, nickNameBody);

            if(joinType.equals(Constants.LOGIN_TYPE.EMAIL))
            {
                RequestBody pwdBody = RequestBody.create(MediaType.parse(MULTI_PART), getPassword());
                RequestBody emailBody = RequestBody.create(MediaType.parse(MULTI_PART), ed_email.getText().toString());
                if(pwdBody != null) params.put(Constants.API_PARAMS_KEYS.PASSWORD, pwdBody);
                if(emailBody != null) params.put(Constants.API_PARAMS_KEYS.EMAIL, emailBody);
            }
            else
            {
                RequestBody authIdBody = RequestBody.create(MediaType.parse(MULTI_PART), getAuthId());
                RequestBody profileImgBody = RequestBody.create(MediaType.parse(MULTI_PART), getProfileImg());
                RequestBody snsEmailBody = RequestBody.create(MediaType.parse(MULTI_PART), ed_email.getText().toString());
                if(authIdBody != null) params.put(Constants.API_PARAMS_KEYS.AUTHID, authIdBody);
                if(profileImgBody != null) params.put(Constants.API_PARAMS_KEYS.PROFILE_IMG, profileImgBody);
                if(snsEmailBody != null) params.put(Constants.API_PARAMS_KEYS.SNS_EMAIL, snsEmailBody);
            }

            RequestBody deviceTypeBody = RequestBody.create(MediaType.parse(MULTI_PART), "android");
            params.put(Constants.API_PARAMS_KEYS.DEVICE_TYPE, deviceTypeBody);

            String gcmToken = BasePreference.getInstance(getActivity()).getValue(BasePreference.GCM_TOKEN, "");
            RequestBody tokenBody = RequestBody.create(MediaType.parse(MULTI_PART), gcmToken);
            params.put(Constants.API_PARAMS_KEYS.GCM_TOKEN, tokenBody);

            RequestBody genderBody = RequestBody.create(MediaType.parse(MULTI_PART), gender);
            params.put(Constants.API_PARAMS_KEYS.GENDER, genderBody);

            RequestBody birthBody = RequestBody.create(MediaType.parse(MULTI_PART), birth);
            params.put(Constants.API_PARAMS_KEYS.BIRTH, birthBody);

            RequestBody localeBody = RequestBody.create(MediaType.parse(MULTI_PART), locale);
            params.put(Constants.API_PARAMS_KEYS.LOCALE, localeBody);

            DataManager.getInstance(getActivity()).api.userRegist(getActivity(), params, file, new DataInterface.ResponseCallback<UserInfoResultData>() {
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
                        userLogin(userInfoData);
                    }
                }

                @Override
                public void onError() {
                    stopIndicator();
                }
            });
        }

    }

    private void userLogin(UserInfoData userInfoData)
    {
        startIndicator("");
        UserInfo.getInstance().clearParams();

        String joinType = userInfoData.getJoinType();
        Configuration config = getResources().getConfiguration();
        String locale = BasePreference.getInstance(getActivity()).getValue(BasePreference.LOCALE, LocaleChage.getSystemLocale(config).getLanguage());

        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.JOIN_TYPE, joinType);

        if(joinType.equals(Constants.LOGIN_TYPE.EMAIL))
        {
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.EMAIL, userInfoData.getEmail());
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.PASSWORD, getPassword());
        }
        else
        {
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.AUTHID, userInfoData.getAuthId());
        }

        String gcmToken = BasePreference.getInstance(getActivity()).getValue(BasePreference.GCM_TOKEN, "");
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
                    BasePreference.getInstance(getActivity()).put(BasePreference.ID, userInfoData.getId());
                    BasePreference.getInstance(getActivity()).put(BasePreference.JOIN_TYPE, userInfoData.getJoinType());
                    BasePreference.getInstance(getActivity()).put(BasePreference.AUTH_ID, userInfoData.getAuthId());
                    BasePreference.getInstance(getActivity()).put(BasePreference.LOCALE, userInfoData.getLocale());
                    BasePreference.getInstance(getActivity()).putObject(BasePreference.USERINFO_DATA, userInfoData);

                    IntentData indata = new IntentData();
                    indata.isRegist = true;
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

    public void callGallery() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "File Chooser"), INTENT_CALL_GALLERY);
    }

    private class Listner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.upload_profile :
                    callGallery();
                    break;
                case R.id.screen_sign_info :
                    inputMethodManager.hideSoftInputFromWindow(ed_email.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(ed_nickname.getWindowToken(), 0);
                    break;
                case R.id.toolbar_back :
                    inputMethodManager.hideSoftInputFromWindow(ed_email.getWindowToken(), 0);
                    inputMethodManager.hideSoftInputFromWindow(ed_nickname.getWindowToken(), 0);
                    mUpdateListenerCallBack.fragmentBackPressed();
                    break;
                case R.id.gender_m :
                    gender_m.setBackgroundResource(R.drawable.round_txt_gender_ltlb_on);
                    gender_m.setTextColor(Color.WHITE);
                    gender_f.setBackgroundResource(R.drawable.round_txt_gender_rtrb_off);
                    gender_f.setTextColor(Color.BLACK);
                    gender = "M";
                    formStatusCheck();
                    break;
                case R.id.gender_f :
                    gender_m.setBackgroundResource(R.drawable.round_txt_gender_ltlb_off);
                    gender_m.setTextColor(Color.BLACK);
                    gender_f.setBackgroundResource(R.drawable.round_txt_gender_rtrb_on);
                    gender_f.setTextColor(Color.WHITE);
                    gender = "F";
                    formStatusCheck();
                    break;
                case R.id.btn_regist_end :

                    if(Utils.checkEmail(ed_email.getText().toString()))
                    {
                        String nickname = ed_nickname.getText().toString().replace(" ", "");
                        String locale = BasePreference.getInstance(getActivity()).getValue(BasePreference.LOCALE, "en");

                        if(nickname.length() > 0)
                        {
                            startIndicator("");
                            DataManager.getInstance(getActivity()).api.userNickNameCheck(getActivity(), nickname, locale, new DataInterface.ResponseCallback<ResponseData>() {
                                @Override
                                public void onSuccess(ResponseData response) {
                                    ed_nickname_alert.setText(response.getMessage());
                                    if(response.getResult().equals(Constants.API_FAIL))
                                    {
                                        stopIndicator();
                                        return;
                                    }
                                    else
                                    {
                                        stopIndicator();
                                        boolean flag = response.isFlag();
                                        if(flag)
                                        {
                                            ed_nickname.setTextColor(Color.parseColor("#000000"));
                                            if(ed_nickname_alert.getVisibility() == View.VISIBLE)
                                                ed_nickname_alert.setVisibility(View.GONE);

                                            registUser();
                                        }
                                        else
                                        {
                                            ed_nickname.setTextColor(Color.parseColor("#E83636"));
                                            if(ed_nickname_alert.getVisibility() == View.GONE)
                                                ed_nickname_alert.setVisibility(View.VISIBLE);
                                            else
                                                ed_nickname_alert.setVisibility(View.GONE);
                                            return;
                                        }
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle(R.string.app_name).setMessage(getString(R.string.nick_empty)).setPositiveButton(getString(R.string.yes), null).create().show();
                            stopIndicator();
                            return;
                        }
                    }
                    else
                    {
                        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity());
                        dialog.setTitle(R.string.app_name).setMessage(getString(R.string.email_pattern_error)).setPositiveButton(getString(R.string.yes), null).create().show();
                    }

                    break;
            }
        }
    }

    private class FileInfo{
        Uri uri;
        File file;

        public FileInfo(Uri uri, File file)
        {
            this.uri = uri;
            this.file = file;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.changeStatusColor((BaseActivity) getActivity(), R.color.colorPrimary);
    }
}
