package shop.plea.and.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.common.view.ProgressWheel;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.ResponseData;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.data.model.UserInfoResultData;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.data.tool.DataInterface;
import shop.plea.and.data.tool.DataManager;
import shop.plea.and.ui.activity.MainPleaListActivity;
import shop.plea.and.ui.listener.FragmentListener;
import shop.plea.and.ui.listener.UpdateListener;
import shop.plea.and.ui.view.DrawerLayoutHorizontalSupport;

/**
 * Created by kwon7575 on 2017-10-28.
 */

public class SideMenuDrawerFragment extends BaseFragment implements FragmentListener {

    private Listener mListener;
    private DrawerLayoutHorizontalSupport drawerLayout;
    private BaseActivity base;
    private UserInfoData userInfoData;
    @BindView(R.id.side_nickname) TextView side_nickname;
    @BindView(R.id.img_profile) BootstrapCircleThumbnail img_profile;
    @BindView(R.id.side_btn_profile) Button side_btn_profile;
    @BindView(R.id.side_btn_myplea) TextView side_btn_myplea;
    @BindView(R.id.side_btn_follower) TextView side_btn_follower;
    @BindView(R.id.side_btn_foryou) TextView side_btn_foryou;
    @BindView(R.id.side_btn_push) TextView side_btn_push;
    @BindView(R.id.side_btn_email) TextView side_btn_email;
    @BindView(R.id.side_btn_notice) TextView side_btn_notice;
    @BindView(R.id.side_btn_notice_badge) Button side_btn_notice_badge;
    @BindView(R.id.side_btn_terms) TextView side_btn_terms;
    @BindView(R.id.side_btn_privacy) TextView side_btn_privacy;
    @BindView(R.id.side_btn_version) TextView side_btn_version;
    @BindView(R.id.side_btn_update) Button side_btn_update;
    @BindView(R.id.side_btn_api) TextView side_btn_api;
    @BindView(R.id.side_btn_support) TextView side_btn_support;
    @BindView(R.id.side_btn_reset_pwd) TextView side_btn_reset_pwd;
    @BindView(R.id.side_btn_signout) TextView side_btn_signout;
    @BindView(R.id.side_btn_delete_user) TextView side_btn_delete_user;
    @BindView(R.id.side_btn_language) Button side_btn_language;


    public static SideMenuDrawerFragment newInstance() {

        SideMenuDrawerFragment sideMenuDrawerFragment = new SideMenuDrawerFragment();
        Bundle bundle = new Bundle();
        sideMenuDrawerFragment.setArguments(bundle);

        return sideMenuDrawerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_sidemenu_drawer, container, false);

        base = (BaseActivity) container.getContext();
        mListener = new Listener();
        init(container);

        if(savedInstanceState == null) initScreen();

        return mView;
    }

    public void initScreen() {

        userInfoData = UserInfo.getInstance().getCurrentUserInfoData(mContext);

        side_nickname.setText(userInfoData.getNickname());
        side_btn_profile.setOnClickListener(mListener);
        side_btn_myplea.setOnClickListener(mListener);
        side_btn_follower.setOnClickListener(mListener);
        side_btn_foryou.setOnClickListener(mListener);
        side_btn_push.setOnClickListener(mListener);
        side_btn_email.setOnClickListener(mListener);
        side_btn_notice.setOnClickListener(mListener);
        side_btn_notice_badge.setOnClickListener(mListener);
        side_btn_terms.setOnClickListener(mListener);
        side_btn_privacy.setOnClickListener(mListener);
        side_btn_version.setOnClickListener(mListener);
        side_btn_update.setOnClickListener(mListener);
        side_btn_api.setOnClickListener(mListener);
        side_btn_support.setOnClickListener(mListener);
        side_btn_reset_pwd.setOnClickListener(mListener);
        side_btn_signout.setOnClickListener(mListener);
        side_btn_delete_user.setOnClickListener(mListener);
        side_btn_language.setOnClickListener(mListener);

        side_btn_language.setText((userInfoData.getLocale().equals("en") ? "English" : "Korean"));

        setNoticeCnt();

        Glide.with(base)
                .load(userInfoData.getProfileImg())
                .into(img_profile);
    }

    public void setDrawerLayout(DrawerLayoutHorizontalSupport drawerLayout)
    {
        this.drawerLayout = drawerLayout;
        this.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        });
    }

    private void setNoticeCnt()
    {


        final UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(getActivity());
        String id = userInfoData.getId();

        DataManager.getInstance(getActivity()).api.getNoticeCnt(getActivity(), id, new DataInterface.ResponseCallback<ResponseData>() {
            @Override
            public void onSuccess(ResponseData response) {

                Logger.log(Logger.LogState.E, "setNoticeCnt = " + Utils.getStringByObject(response));
                if(response.getResult().equals(Constants.API_SUCCESS))
                {
                    side_btn_notice_badge.setText(String.valueOf(response.count));
                }
                else
                {
                    Logger.log(Logger.LogState.E, "setNoticeCnt = " + Utils.getStringByObject(response));
                }
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "updateUser 실패!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUser(final String updateLocale)
    {
        startIndicator("");
        final UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(getActivity());
        UserInfo.getInstance().clearParams();
        String id = userInfoData.getId();

        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.LOCALE, updateLocale);

        HashMap<String, String> params = UserInfo.getInstance().getLoginParams();

        DataManager.getInstance(getActivity()).api.userUpdate(getActivity(), id, params, new DataInterface.ResponseCallback<UserInfoResultData>() {
            @Override
            public void onSuccess(UserInfoResultData response) {
                stopIndicator();
                Logger.log(Logger.LogState.E, "updateUser = " + Utils.getStringByObject(response));
                side_btn_language.setText((updateLocale.equals("en") ? "English" : "Korean"));
                userInfoData.setLocale(updateLocale);
                setLanguage((updateLocale.equals("en") ? Locale.ENGLISH : Locale.KOREAN));
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "setNoticeCnt 실패!!", Toast.LENGTH_LONG).show();
                stopIndicator();
            }
        });
    }

    private void setLanguage(Locale locale)
    {
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;


        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());
    }

    private void signOut()
    {
        BasePreference.getInstance(getActivity()).removeAll();
        mUpdateListenerCallBack.addFragment(Constants.FRAGMENT_MENUID.LOGIN);
    }

    private void deleteUser()
    {
        startIndicator("");
        final UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(getActivity());
        String id = userInfoData.getId();

        DataManager.getInstance(getActivity()).api.userDelete(getActivity(), id, new DataInterface.ResponseCallback<ResponseData>() {
            @Override
            public void onSuccess(ResponseData response) {
                stopIndicator();
                Logger.log(Logger.LogState.E, "deleteUser = " + Utils.getStringByObject(response));
                BasePreference.getInstance(getActivity()).removeAll();
                mUpdateListenerCallBack.addFragment(Constants.FRAGMENT_MENUID.LOGIN);
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "setNoticeCnt 실패!!", Toast.LENGTH_LONG).show();
                stopIndicator();
            }
        });
    }

    private void selectLanguageDialog()
    {
        final CharSequence[] languages = {"English", "Korean"};
        final AlertDialog.Builder radioAlert = new AlertDialog.Builder(getActivity());
        radioAlert.setIcon(R.mipmap.ic_launcher);
        radioAlert.setTitle("Select a Language");

        UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(getActivity());
        final String locale = userInfoData.getLocale();
        int index = 0;
        if(locale.equals("ko"))
        {
            index = 1;
        }
        radioAlert.setSingleChoiceItems(languages, index, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                String updateLocale = (languages[item].equals("English")) ? "en" : "ko";

                updateUser(updateLocale);

                dialog.dismiss();
            }

        });
        AlertDialog alert = radioAlert.create();
        alert.show();
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.side_btn_profile :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_myplea :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_follower :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_foryou :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_push :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_email :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_notice :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_notice_badge :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_terms :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_privacy :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_version :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_update :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_api :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_support :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_reset_pwd :
                    Toast.makeText(getActivity(), "주소 알려주세요.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.side_btn_signout :
                    signOut();
                    break;
                case R.id.side_btn_delete_user :
                    deleteUser();
                    break;
                case R.id.side_btn_language :
                    selectLanguageDialog();
                    break;
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
