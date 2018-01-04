package shop.plea.and.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
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
import shop.plea.and.ui.activity.LoginActivity;
import shop.plea.and.ui.activity.MainPleaListActivity;
import shop.plea.and.ui.listener.FragmentListener;
import shop.plea.and.ui.view.CustomFontBtn;
import shop.plea.and.ui.view.CustomFontTextView;
import shop.plea.and.ui.view.DrawerLayoutHorizontalSupport;

/**
 * Created by kwon7575 on 2017-10-28.
 */

public class SideMenuDrawerFragment extends BaseFragment implements FragmentListener {

    private Listener mListener;
    private DrawerLayoutHorizontalSupport drawerLayout;
    private BaseActivity base;
    private UserInfoData userInfoData;
    private MainPleaListActivity.sideMenuCallback menuCallback;

    @BindView(R.id.side_nickname) CustomFontTextView side_nickname;
    @BindView(R.id.img_profile) BootstrapCircleThumbnail img_profile;
    @BindView(R.id.side_btn_profile) CustomFontBtn side_btn_profile;
    @BindView(R.id.side_btn_myplea) LinearLayout side_btn_myplea;
    @BindView(R.id.side_btn_follower) LinearLayout side_btn_follower;
    @BindView(R.id.side_btn_foryou) LinearLayout side_btn_foryou;
    @BindView(R.id.side_btn_push_email) RelativeLayout side_btn_push_email;

    @BindView(R.id.side_btn_notice) CustomFontTextView side_btn_notice;
    @BindView(R.id.side_btn_notice_badge) CustomFontBtn side_btn_notice_badge;
    @BindView(R.id.side_btn_terms) CustomFontTextView side_btn_terms;
    @BindView(R.id.side_btn_privacy) CustomFontTextView side_btn_privacy;
    @BindView(R.id.side_btn_version) CustomFontTextView side_btn_version;
    @BindView(R.id.side_btn_update) CustomFontBtn side_btn_update;

    @BindView(R.id.side_btn_support) CustomFontTextView side_btn_support;
    @BindView(R.id.side_btn_reset_pwd) CustomFontTextView side_btn_reset_pwd;
    @BindView(R.id.side_btn_signout) CustomFontTextView side_btn_signout;
    @BindView(R.id.side_btn_delete_user) CustomFontTextView side_btn_delete_user;
    @BindView(R.id.side_btn_language) CustomFontBtn side_btn_language;
    @BindView(R.id.side_btn_follower_following) RelativeLayout side_btn_follower_following;


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
        side_btn_push_email.setOnClickListener(mListener);
        side_btn_notice.setOnClickListener(mListener);
        side_btn_notice_badge.setOnClickListener(mListener);
        side_btn_terms.setOnClickListener(mListener);
        side_btn_privacy.setOnClickListener(mListener);
        side_btn_version.setOnClickListener(mListener);
        side_btn_update.setOnClickListener(mListener);
        side_btn_support.setOnClickListener(mListener);
        side_btn_reset_pwd.setOnClickListener(mListener);
        side_btn_signout.setOnClickListener(mListener);
        side_btn_delete_user.setOnClickListener(mListener);
        side_btn_language.setOnClickListener(mListener);
        side_btn_follower_following.setOnClickListener(mListener);

        side_btn_version.setText(String.format(getString(R.string.menu_version), "1.0.0"));

        side_btn_language.setText((userInfoData.getLocale().equals("en") ? "English" : "Korean"));

        setNoticeCnt();

        int genderImg = (userInfoData.getGender().equals("M")) ? R.drawable.profile_man : R.drawable.profile_woman;

        Glide.with(base)
                .load(userInfoData.getProfileImg())
                .apply(new RequestOptions().override(100, 100).placeholder(genderImg).error(genderImg))
                .into(img_profile);

    }

    public void setUserData(UserInfoData userInfoData )
    {
        int genderImg = (userInfoData.getGender().equals("M")) ? R.drawable.profile_man : R.drawable.profile_woman;
        Glide.with(base)
                .load(userInfoData.getProfileImg())
                .apply(new RequestOptions().override(100, 100).placeholder(genderImg).error(genderImg))
                .into(img_profile);

        side_nickname.setText(userInfoData.getNickname());
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

    public void setNoticeCnt()
    {
        final UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(getActivity());
        String id = userInfoData.getId();

        DataManager.getInstance(getActivity()).api.getNoticeCnt(getActivity(), id, new DataInterface.ResponseCallback<ResponseData>() {
            @Override
            public void onSuccess(ResponseData response) {

                Logger.log(Logger.LogState.E, "setNoticeCnt = " + Utils.getStringByObject(response));
                if(response.getResult().equals(Constants.API_SUCCESS))
                {
                    if(response.count == 0)
                        side_btn_notice_badge.setVisibility(View.INVISIBLE);
                    else
                    {
                        side_btn_notice_badge.setVisibility(View.VISIBLE);
                        side_btn_notice_badge.setText(String.valueOf(response.count));
                    }

                    setVersion();
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

    private void setVersion()
    {
        startIndicator("");
        DataManager.getInstance(getActivity()).api.getVersion(getActivity(), new DataInterface.ResponseCallback<ResponseData>() {
            @Override
            public void onSuccess(ResponseData response) {
                stopIndicator();
                Logger.log(Logger.LogState.E, "setVersion = " + Utils.getStringByObject(response));
                try
                {
                    String androidVersion = (response.getData().has("androidVersion")) ? response.getData().getString("androidVersion") : "1.0.0";
                    side_btn_version.setText(String.format(getString(R.string.menu_version), androidVersion));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "updateUser 실패!!", Toast.LENGTH_LONG).show();
                stopIndicator();
            }
        });
    }

    private void updateUser(final String updateLocale)
    {
        startIndicator("");
        final UserInfoData userInfoData = BasePreference.getInstance(getActivity()).getObject(BasePreference.USERINFO_DATA, UserInfoData.class);
        UserInfo.getInstance().clearParams();
        String id = userInfoData.getId();
        Logger.log(Logger.LogState.E, "updateUser id = " + Utils.getStringByObject(id));
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.LOCALE, updateLocale);
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.ID, id);


        HashMap<String, String> params = UserInfo.getInstance().getLoginParams();
        Logger.log(Logger.LogState.E, "updateUser params = " + Utils.getStringByObject(params));

        DataManager.getInstance(getActivity()).api.userUpdate(getActivity(), id, params, new DataInterface.ResponseCallback<UserInfoResultData>() {
            @Override
            public void onSuccess(UserInfoResultData response) {
                stopIndicator();
                Logger.log(Logger.LogState.E, "updateUser = " + Utils.getStringByObject(response));
                UserInfoData userInfoData = response.userData;
                UserInfo.getInstance().setCurrentUserInfoData(getActivity(), userInfoData);
                BasePreference.getInstance(getActivity()).putObject(BasePreference.USERINFO_DATA, userInfoData);
                side_btn_language.setText((updateLocale.equals("en") ? "English" : "Korean"));
                BasePreference.getInstance(getActivity()).put(BasePreference.LOCALE, updateLocale);
                setLanguage((updateLocale.equals("en") ? Locale.ENGLISH : Locale.KOREAN));

                base.finish();
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "updateUser 실패!!", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(R.string.app_name)).setMessage(getString(R.string.logout)).setCancelable(false).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BasePreference.getInstance(getActivity()).removeAll();
                Logger.log(Logger.LogState.E, "signOut!!!");
                IntentData indata = new IntentData();
                indata.aniType = Constants.VIEW_ANIMATION.ANI_FLIP;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void deleteUser()
    {

        startIndicator("");
        final UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(getActivity());

        String id = userInfoData.getId();

        Logger.log(Logger.LogState.E, "deleteUser  id= " + id);

        UserInfo.getInstance().clearParams();
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.UID, id);

        HashMap<String, String> params = UserInfo.getInstance().getLoginParams();

        DataManager.getInstance(getActivity()).api.userDelete(getActivity(), id, params, new DataInterface.ResponseCallback<ResponseData>() {
            @Override
            public void onSuccess(ResponseData response) {
                stopIndicator();
                Logger.log(Logger.LogState.E, "deleteUser = " + Utils.getStringByObject(response));
                BasePreference.getInstance(getActivity()).removeAll();
                IntentData indata = new IntentData();
                indata.aniType = Constants.VIEW_ANIMATION.ANI_FLIP;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "deleteUser 실패!!", Toast.LENGTH_LONG).show();
                stopIndicator();
            }
        });
    }

    private void selectLanguageDialog()
    {
        final CharSequence[] languages = {"English", "Korean"};
        final AlertDialog.Builder radioAlert = new AlertDialog.Builder(getActivity());
        radioAlert.setIcon(R.mipmap.ic_launcher);
        radioAlert.setTitle(getString(R.string.select_locale));

        UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(getActivity());
        final String locale = userInfoData.getLocale();
        int index = 0;
        if(locale.equals("ko"))
        {
            index = 1;
        }
        radioAlert.setSingleChoiceItems(languages, index, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                String updateLocale = (languages[item].toString().toLowerCase().contains("eng")) ? "en" : "ko";

                updateUser(updateLocale);

                dialog.dismiss();
            }

        });
        AlertDialog alert = radioAlert.create();
        alert.show();
    }

    public void setMenuCallback(MainPleaListActivity.sideMenuCallback listener)
    {
        menuCallback = listener;
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String uid = userInfoData.getId();
            String url;
            String locale = userInfoData.getLocale();
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

            switch (v.getId())
            {
                case R.id.side_btn_profile :
                    url = String.format(Constants.MENU_LINKS.PROFILE, uid, uid);
                    menuCallback.onReceive(url);
                    break;
                case R.id.side_btn_myplea :
                    url = String.format(Constants.MENU_LINKS.MY_PLEA, uid);
                    menuCallback.onReceive(url);
                    break;
                case R.id.side_btn_follower :
                    url = String.format(Constants.MENU_LINKS.FREIEND_NEWS, uid);
                    menuCallback.onReceive(url);
                    break;
                case R.id.side_btn_foryou :
                    url = String.format(Constants.MENU_LINKS.RECOMMEND_PLEA, uid);
                    menuCallback.onReceive(url);
                    break;
                case R.id.side_btn_push_email :
                    url = String.format(Constants.MENU_LINKS.PUSH_EMAIL, uid);
                    menuCallback.onReceive(url);
                    break;

                case R.id.side_btn_notice :
                case R.id.side_btn_notice_badge :
                    url = String.format(Constants.MENU_LINKS.NOTICE, uid);
                    menuCallback.onReceive(url);
                    break;
                case R.id.side_btn_terms :
                    url = String.format(Constants.MENU_LINKS.TERMS, locale);
                    menuCallback.onReceive(url);
                    break;
                case R.id.side_btn_privacy :
                    url = String.format(Constants.MENU_LINKS.POLICY, locale);
                    menuCallback.onReceive(url);
                    break;
                case R.id.side_btn_update :
                    setVersion();
                    break;
                case R.id.side_btn_support :

                    /*
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "pleashop@gmail.com" });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    */
                    dialog.setTitle(R.string.app_name).setMessage(getString(R.string.preparing)).setPositiveButton(getString(R.string.yes), null).create().show();
                    break;
                case R.id.side_btn_reset_pwd :

                    String joinType = userInfoData.getJoinType();
                    if(joinType.equals(Constants.LOGIN_TYPE.EMAIL))
                    {
                        url = String.format(Constants.MENU_LINKS.RESET_PASSWORD, uid);
                        menuCallback.onReceive(url);
                    }
                    else
                    {
                        dialog.setTitle(R.string.app_name).setMessage(getString(R.string.join_type_check)).setPositiveButton(getString(R.string.yes), null).create().show();
                    }
                    break;
                case R.id.side_btn_signout :
                    signOut();
                    break;
                case R.id.side_btn_delete_user :

                    dialog.setTitle(getString(R.string.app_name)).setMessage(getString(R.string.delete_user)).setCancelable(false).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser();
                        }
                    }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().setCanceledOnTouchOutside(false);
                    dialog.show();

                    break;
                case R.id.side_btn_language :

                    dialog.setTitle(getString(R.string.app_name)).setMessage(R.string.reset_language).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            selectLanguageDialog();
                        }
                    }).setNegativeButton(R.string.cancel, null).create().show();

                    break;
                case R.id.side_btn_follower_following :
                    url = String.format(Constants.MENU_LINKS.FRIEND_MGR, uid, uid);
                    menuCallback.onReceive(url);
                    break;
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
