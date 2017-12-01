package shop.plea.and.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
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
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.ui.fragment.SideMenuDrawerFragment;
import shop.plea.and.ui.listener.FragmentListener;
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

    public interface headerJsonCallback{
        void onReceive(JSONObject jsonObject);
    }

    public interface sideMenuCallback{
        void onReceive(String url);
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

        Logger.log(Logger.LogState.E, "MainPleaListActivity : " + inData.isRegist);
        if(inData.isRegist)
        {
            hellow_area.setVisibility(View.VISIBLE);
            handler.postDelayed(runMain, 5000);
        }

        drawer_Fragment = SideMenuDrawerFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.drawer_content, drawer_Fragment, "drawer_fragment");
        ft.commitAllowingStateLoss();

        ((SideMenuDrawerFragment)drawer_Fragment).setDrawerLayout(mDrawerLayout);
        ((SideMenuDrawerFragment)drawer_Fragment).setMenuCallback(mSideMenuCallback);

        initScreen();
        init();
    }

    private void initToobar(JSONObject jsonObject)
    {
        try
        {
            String menuBt = (jsonObject.has("menuBt")) ? jsonObject.getString("menuBt") : "N";
            String searchBt = (jsonObject.has("searchBt")) ? jsonObject.getString("searchBt") : "N";
            String alertBt = (jsonObject.has("alertBt")) ? jsonObject.getString("alertBt") : "N";
            String preBt = (jsonObject.has("preBt")) ? jsonObject.getString("preBt") : "N";
            String title = (jsonObject.has("title")) ? jsonObject.getString("title") : "N";

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
                toolbar_header.findViewById(R.id.btn_toolbar_alert).setVisibility(View.GONE);

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

            toolbar_header.setBackgroundColor(getResources().getColor(R.color.colorSubHeader));
            Utils.changeStatusColor(this, R.color.colorSubHeader);
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

        if(status.equals("T"))
            customWebView.initContentView(String.format(Constants.MENU_LINKS.BLOCK, uid));
        else
            customWebView.initContentView(inData.link);
        //customWebView.initContentView("http://www.favinet.co.kr/deeplink_test.html");
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

            boolean check = backPressed();
            if(check) return check;
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
            switch (v.getId())
            {
                case R.id.btn_menu :
                case R.id.btn_menu_profile :
                    openDrawer();
                    break;

                case R.id.btn_toolbar_search :
                    Toast.makeText(MainPleaListActivity.this, "어떤 검색 동작을 할까요?", Toast.LENGTH_SHORT).show();
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
            }
        }
    }
}
