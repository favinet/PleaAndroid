package shop.plea.and.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import shop.plea.and.R;
import shop.plea.and.common.preference.BasePreference;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.ui.view.CustomWebView;

/**
 * Created by kwon on 2018-01-04.
 * https://developer.android.com/training/sharing/receive.html
 */

public class PleaInsertActivity extends PleaActivity {

    public CustomWebView customWebView;

    private pleaCallBack mPleaCallback = new pleaCallBack() {
        @Override
        public void onPleaClose() {
            finish();
        }

        @Override
        public void onPleaComplected() {
            setResult(MainPleaListActivity.INTENT_CALL_PLEA_COMPLECT);
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
        customWebView.setPleaCallback(mPleaCallback);
    }

    private void init()
    {

        if(inData.link.equals(""))  //외부 공유
        {

            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();

            if(Intent.ACTION_SEND.equals(action) && type != null)
            {
                String shareUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
                String id = BasePreference.getInstance(this).getValue(BasePreference.ID, null);
                inData.link = String.format(Constants.MENU_LINKS.PLEA_INSERT, id, shareUrl);
            }
        }
        Log.e("PLEA", "타이틀!" + inData.link);

        customWebView = new CustomWebView(this, this.findViewById(R.id.content).getRootView(), null);
        customWebView.initContentView(inData.link);
    }

    public interface pleaCallBack{
        void onPleaClose();
        void onPleaComplected();
    }
}
