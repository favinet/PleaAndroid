package shop.plea.and.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.ui.fragment.SideMenuDrawerFragment;
import shop.plea.and.ui.listener.FragmentListener;
import shop.plea.and.ui.view.CustomFontBtn;
import shop.plea.and.ui.view.CustomFontTextView;
import shop.plea.and.ui.view.CustomWebView;
import shop.plea.and.ui.view.DrawerLayoutHorizontalSupport;

/**
 * Created by kwon7575 on 2017-10-27.
 */

public class InAppWebView extends PleaActivity{

    @BindView(R.id.inapp_title) CustomFontTextView inapp_title;
    @BindView(R.id.inapp_footer_back) ImageView inapp_footer_back;
    @BindView(R.id.inapp_footer_next) ImageView inapp_footer_next;
    @BindView(R.id.inapp_footer_refresh) ImageView inapp_footer_refresh;
    @BindView(R.id.inapp_footer_plea) CustomFontBtn inapp_footer_plea;
    @BindView(R.id.inapp_close) ImageView inapp_close;




    public CustomWebView customWebView;
    private Listener mListener = new Listener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp_weview);

        initScreen();
        init();
    }



    public void initScreen()
    {
        inapp_title.setText(inData.title);
        inapp_footer_back.setOnClickListener(mListener);
        inapp_footer_next.setOnClickListener(mListener);
        inapp_footer_refresh.setOnClickListener(mListener);
        inapp_footer_plea.setOnClickListener(mListener);
        inapp_close.setOnClickListener(mListener);

    }

    private void init()
    {
        customWebView = new CustomWebView(this, this.findViewById(R.id.content).getRootView(), 0);
        customWebView.initContentView(inData.link);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {

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
                case R.id.inapp_footer_back :

                    customWebView.goBack();

                    break;

                case R.id.inapp_footer_next :

                    customWebView.goNext();

                    break;

                case R.id.inapp_footer_refresh :

                    customWebView.refresh();

                    break;

                case R.id.inapp_footer_plea :

                    Toast.makeText(InAppWebView.this, "함수 호출!", Toast.LENGTH_SHORT).show();
                    finish();

                    break;

                case R.id.inapp_close :

                    finish();

                    break;

            }
        }
    }
}
