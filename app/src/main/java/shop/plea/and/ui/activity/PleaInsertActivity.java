package shop.plea.and.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.webkit.URLUtil;
import android.widget.Toast;

import java.net.URL;

import shop.plea.and.R;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.model.UserInfoData;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.ui.view.CustomWebView;

/**
 * Created by kwon on 2018-01-04.
 * https://developer.android.com/training/sharing/receive.html
 */

public class PleaInsertActivity extends PleaActivity {

    public CustomWebView customWebView;
    private boolean isExternal = false;

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
}
