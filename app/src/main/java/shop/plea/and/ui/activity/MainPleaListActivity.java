package shop.plea.and.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import shop.plea.and.R;
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
import shop.plea.and.ui.fragment.SideMenuDrawerFragment;
import shop.plea.and.ui.listener.FragmentListener;
import shop.plea.and.ui.view.CustomFontEditView;
import shop.plea.and.ui.view.CustomFontTextView;
import shop.plea.and.ui.view.CustomWebView;
import shop.plea.and.ui.view.DrawerLayoutHorizontalSupport;

/**
 * Created by kwon7575 on 2017-10-27.
 * 언어 변경 http://devdeeds.com/android-change-language-at-runtime/
 */

public class MainPleaListActivity extends PleaActivity{

    @BindView(R.id.hellow_area) RelativeLayout hellow_area;
    @BindView(R.id.toolbar_header) Toolbar toolbar_header;
    @BindView(R.id.drawerLayout) DrawerLayoutHorizontalSupport mDrawerLayout;
    @BindView(R.id.txt_nickname) CustomFontTextView txt_nickname;
    @BindView(R.id.main_profile) BootstrapCircleThumbnail main_profile;
    @BindView(R.id.ticker_header) LinearLayout ticker_header;
    @BindView(R.id.ticker_notice) LinearLayout ticker_notice;
    @BindView(R.id.ticker_like) LinearLayout ticker_like;
    @BindView(R.id.ticker_follow) LinearLayout ticker_follow;
    @BindView(R.id.noticeCnt) CustomFontTextView noticeCnt;
    @BindView(R.id.likeCnt) CustomFontTextView likeCnt;
    @BindView(R.id.followCnt) CustomFontTextView followCnt;
    @BindView(R.id.toolbar_title) CustomFontTextView toolbar_title;
    @BindView(R.id.btn_plea) ImageView btn_plea;

    private final static int INTENT_CALL_PROFILE_GALLERY = 3002;
    public final static int INTENT_CALL_PLEA_COMPLECT = 3003;
    private List<MainPleaListActivity.FileInfo> fileInfoList = new ArrayList<>();
    private HashMap<String, JSONObject> tickerMap = new HashMap<>();
    public CustomWebView customWebView;
    private Listener mListener = new Listener();
    private Fragment drawer_Fragment;
    private JSONObject mToobarData;

    private headerJsonCallback mHeaderJsonCallback = new headerJsonCallback() {
        @Override
        public void onReceive(JSONObject jsonObject) {
            mToobarData = jsonObject;
            initToobar(jsonObject);
        }
    };

    private sideMenuCallback mSideMenuCallback = new sideMenuCallback() {
        @Override
        public void onReceive(String url) {
            closeDrawer();
            customWebView.initContentView(url);
        }
    };

    private pleaCallBack mPleaCallBack = new pleaCallBack() {
        @Override
        public void onPlea(String url) {
            customWebView.initContentView(url);
        }
    };

    private userUpdateCallBack mUserUpdateCallBack = new userUpdateCallBack() {


        @Override
        public void onUserUpdate(JSONObject jsonObject) {

            if(jsonObject.has("type"))
            {
                try
                {
                    if (jsonObject.getString("type").equals("updateUser"))
                    {
                        userLogin();
                    }
                    else if (jsonObject.getString("type").equals("updateNotice"))
                    {
                        ((SideMenuDrawerFragment)drawer_Fragment).setNoticeCnt();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }
    };

    public interface headerJsonCallback{
        void onReceive(JSONObject jsonObject);
    }


    public interface sideMenuCallback{
        void onReceive(String url);
    }


    public interface pleaCallBack{
        void onPlea(String url);
    }

    public interface userUpdateCallBack{
        void onUserUpdate(JSONObject jsonObject);
    }

    Handler handler = new Handler();
    Runnable runMain = new Runnable() {
        @Override
        public void run() {
                hellow_area.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == INTENT_CALL_PROFILE_GALLERY) { // 킷캣.
                startIndicator("");
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();

                File file = Utils.getAlbum(this, result);
                if(file == null)
                {
                    stopIndicator();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainPleaListActivity.this);
                    dialog.setTitle(R.string.app_name).setMessage(getString(R.string.gallery_error)).setPositiveButton(getString(R.string.yes), null).create().show();
                }
                else
                {
                    fileInfoList.clear();
                    fileInfoList.add(new MainPleaListActivity.FileInfo(result, file));

                    File fileImg = (fileInfoList.size() > 0) ? fileInfoList.get(0).file : null;

                    String uid = BasePreference.getInstance(this).getValue(BasePreference.ID, "");

                    DataManager.getInstance(this).api.uploadProfile(this, uid, fileImg, new DataInterface.ResponseCallback<ResponseData>() {
                        @Override
                        public void onSuccess(ResponseData response) {
                            stopIndicator();
                            customWebView.initContentView("javascript:setProfileImg('"+response.getProfileImg()+"');");
                        }

                        @Override
                        public void onError() {

                            stopIndicator();
                        }
                    });
                }

                return;
            }
        }
        else if(resultCode == INTENT_CALL_PLEA_COMPLECT)
        {
            String uid = BasePreference.getInstance(this).getValue(BasePreference.ID, "");
            customWebView.mView.clearHistory();
            customWebView.initContentView(String.format(Constants.MAIN_URL, uid));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_plealist);

        if(inData.isRegist)
        {
            hellow_area.setVisibility(View.VISIBLE);
            handler.postDelayed(runMain, 2500);
        }

        drawer_Fragment = SideMenuDrawerFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.drawer_content, drawer_Fragment, "drawer_fragment");
        ft.commitAllowingStateLoss();

        ((SideMenuDrawerFragment)drawer_Fragment).setDrawerLayout(mDrawerLayout);
        ((SideMenuDrawerFragment)drawer_Fragment).setMenuCallback(mSideMenuCallback);

        InAppWebView.setPleaCallback(mPleaCallBack);

        initScreen();
        init();
    }


    public void initToobar(JSONObject jsonObject)
    {
        try
        {
            if(jsonObject == null)
            {
                toolbar_header.setVisibility(View.GONE);
                ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_alert)).setImageResource(R.drawable.top_icon_notice);
                toolbar_header.findViewById(R.id.btn_toolbar_alert).setTag("off");
                ticker_header.setVisibility(View.GONE);
                toolbar_header.findViewById(R.id.btn_toolbar_alert).setVisibility(View.GONE);
            }
            else
            {

                toolbar_header.setVisibility(View.VISIBLE);
                String menuBt = (jsonObject.has("menuBt")) ? jsonObject.getString("menuBt") : "N";
                String searchBt = (jsonObject.has("searchBt")) ? jsonObject.getString("searchBt") : "N";
                String alertBt = (jsonObject.has("alertBt")) ? jsonObject.getString("alertBt") : "N";
                String preBt = (jsonObject.has("preBt")) ? jsonObject.getString("preBt") : "N";
                String title = (jsonObject.has("title")) ? jsonObject.getString("title") : "N";
                String modifyBt = (jsonObject.has("modifyBt")) ? jsonObject.getString("modifyBt") : "N";
                String likeBt = (jsonObject.has("likeBt")) ? jsonObject.getString("likeBt") : "N";
                String likeyn = (jsonObject.has("likeyn")) ? jsonObject.getString("likeyn") : "N";
                String searchBox = (jsonObject.has("searchBox")) ? jsonObject.getString("searchBox") : "N";
                String homeBt = (jsonObject.has("homeBt")) ? jsonObject.getString("homeBt") : "N";
                String logoutBt = (jsonObject.has("logoutBt")) ? jsonObject.getString("logoutBt") : "N";

                if(alertBt.equals("Y"))
                {
                    JSONArray tickers = (jsonObject.has("tickers")) ? jsonObject.getJSONArray("tickers") : null;
                    if(tickers != null)
                    {
                        for(int i = 0; i < tickers.length(); i++)
                        {
                            JSONObject ticker = tickers.getJSONObject(i);
                            String name = ticker.getString("name");
                            String cnt = ticker.getString("cnt");
                            if(name.equals("noti"))
                                noticeCnt.setText(String.valueOf(cnt));
                            if(name.equals("like"))
                                likeCnt.setText(String.valueOf(cnt));
                            if(name.equals("follow"))
                                followCnt.setText(String.valueOf(cnt));

                            tickerMap.put(name, ticker);

                        }

                        if(!noticeCnt.getText().toString().equals("0") || !likeCnt.getText().toString().equals("0") || !followCnt.getText().toString().equals("0")) {
                            ticker_header.setVisibility(View.VISIBLE);
                            ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_alert)).setImageResource(R.drawable.top_icon_notice_on);
                            ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_alert)).setTag("on");
                        }
                    }

                    if(tickers.length() > 0)
                    {
                        toolbar_header.findViewById(R.id.btn_toolbar_alert).setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        toolbar_header.findViewById(R.id.btn_toolbar_alert).setVisibility(View.GONE);
                    }
                }
                else
                {
                    ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_alert)).setImageResource(R.drawable.top_icon_notice);
                    ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_alert)).setTag("off");
                    ticker_header.setVisibility(View.GONE);
                    toolbar_header.findViewById(R.id.btn_toolbar_alert).setVisibility(View.GONE);
                }


                if(menuBt.equals("Y"))
                    toolbar_header.findViewById(R.id.btn_menu).setVisibility(View.VISIBLE);
                else
                    toolbar_header.findViewById(R.id.btn_menu).setVisibility(View.GONE);

                if(title.equals("N"))
                {
                    toolbar_title.setVisibility(View.GONE);
                    toolbar_header.findViewById(R.id.btn_toolbar_img).setVisibility(View.VISIBLE);
                }
                else
                {
                    toolbar_title.setVisibility(View.VISIBLE);
                    toolbar_title.setText(Utils.decode(title, "UTF-8").replace("&amp;", "&"));
                    toolbar_header.findViewById(R.id.btn_toolbar_img).setVisibility(View.GONE);
                }

                if(searchBt.equals("Y"))
                    toolbar_header.findViewById(R.id.btn_toolbar_search).setVisibility(View.VISIBLE);
                else
                    toolbar_header.findViewById(R.id.btn_toolbar_search).setVisibility(View.GONE);

                if(preBt.equals("Y"))
                    toolbar_header.findViewById(R.id.toolbar_back).setVisibility(View.VISIBLE);
                else
                    toolbar_header.findViewById(R.id.toolbar_back).setVisibility(View.GONE);

                if(menuBt.equals("Y") && preBt.equals("Y"))
                {
                    toolbar_header.findViewById(R.id.btn_menu_profile).setVisibility(View.VISIBLE);
                    toolbar_header.findViewById(R.id.toolbar_back_profile).setVisibility(View.VISIBLE);
                    toolbar_header.findViewById(R.id.toolbar_back).setVisibility(View.GONE);
                    toolbar_header.findViewById(R.id.btn_menu).setVisibility(View.GONE);
                }
                else
                {
                    toolbar_header.findViewById(R.id.btn_menu_profile).setVisibility(View.GONE);
                    toolbar_header.findViewById(R.id.toolbar_back_profile).setVisibility(View.GONE);
                }

                if(modifyBt.equals("Y"))
                    toolbar_header.findViewById(R.id.btn_toolbar_modify).setVisibility(View.VISIBLE);
                else
                    toolbar_header.findViewById(R.id.btn_toolbar_modify).setVisibility(View.GONE);


                if(homeBt.equals("Y"))
                    toolbar_header.findViewById(R.id.btn_toolbar_home).setVisibility(View.VISIBLE);
                else
                    toolbar_header.findViewById(R.id.btn_toolbar_home).setVisibility(View.GONE);

                if(logoutBt.equals("Y"))
                    toolbar_header.findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
                else
                    toolbar_header.findViewById(R.id.btn_logout).setVisibility(View.GONE);

                if(likeBt.equals("Y"))
                {
                    toolbar_header.findViewById(R.id.btn_toolbar_like).setVisibility(View.VISIBLE);

                    if(likeyn.equals("Y"))
                        ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_like)).setImageResource(R.drawable.top_icon_follow_on);
                    else
                        ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_like)).setImageResource(R.drawable.top_icon_follow);
                }
                else
                {
                    toolbar_header.findViewById(R.id.btn_toolbar_like).setVisibility(View.GONE);
                    ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_like)).setImageResource(R.drawable.top_icon_follow);
                }

                if(searchBox.equals("Y"))
                {
                    toolbar_header.findViewById(R.id.btn_toolbar_img).setVisibility(View.GONE);
                    ((CustomFontEditView)toolbar_header.findViewById(R.id.btn_toolbar_searchbox)).setVisibility(View.VISIBLE);

                    ((CustomFontEditView)toolbar_header.findViewById(R.id.btn_toolbar_searchbox)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                            switch (actionId) {

                                case EditorInfo.IME_ACTION_SEARCH:
                                    searchAction();
                                    break;
                                default:
                                    return false;
                            }
                            return true;
                        }
                    });

                    if(customWebView.mView.getUrl().contains("tag"))
                    {
                        String url = customWebView.mView.getUrl();
                        String[] urls = url.split("/");
                        int urlLen = urls.length;
                        if(urlLen > 4)
                        {
                            String tag = Utils.decode(urls[5], "UTF-8");
                            tag = tag.substring(0, tag.indexOf("?"));

                            ((CustomFontEditView)toolbar_header.findViewById(R.id.btn_toolbar_searchbox)).setText(tag);
                        }
                    }
                }
                else
                {
                    ((CustomFontEditView)toolbar_header.findViewById(R.id.btn_toolbar_searchbox)).setVisibility(View.GONE);
                    ((CustomFontEditView)toolbar_header.findViewById(R.id.btn_toolbar_searchbox)).setText("");
                }

                toolbar_header.setBackgroundColor(getResources().getColor(R.color.colorSubHeader));
                Utils.changeStatusColor(this, R.color.colorSubHeader);

            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public void initScreen()
    {
        setSupportActionBar(toolbar_header);
        toolbar_header.findViewById(R.id.btn_menu).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_menu_profile).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_toolbar_search).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_toolbar_alert).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.toolbar_back).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.toolbar_back_profile).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_toolbar_img).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_toolbar_home).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_toolbar_like).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_logout).setOnClickListener(mListener);
        btn_plea.setOnClickListener(mListener);

        ticker_notice.setOnClickListener(mListener);
        ticker_like.setOnClickListener(mListener);
        ticker_follow.setOnClickListener(mListener);


        UserInfoData userInfo = UserInfo.getInstance().getCurrentUserInfoData(this);
        txt_nickname.setText(String.format(getString(R.string.user_resist_finish), userInfo.getNickname()));


        int genderImg = (userInfo.getGender().equals("M")) ? R.drawable.profile_man : R.drawable.profile_woman;

        Glide.with(this)
                .load(userInfo.getProfileImg())
                .apply(new RequestOptions().override(100, 100).placeholder(genderImg).error(genderImg))
                .into(main_profile);

    }

    private void init()
    {
        customWebView = new CustomWebView(this, this.findViewById(R.id.content).getRootView(), btn_plea);
        customWebView.setWebHeaderCallback(mHeaderJsonCallback);
        customWebView.setUserUpdateCallback(mUserUpdateCallBack);

        UserInfoData userInfo = UserInfo.getInstance().getCurrentUserInfoData(this);

        String status = userInfo.getStatus();
        String uid = userInfo.getId();

        if(status == null || uid == null)
        {
            customWebView.initContentView(inData.link);
        }
        else
        {
            if(status.equals("T"))
                customWebView.initContentView(String.format(Constants.MENU_LINKS.BLOCK, uid));
            else
                customWebView.initContentView(inData.link);
        }
        //cust
        //
        // omWebView.initContentView("http://www.favinet.co.kr/deeplink_test.html");
    }

    private void userLogin()
    {
        UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(this);
        String joinType = userInfoData.getJoinType();
        String locale = BasePreference.getInstance(this).getValue(BasePreference.LOCALE, null);

        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.JOIN_TYPE, joinType);
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.GCM_TOKEN, userInfoData.getDeviceToken());
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.DEVICE_TYPE, "android");
        UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.LOCALE, locale);

        if(joinType.equals(Constants.LOGIN_TYPE.EMAIL))
        {
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.EMAIL, userInfoData.getEmail());
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.PASSWORD, userInfoData.getPassword());
        }
        else
        {
            UserInfo.getInstance().setParams(Constants.API_PARAMS_KEYS.AUTHID, userInfoData.getAuthId());
        }


        HashMap<String, String> params = UserInfo.getInstance().getLoginParams();
        DataManager.getInstance(this).api.userLogin(this, params, new DataInterface.ResponseCallback<UserInfoResultData>() {
            @Override
            public void onSuccess(UserInfoResultData response) {
                stopIndicator();
                String result = response.getResult();
                if(result.equals(Constants.API_FAIL))
                {
                    BasePreference.getInstance(MainPleaListActivity.this).putObject(BasePreference.USERINFO_DATA, null);
                    UserInfo.getInstance().setCurrentUserInfoData(getApplicationContext(), new UserInfoData());
                }
                else
                {
                    UserInfoData userInfoData = response.userData;
                    UserInfo.getInstance().setCurrentUserInfoData(getApplicationContext(), userInfoData);
                    BasePreference.getInstance(getApplicationContext()).put(BasePreference.LOCALE, userInfoData.getLocale());
                    BasePreference.getInstance(getApplicationContext()).put(BasePreference.ID, userInfoData.getId());
                    BasePreference.getInstance(getApplicationContext()).put(BasePreference.JOIN_TYPE, userInfoData.getJoinType());
                    BasePreference.getInstance(getApplicationContext()).put(BasePreference.AUTH_ID, userInfoData.getAuthId());
                    BasePreference.getInstance(getApplicationContext()).putObject(BasePreference.USERINFO_DATA, userInfoData);

                    ((SideMenuDrawerFragment)drawer_Fragment).setUserData(userInfoData);
                    customWebView.initContentView("javascript:userUpdateFinish();");
                }
            }

            @Override
            public void onError() {
                stopIndicator();
            }
        });
    }

    @Override
    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void closeDrawer() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runMain);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }

            if(customWebView.canBack())
            {
                customWebView.goBack();
                return true;
            }

            try {
                FragmentListener fragmentListener = (FragmentListener) curFragment;
                if(fragmentListener != null)
                {
                    boolean check = fragmentListener.onBackPressed();

                    if(check) return check;
                }
            }
            catch (Exception ex)
            {

            }

        }

        return super.onKeyDown(keyCode, event);
    }

    private void backAction()
    {
        String preAction = null;
        String target = null;
        try {
            preAction = (mToobarData.has("preAction")) ? mToobarData.getString("preAction") : "N";
            target = (mToobarData.has("target")) ? Utils.decode(mToobarData.getString("target"), "UTF-8") : "N";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(preAction.equals("T"))
        {
            customWebView.initContentView(target);
        }
        else if(preAction.equals("B"))
        {
            if(customWebView.mView.canGoBack())
                customWebView.goBack();
            else
            {
                UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(this);
                String url = String.format(Constants.MAIN_URL, userInfoData.getId());
                customWebView.initContentView(url);
            }
        }
    }

    private void searchAction()
    {
        String id  = BasePreference.getInstance(MainPleaListActivity.this).getValue(BasePreference.ID, "");
        if(id.equals(""))
        {
            UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(MainPleaListActivity.this);
            id =   userInfoData.getId();
        }

        CustomFontEditView searchBox = (CustomFontEditView)toolbar_header.findViewById(R.id.btn_toolbar_searchbox);

        boolean isViewSearchBox = (searchBox.getVisibility() == View.VISIBLE) ? true : false;

        if(isViewSearchBox)
        {
            String keyword = ((CustomFontEditView)toolbar_header.findViewById(R.id.btn_toolbar_searchbox)).getText().toString();
            String searchAction = "javascript:searchAction('"+keyword+"');";
            customWebView.initContentView(searchAction);
        }
        else
        {
            customWebView.initContentView(String.format(Constants.MENU_LINKS.SEARCH_MAIN, id));
        }
    }

    private void setTickerUrl(int  index)
    {
        String tickerUrl = "";

        ImageButton alertBtn = (ImageButton) toolbar_header.findViewById(R.id.btn_toolbar_alert);
        alertBtn.setImageResource(R.drawable.top_icon_notice);
        alertBtn.setTag("off");
        ticker_header.setVisibility(View.GONE);

        try
        {
            if(index == 0)
                tickerUrl = URLDecoder.decode(tickerMap.get("noti").getString("target") , "EUC-KR" );
            else if(index == 1)
                tickerUrl = URLDecoder.decode(tickerMap.get("like").getString("target") , "EUC-KR" );
            else
                tickerUrl = URLDecoder.decode(tickerMap.get("follow").getString("target") , "EUC-KR" );
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        customWebView.initContentView(tickerUrl);

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

    private void signOut()
    {
        BasePreference.getInstance(this).removeAll();
        IntentData indata = new IntentData();
        indata.aniType = Constants.VIEW_ANIMATION.ANI_FLIP;
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String tag = toolbar_header.findViewById(R.id.btn_toolbar_alert).getTag().toString();
            ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_alert)).setImageResource(R.drawable.top_icon_notice);
            if(tag.equals("on"))
            {
                toolbar_header.findViewById(R.id.btn_toolbar_alert).setTag("off");
                ticker_header.setVisibility(View.GONE);
            }

            String id  = BasePreference.getInstance(MainPleaListActivity.this).getValue(BasePreference.ID, "");
            if(id.equals(""))
            {
                UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(MainPleaListActivity.this);
                id =   userInfoData.getId();
            }

            switch (v.getId())
            {
                case R.id.btn_menu :
                case R.id.btn_menu_profile :

                    openDrawer();
                    break;

                case R.id.btn_toolbar_search :

                    searchAction();

                    break;

                case R.id.btn_toolbar_alert :

                    ImageButton alertBtn = (ImageButton) toolbar_header.findViewById(R.id.btn_toolbar_alert);
                    if(tag.equals("off"))
                    {
                        alertBtn.setImageResource(R.drawable.top_icon_notice_on);
                        alertBtn.setTag("on");
                        ticker_header.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        alertBtn.setImageResource(R.drawable.top_icon_notice);
                        alertBtn.setTag("off");
                        ticker_header.setVisibility(View.GONE);
                    }
                    break;

                case R.id.ticker_notice :

                    setTickerUrl(0);

                    break;

                case R.id.ticker_like :

                    setTickerUrl(1);

                    break;

                case R.id.ticker_follow :

                    setTickerUrl(2);

                    break;

                case R.id.toolbar_back :
                case R.id.toolbar_back_profile :
                    backAction();
                    break;

                case R.id.btn_toolbar_img :
                    customWebView.initContentView(String.format(Constants.MAIN_URL, id));
                    break;

                case R.id.btn_toolbar_home :
                    customWebView.initContentView(String.format(Constants.MAIN_URL, id));
                    break;

                case R.id.btn_logout :
                    signOut();
                    break;

                case R.id.btn_toolbar_like :
                    String followAction = "javascript:followAction();";
                    customWebView.mView.loadUrl(followAction);
                    break;
                case R.id.btn_plea :

                    //https://developer.android.com/training/basics/intents/filters.html?hl=ko
                    IntentData indata = new IntentData();

                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = clipboardManager.getPrimaryClip();
                    String clipUrl;

                    indata.link = String.format(Constants.MENU_LINKS.PLEA_INSERT, id, "", "N");

                    if(clipData != null)
                    {
                        ClipData.Item item = clipData.getItemAt(0);
                        clipUrl = item.getText().toString().trim();
                        Logger.log(Logger.LogState.E, "clipUrl 1 = " + clipUrl);
                        boolean isUrl = Patterns.WEB_URL.matcher(clipUrl).matches();
                        if(isUrl)
                        {
                            indata.link = String.format(Constants.MENU_LINKS.PLEA_INSERT, id, clipUrl.replace("?&", "?"), "N");
                        }
                        else
                        {
                            clipUrl = Utils.getParseUrl(clipUrl);
                            //Logger.log(Logger.LogState.E, "clipUrl 2 = " + clipUrl);
                            indata.link = String.format(Constants.MENU_LINKS.PLEA_INSERT, id, clipUrl.replace("?&", "?"), "N");
                        }
                    }

                    Logger.log(Logger.LogState.E, "indata.link  = " + Utils.getStringByObject(indata.link));
                    indata.aniType = Constants.VIEW_ANIMATION.ANI_SLIDE_DOWN_IN;
                    Intent intent = new Intent(getApplicationContext(), PleaInsertActivity.class);
                    intent.putExtra(Constants.INTENT_DATA_KEY, indata);
                    startActivityForResult(intent, 0);
                    break;
            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        String locale = BasePreference.getInstance(this).getValue(BasePreference.LOCALE, "en");
        Logger.log(Logger.LogState.E, "locale = " + locale);
        super.attachBaseContext(LocaleChage.wrap(newBase, locale));
    }

}
