package shop.plea.and.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

import java.io.File;
import java.util.ArrayList;
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
import shop.plea.and.ui.activity.MainPleaListActivity;
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

    @BindView(R.id.ed_nickname) CustomFontEditView ed_nickname;
    @BindView(R.id.ed_nickname_alert) CustomFontTextView ed_nickname_alert;
    @BindView(R.id.ed_email) CustomFontEditView ed_email;

    @BindView(R.id.upload_profile) ImageView upload_profile;
    @BindView(R.id.btn_regist_end) CustomFontBtn btn_regist_end;
    @BindView(R.id.txt_agree_info) CustomFontTextView txt_agree_info;
    @BindView(R.id.img_profile) BootstrapCircleThumbnail img_profile;
    @BindView(R.id.toolbar_header) Toolbar toolbar_header;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == INTENT_CALL_GALLERY) { // 킷캣.
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();

                File file = Utils.getAlbum(getActivity(), result);

                fileInfoList.add(new FileInfo(result, file));

                refreshThums();
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

            Logger.log(Logger.LogState.E, "CHK : " + getEmail());
            Logger.log(Logger.LogState.E, "CHK : " + getPassword());
            Logger.log(Logger.LogState.E, "CHK : " + getJoinType());
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
        upload_profile.setOnClickListener(mListner);
        btn_regist_end.setOnClickListener(mListner);
        img_profile.setOnClickListener(mListner);
        toolbar_header.findViewById(R.id.toolbar_back).setOnClickListener(mListner);
        String email = (getJoinType().equals("email")) ? getEmail() : getSnsEmail();
        ed_email.setText(email);
    }

    private String getEmail()
    {
        return getArguments().getString(ARG_EMAIL);
    }
    private String getSnsEmail()
    {
        return getArguments().getString(ARG_SNS_EMAIL);
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

        int start = text.indexOf("Terms");
        int end = start + "Terms".length();
        int start2 = text.indexOf("Privacy Policy.");
        int end2 = start2 + "Privacy Policy.".length();
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getActivity(), "Terms", Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
            }
        }, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getActivity(), "Privacy Policy.", Toast.LENGTH_LONG).show();
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
        startIndicator("");
        Map<String, RequestBody> params = new HashMap<>();

        String joinType = getJoinType();
        File file = (fileInfoList.size() > 0) ? fileInfoList.get(0).file : null;

        RequestBody loginTypBody = RequestBody.create(MediaType.parse(MULTI_PART), joinType);
        RequestBody nickNameBody = RequestBody.create(MediaType.parse(MULTI_PART), ed_nickname.getText().toString());
        if(loginTypBody != null) params.put(Constants.API_PARAMS_KEYS.JOIN_TYPE, loginTypBody);
        if(nickNameBody != null) params.put(Constants.API_PARAMS_KEYS.NICKNAME, nickNameBody);

        if(joinType.equals(Constants.LOGIN_TYPE.EMAIL))
        {
            RequestBody pwdBody = RequestBody.create(MediaType.parse(MULTI_PART), getPassword());
            RequestBody emailBody = RequestBody.create(MediaType.parse(MULTI_PART), getEmail());
            if(pwdBody != null) params.put(Constants.API_PARAMS_KEYS.PASSWORD, pwdBody);
            if(emailBody != null) params.put(Constants.API_PARAMS_KEYS.EMAIL, emailBody);
        }
        else
        {
            RequestBody authIdBody = RequestBody.create(MediaType.parse(MULTI_PART), getAuthId());
            RequestBody profileImgBody = RequestBody.create(MediaType.parse(MULTI_PART), getProfileImg());
            RequestBody snsEmailBody = RequestBody.create(MediaType.parse(MULTI_PART), getSnsEmail());
            if(authIdBody != null) params.put(Constants.API_PARAMS_KEYS.AUTHID, authIdBody);
            if(profileImgBody != null) params.put(Constants.API_PARAMS_KEYS.PROFILE_IMG, profileImgBody);
            if(snsEmailBody != null) params.put(Constants.API_PARAMS_KEYS.SNS_EMAIL, snsEmailBody);
        }

        String gcmToken = BasePreference.getInstance(getActivity()).getValue(BasePreference.GCM_TOKEN, "");
        RequestBody tokenBody = RequestBody.create(MediaType.parse(MULTI_PART), gcmToken);
        params.put(Constants.API_PARAMS_KEYS.GCM_TOKEN, tokenBody);

        DataManager.getInstance(getActivity()).api.userRegist(getActivity(), params, file, new DataInterface.ResponseCallback<UserInfoResultData>() {
            @Override
            public void onSuccess(UserInfoResultData response) {
                stopIndicator();
                Logger.log(Logger.LogState.E, "userRegist = " + Utils.getStringByObject(response));

                String result = response.getResult();
                if(result.equals(Constants.API_FAIL))
                {
                    Toast.makeText(getActivity(), "회원가입 실패!!" + response.getMessage(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), "회원가입 실패!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void userLogin(UserInfoData userInfoData)
    {
        startIndicator("");
        UserInfo.getInstance().clearParams();
        String joinType = userInfoData.getJoinType();

        Logger.log(Logger.LogState.E, "userLogin userInfoData= " + Utils.getStringByObject(userInfoData));

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

        HashMap<String, String> params = UserInfo.getInstance().getLoginParams();

        DataManager.getInstance(getActivity()).api.userLogin(getActivity(), params, new DataInterface.ResponseCallback<UserInfoResultData>() {
            @Override
            public void onSuccess(UserInfoResultData response) {
                stopIndicator();
                Logger.log(Logger.LogState.E, "userLogin = " + Utils.getStringByObject(response));

                String result = response.getResult();
                if(result.equals(Constants.API_FAIL))
                {
                    Toast.makeText(getActivity(), "로그인 실패!!" + response.getMessage(), Toast.LENGTH_LONG).show();
                }
                else
                {
                    UserInfoData userInfoData = response.userData;
                    UserInfo.getInstance().setCurrentUserInfoData(getActivity(), userInfoData);
                    BasePreference.getInstance(getActivity()).put(BasePreference.ID, userInfoData.getId());
                    BasePreference.getInstance(getActivity()).put(BasePreference.JOIN_TYPE, userInfoData.getJoinType());
                    BasePreference.getInstance(getActivity()).put(BasePreference.AUTH_ID, userInfoData.getAuthId());
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
                Toast.makeText(getActivity(), "로그인 실패!!", Toast.LENGTH_LONG).show();
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
                case R.id.toolbar_back :
                    mUpdateListenerCallBack.fragmentBackPressed();
                    break;
                case R.id.btn_regist_end :

                    String nickname = ed_nickname.getText().toString();
                    if(nickname.length() > 0)
                    {
                        startIndicator("");
                        DataManager.getInstance(getActivity()).api.userNickNameCheck(getActivity(), nickname, new DataInterface.ResponseCallback<ResponseData>() {
                            @Override
                            public void onSuccess(ResponseData response) {
                                if(response.getResult().equals(Constants.API_FAIL))
                                {
                                    stopIndicator();
                                    Toast.makeText(getActivity(), "닉네임 체크 실패!!" + response.getMessage(), Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(getActivity(), "닉네임 체크 사용 불가능!!" + flag, Toast.LENGTH_LONG).show();
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
                                Toast.makeText(getActivity(), "userNickNameCheck onError !", Toast.LENGTH_LONG).show();
                                stopIndicator();
                            }
                        });
                    }
                    else
                    {
                        stopIndicator();
                        Toast.makeText(getActivity(), "닉네임을 입력해주세요." + nickname, Toast.LENGTH_LONG).show();
                        return;
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
