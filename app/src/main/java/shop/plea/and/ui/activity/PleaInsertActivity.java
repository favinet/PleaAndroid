package shop.plea.and.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import butterknife.BindView;
import shop.plea.and.R;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.data.tool.LocaleChage;
import shop.plea.and.data.tool.LocaleWrapper;
import shop.plea.and.ui.view.CustomFontEditView;
import shop.plea.and.ui.view.CustomFontTextView;
import shop.plea.and.ui.view.CustomWebView;

/**
 * Created by kwon on 2018-01-04.
 * https://developer.android.com/training/sharing/receive.html
 */

public class PleaInsertActivity extends PleaActivity {

    public CustomWebView customWebView;
    private boolean isExternal = false;
    @BindView(R.id.toolbar_header) Toolbar toolbar_header;
    @BindView(R.id.toolbar_title) CustomFontTextView toolbar_title;

    public interface headerJsonCallback{
        void onReceive(JSONObject jsonObject);
    }

    private PleaInsertActivity.headerJsonCallback mHeaderJsonCallback = new PleaInsertActivity.headerJsonCallback() {
        @Override
        public void onReceive(JSONObject jsonObject) {
            initToobar(jsonObject);
        }
    };

    private pleaCallBack mPleaCallback = new pleaCallBack() {
        @Override
        public void onPleaClose() {
            finish();
        }

        @Override
        public void onPleaComplected() {
            if(isExternal)  //외부 공유
            {
                Toast.makeText(PleaInsertActivity.this, getString(R.string.plea_inserted), Toast.LENGTH_LONG).show();
            }
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("", "");
            clipboardManager.setPrimaryClip(data);          //클립보드 초기화
            setResult(MainPleaListActivity.INTENT_CALL_PLEA_COMPLECT);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plea_insert);
        init();
        initScreen();


    }

    public void initScreen()
    {
        UserInfoData userInfoData = BasePreference.getInstance(this).getObject(BasePreference.USERINFO_DATA, UserInfoData.class);
        String stoken = BasePreference.getInstance(this).getValue(BasePreference.GCM_TOKEN, null);
        if(userInfoData ==  null || stoken == null)
        {
            IntentData indata = new IntentData();
            indata.aniType = Constants.VIEW_ANIMATION.ANI_FLIP;
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra(Constants.INTENT_DATA_KEY, indata);
            startActivity(intent);
            finish();
        }
        else
        {
            customWebView.setPleaCallback(mPleaCallback);
        }

    }

    private void init()
    {

        //Logger.log(Logger.LogState.E, "PleaInsertActivity indata.link  = " + Utils.getStringByObject(inData.link ));
        if(inData.link.equals(""))  //외부 공유
        {
            isExternal = true;
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();

            if(Intent.ACTION_SEND.equals(action) && type != null)
            {
                String shareUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
                //Logger.log(Logger.LogState.E, "PleaInsertActivity shareUrl = " + shareUrl);
                boolean isUrl = Patterns.WEB_URL.matcher(shareUrl).matches();
                if(!isUrl)
                {
                    shareUrl = Utils.getParseUrl(shareUrl);
                }
                String id = BasePreference.getInstance(this).getValue(BasePreference.ID, null);
               // Toast.makeText(this, "shareUrl : " + shareUrl, Toast.LENGTH_LONG).show();
                inData.link = String.format(Constants.MENU_LINKS.PLEA_INSERT, id, shareUrl.replace("?&", "?"), "Y");
            }
        }
        //Logger.log(Logger.LogState.E, "PleaInsertActivity indata.link  = " + Utils.getStringByObject(inData.link ));
        customWebView = new CustomWebView(this, this.findViewById(R.id.content).getRootView(), null);
        customWebView.initContentView(inData.link);
        customWebView.setWebHeaderPleaInsertCallback(mHeaderJsonCallback);
    }

    public void initToobar(JSONObject jsonObject)
    {
        //Logger.log(Logger.LogState.E, "Plea Insert = " + Utils.getStringByObject(jsonObject));
        try
        {
            if(jsonObject == null)
            {
                toolbar_header.setVisibility(View.GONE);
            }
            else
            {

                toolbar_header.setVisibility(View.VISIBLE);

                String title = (jsonObject.has("title")) ? jsonObject.getString("title") : "N";

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


                toolbar_header.setBackgroundColor(getResources().getColor(R.color.colorSubHeader));
                Utils.changeStatusColor(this, R.color.colorSubHeader);

            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public interface pleaCallBack{
        void onPleaClose();
        void onPleaComplected();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(customWebView.mView.getUrl().indexOf("cartRegResult") > -1)
        {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration config = newBase.getResources().getConfiguration();
        String locale = BasePreference.getInstance(newBase).getValue(BasePreference.LOCALE, LocaleChage.getSystemLocale(config).getLanguage());
        //Logger.log(Logger.LogState.E, "Insert locale = " + locale);
        //super.attachBaseContext(LocaleChage.wrap(newBase, locale));
        LocaleWrapper.setLocale(locale);
        super.attachBaseContext(LocaleWrapper.wrap(newBase));
    }

}
