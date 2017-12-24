package shop.plea.and.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.UserInfo;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.ui.listener.FragmentListener;
import shop.plea.and.ui.view.CustomFontBtn;
import shop.plea.and.ui.view.CustomFontTextView;
import shop.plea.and.ui.view.CustomWebView;

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
    @BindView(R.id.inapp_footer) RelativeLayout inapp_footer;


    public CustomWebView customWebView;
    private Listener mListener = new Listener();
    private static MainPleaListActivity.pleaCallBack mPleaCallback;

    private titleCallback mTitleCallback = new titleCallback() {
        @Override
        public void onReceive(String title) {
            inapp_title.setText(title);
        }
    };

    public static  void setPleaCallback(MainPleaListActivity.pleaCallBack listener)
    {
        mPleaCallback = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inapp_weview);

        initScreen();
        init();
    }

    public interface titleCallback{
        void onReceive(String title);
    }

    public void initScreen()
    {
        Log.e("PLEA", "타이틀!" + inData.title);
        if(inData.screenType.equals(Constants.SCREEN_TYPE.SHOP))
            inapp_title.setSelected(true);
        else if(inData.screenType.equals(Constants.SCREEN_TYPE.INAPP)){
            inapp_title.setText(inData.title);
            inapp_footer.setVisibility(View.GONE);
        }
        else            //비밀번호 변경 헤더 변경
        {
            inapp_title.setText(inData.title);
            inapp_footer.setVisibility(View.GONE);
        }

        inapp_footer_back.setOnClickListener(mListener);
        inapp_footer_next.setOnClickListener(mListener);
        inapp_footer_refresh.setOnClickListener(mListener);
        inapp_footer_plea.setOnClickListener(mListener);
        inapp_close.setOnClickListener(mListener);

    }

    private void init()
    {
        customWebView = new CustomWebView(this, this.findViewById(R.id.content).getRootView(), 0);
        if(inData.title.equals("")) {
            customWebView.setWebTitleCallback(mTitleCallback);
        }
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
                    UserInfoData userInfoData = UserInfo.getInstance().getCurrentUserInfoData(context);
                    String url = customWebView.mView.getUrl();
                    mPleaCallback.onPlea(String.format(Constants.MENU_LINKS.PLEA, url, userInfoData.getId()));
                    finish();

                    break;

                case R.id.inapp_close :
                  //  System.exit(0);
                    finish();

                    break;

            }
        }
    }
}
