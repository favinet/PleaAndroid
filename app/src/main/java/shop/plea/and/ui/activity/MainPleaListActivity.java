package shop.plea.and.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import shop.plea.and.ui.fragment.SideMenuDrawerFragment;
import shop.plea.and.ui.listener.FragmentListener;
import shop.plea.and.ui.view.CustomFontEditView;
import shop.plea.and.ui.view.CustomFontTextView;
import shop.plea.and.ui.view.CustomWebView;
import shop.plea.and.ui.view.DrawerLayoutHorizontalSupport;

/**
 * Created by kwon7575 on 2017-10-27.
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
            Logger.log(Logger.LogState.E, "mSideMenuCallback : " + url);
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

    public interface headerJsonCallback{
        void onReceive(JSONObject jsonObject);
    }


    public interface sideMenuCallback{
        void onReceive(String url);
    }


    public interface pleaCallBack{
        void onPlea(String url);
    }

    Handler handler = new Handler();
    Runnable runMain = new Runnable() {
        @Override
        public void run() {
                hellow_area.setVisibility(View.GONE);
        }
    };

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

    private void initToobar(JSONObject jsonObject)
    {
        try
        {
            if(jsonObject == null)
            {
                toolbar_header.setVisibility(View.GONE);
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

                if(alertBt.equals("Y"))
                {
                    JSONArray tickers = (jsonObject.has("tickers")) ? jsonObject.getJSONArray("tickers") : null;
                    Logger.log(Logger.LogState.E, "header tickers!" + Utils.getStringByObject(tickers));
                    if(tickers != null)
                    {
                        for(int i = 0; i < tickers.length(); i++)
                        {
                            JSONObject ticker = tickers.getJSONObject(i);
                            Logger.log(Logger.LogState.E, "header ticker!" + Utils.getStringByObject(ticker));
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
                    toolbar_title.setText(Utils.decode(title, "UTF-8"));
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
                            Logger.log(Logger.LogState.E, "onEditorAction = " + Utils.getStringByObject(actionId));
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
                        Log.e("PLEA", "url = " + url);
                        String[] urls = url.split("/");
                        int urlLen = urls.length;
                        if(urlLen > 4)
                        {
                            String tag = Utils.decode(urls[5], "UTF-8");
                            Log.e("PLEA", "tag = " + tag);
                            Log.e("PLEA", "tag = " + tag.indexOf("?"));
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
        toolbar_header.findViewById(R.id.btn_toolbar_modify).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_toolbar_like).setOnClickListener(mListener);


        ticker_notice.setOnClickListener(mListener);
        ticker_like.setOnClickListener(mListener);
        ticker_follow.setOnClickListener(mListener);

        UserInfoData userInfo = UserInfo.getInstance().getCurrentUserInfoData(this);
        txt_nickname.setText(String.format(getString(R.string.user_resist_finish), userInfo.getNickname()));

        Glide.with(this)
                .load(userInfo.getProfileImg())
                .into(main_profile);

    }

    private void init()
    {
        customWebView = new CustomWebView(this, this.findViewById(R.id.content).getRootView(), 0);
        customWebView.setWebHeaderCallback(mHeaderJsonCallback);

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
        //customWebView.initContentView("http://plea.shop/block/"+uid);
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
        Logger.log(Logger.LogState.E, "MAIN onKeyDown!");
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


           // boolean check = backPressed();
           // if(check) return check;
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

        Logger.log(Logger.LogState.E, "preAction!" + preAction);
        Logger.log(Logger.LogState.E, "target!" + target);
        if(preAction.equals("T"))
        {
            customWebView.initContentView(target);
        }
        else if(preAction.equals("B"))
        {
            customWebView.goBack();
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
            Logger.log(Logger.LogState.E, "keyword = " + searchAction);
            Logger.log(Logger.LogState.E, "keyword = " + Utils.encode(searchAction));
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

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            ((ImageButton)toolbar_header.findViewById(R.id.btn_toolbar_alert)).setImageResource(R.drawable.top_icon_notice);
            toolbar_header.findViewById(R.id.btn_toolbar_alert).setTag("off");
            ticker_header.setVisibility(View.GONE);

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
                    String tag = toolbar_header.findViewById(R.id.btn_toolbar_alert).getTag().toString();
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

                case R.id.btn_toolbar_modify :
                    Toast.makeText(MainPleaListActivity.this, "memberUpdate 함수 호출 되는지 확인!", Toast.LENGTH_LONG).show();
                    String memberUpdate = "javascript:memberUpdate();";
                    customWebView.mView.loadUrl(memberUpdate);
                    break;

                case R.id.btn_toolbar_like :
                   // Toast.makeText(MainPleaListActivity.this, "followAction 함수 호출 되는지 확인!", Toast.LENGTH_LONG).show();
                    String followAction = "javascript:followAction();";
                    customWebView.mView.loadUrl(followAction);
                    break;
            }
        }
    }
}
