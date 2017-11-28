package shop.plea.and.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
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

    public CustomWebView customWebView;
    private Listener mListener = new Listener();
    private Fragment drawer_Fragment;

    public interface titleCallback{
        void onReceive(String title);
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

        initToobar();
        initScreen();
        init();
    }

    private void initToobar()
    {
        toolbar_header.findViewById(R.id.btn_menu).setVisibility(View.VISIBLE);
        toolbar_header.findViewById(R.id.btn_toolbar_img).setVisibility(View.VISIBLE);
        toolbar_header.findViewById(R.id.btn_toolbar_search).setVisibility(View.VISIBLE);
        toolbar_header.findViewById(R.id.btn_toolbar_alert).setVisibility(View.VISIBLE);
        toolbar_header.findViewById(R.id.toolbar_right_btns).setVisibility(View.VISIBLE);
        toolbar_header.setBackgroundColor(getResources().getColor(R.color.colorSubHeader));

        Utils.changeStatusColor(this, R.color.colorSubHeader);
    }

    public void initScreen()
    {
        setSupportActionBar(toolbar_header);
        toolbar_header.findViewById(R.id.btn_menu).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_toolbar_search).setOnClickListener(mListener);
        toolbar_header.findViewById(R.id.btn_toolbar_alert).setOnClickListener(mListener);

        UserInfoData userInfo = UserInfo.getInstance().getCurrentUserInfoData(this);
        txt_nickname.setText(String.format(getString(R.string.user_resist_finish), userInfo.getNickname()));

        Glide.with(this)
                .load(userInfo.getProfileImg())
                .into(main_profile);

    }

    private void init()
    {
        customWebView = new CustomWebView(this, this.findViewById(R.id.content).getRootView(), 0);
        //customWebView.initContentView(inData.link);
        customWebView.initContentView("http://www.favinet.co.kr/deeplink_test.html");
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


    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_menu :
                    openDrawer();
                    break;

                case R.id.btn_toolbar_search :
                    Toast.makeText(MainPleaListActivity.this, "어떤 검색 동작을 할까요?", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btn_toolbar_alert :
                    Toast.makeText(MainPleaListActivity.this, "어떤 알림 동작을 할까요?", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
