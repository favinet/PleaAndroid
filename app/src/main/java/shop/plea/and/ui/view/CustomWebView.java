package shop.plea.and.ui.view;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.ui.activity.MainPleaListActivity;

/**
 * Created by kwon7575 on 2017-10-28.
 */

public class CustomWebView {

    private BaseActivity base;
    public WebView mView, mWebviewPop;
    private ProgressBar progressBar;
    public String curUrl;
    public FrameLayout mContainer;
    public Map<String, String> titleArr = new HashMap<>();
    private MainPleaListActivity.titleCallback callback;
    private int mPosition = 0;

    public CustomWebView(BaseActivity baseActivity, View v, int postion) {
        base = baseActivity;

        mView = (WebView) v.findViewById(R.id.webview);
        mContainer = (FrameLayout) v.findViewById(R.id.webview_frame);
        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar);

        mView.setFocusable(true);
        mView.setVerticalScrollbarOverlay(true);
        mView.getSettings().setSupportZoom(true);
        mView.getSettings().setJavaScriptEnabled(true);
        mView.setVerticalScrollBarEnabled(true);
        mView.getSettings().setDomStorageEnabled(true);
        mView.getSettings().setSupportMultipleWindows(true);

        mPosition = postion;

        if (18 < Build.VERSION.SDK_INT) {
            mView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            mView.getSettings().setTextZoom(100);

        // 롤리팝이상 https->http 데이타 전송 block됨
        if (20 < Build.VERSION.SDK_INT) {
            mView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mView, true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(mView, true);
            mView.setWebContentsDebuggingEnabled(true);
        }

        // request check
        mView.setWebViewClient(new MyCustomWebViewClient());
        // alert check
        mView.setWebChromeClient(new MyCustomWebChromeClient());
    }

    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.log(Logger.LogState.D, "webviewview load : " + url);

            if (Utils.getNetWorkType(base) == Utils.NETWORK_NO) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(base);
                dialog.setTitle(R.string.app_name).setMessage("네트워크 상태를 확인해주세요").setPositiveButton("예", null).create().show();

                return true;
            }

            if (url.startsWith("intent")) {
                Intent intent = null;
                try {
                    intent = Intent.parseUri(url, 0);
                    base.startActivity(intent);
                    return true;
                } catch (ActivityNotFoundException ex) {
                    ex.printStackTrace();
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + intent.getPackage()));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    base.startActivity(i);

                    return true;
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }

           // String action = Utils.queryToMap(url).get("name");
            if(url.contains("webview"))
            {

                    String target = Utils.queryToMap(url).get("target");
                    Logger.log(Logger.LogState.D, "webviewview target : " + Utils.getStringByObject(target));
                    url = Utils.queryToMap(url).get("url");
                    Logger.log(Logger.LogState.D, "webviewview url : " + Utils.getStringByObject(url));
                    initContentView(url);

            }

            curUrl = url;
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CookieSyncManager.getInstance().sync();



            super.onPageFinished(view, url);
        }
    }

    private class MyCustomWebChromeClient extends WebChromeClient {
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(view.getContext());
            mWebviewPop.setFocusable(true);
            mWebviewPop.setVerticalScrollbarOverlay(true);
            mWebviewPop.getSettings().setSupportZoom(true);
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.setVerticalScrollBarEnabled(true);
            mWebviewPop.getSettings().setSupportMultipleWindows(true);

            mWebviewPop.setWebViewClient(new MyCustomWebViewClient());
            mWebviewPop.setWebChromeClient(new MyCustomWebChromeClient());
            mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mContainer.addView(mWebviewPop);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
            if(mWebviewPop != null)
            {
                mWebviewPop.setVisibility(View.GONE);
                mContainer.removeView(mWebviewPop);
                mWebviewPop = null;
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            // TODO Auto-generated method stub
//			Log.e(Constants.LogTag, "web title : " + title+ ", url : " + view.getUrl());

            if(!titleArr.containsKey(view.getUrl()))
            {
                titleArr.put(view.getUrl(), title);
            }

            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            if(progressBar != null)
            {
                progressBar.setProgress(progress);

                if(progress == 100)
                {
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    if(!progressBar.isShown())
                    {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(base);
            dialog.setTitle(R.string.app_name).setMessage(message).setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).create().setCanceledOnTouchOutside(false);

            if(!base.isFinishing()) dialog.show();

            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(base);
            dialog.setTitle(R.string.app_name).setMessage(message).setCancelable(false).setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).create().setCanceledOnTouchOutside(false);

            if(!base.isFinishing()) dialog.show();

            return true;
        }
    }

    private void setWebViewTitle(String title) {
        if(callback != null) callback.onReceive(title);
    }

    public void setWebTitleCallback(MainPleaListActivity.titleCallback listener)
    {
        callback = listener;
    }

    public void initContentView(String link) {
//		String targetUrl = WebViewHelper.getTargetUrlWithParam(base, link);
        curUrl = link;
        mView.loadUrl(link);
    }

}
