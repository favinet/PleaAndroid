package shop.plea.and.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import shop.plea.and.R;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.ui.view.CustomWebView;

/**
 * Created by kwon on 2018-01-04.
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
            Bundle bundle = intent.getExtras();
        //    Uri uri = (Uri)bundle.get(Intent.EXTRA_TEXT);

            if (intent.getType().equals("text/plain"))
            {
                // Handle intents with text ...
               // inData.link = data.getPath();
                Log.e("PLEA", "data!" + Utils.getStringByObject(bundle));
            }
        }
        Log.e("PLEA", "타이틀!" + inData.link);

        customWebView = new CustomWebView(this, this.findViewById(R.id.content).getRootView(), 0);
        customWebView.initContentView(inData.link);
    }
}
