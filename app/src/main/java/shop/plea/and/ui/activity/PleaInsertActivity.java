package shop.plea.and.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plea_insert);
        init();
        initScreen();


    }

    public void initScreen()
    {

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

        customWebView = new CustomWebView(this, this.findViewById(R.id.content).getRootView(), 0);
        customWebView.initContentView(inData.link);
    }
}
