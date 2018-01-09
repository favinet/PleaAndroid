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
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shop.plea.and.R;
import shop.plea.and.common.activity.BaseActivity;
import shop.plea.and.common.tool.Logger;
import shop.plea.and.common.tool.Utils;
import shop.plea.and.data.config.Constants;
import shop.plea.and.data.parcel.IntentData;
import shop.plea.and.ui.activity.InAppWebView;
import shop.plea.and.ui.activity.LoginActivity;
import shop.plea.and.ui.activity.MainPleaListActivity;
import shop.plea.and.ui.activity.PleaInsertActivity;

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
    private MainPleaListActivity.headerJsonCallback callback;
    private MainPleaListActivity.userUpdateCallBack userUpdateCallBack;
    private PleaInsertActivity.pleaCallBack pleaCallBack;
    private InAppWebView.titleCallback callbackTitle;
    private final static int INTENT_CALL_PROFILE_GALLERY = 3002;
    private ImageView mBtnPlea;


    public CustomWebView(BaseActivity baseActivity, View v, ImageView btnPlea) {
        base = baseActivity;
        mBtnPlea = btnPlea;
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

    public void callGallery() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        base.startActivityForResult(Intent.createChooser(intent, "File Chooser"), INTENT_CALL_PROFILE_GALLERY);
    }

    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (Utils.getNetWorkType(base) == Utils.NETWORK_NO) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(base);
                dialog.setTitle(R.string.app_name).setMessage(base.getString(R.string.network_error)).setPositiveButton(base.getString(R.string.yes), null).create().show();

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

            String action = Utils.queryToMap(url).get("name");
            Logger.log(Logger.LogState.E, "action = " + action);
            if(action != null)
            {
                if(action.equals("setTopMenu"))
                {
                    String json = Utils.queryToMap(url).get("params");

                    try
                    {
                        JSONObject jsonObject = new JSONObject(json);
                        setWebViewHeaderJson(jsonObject);
                        return true;
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        return false;
                    }

                }
                else if(action.equals("removeTopMenu"))
                {
                    setWebViewHeaderJson(null);
                    return true;
                }
                else if(action.equals("outLink"))
                {
                    String outLink = Utils.queryToMap(url).get("url");
                    if(outLink != null)
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.decode(outLink, "UTF-8")));
                        base.startActivity(intent);
                    }
                    return true;
                }
                else if(action.equals("goSignUp"))
                {
                    Intent intent = new Intent(base, LoginActivity.class);
                    base.startActivity(intent);
                    base.finish();
                    return true;
                }
                else if(action.equals("gallery"))
                {
                    callGallery();
                    return true;
                }
                else if(action.equals("updateUser"))
                {

                    JSONObject jsonObject = new JSONObject();
                    try
                    {
                        jsonObject.put("type", "updateUser");
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    setUpdateUserJson(jsonObject);

                    return true;
                }
                else if(action.equals("pleaClose"))
                {
                    pleaCallBack.onPleaClose();
                    return true;
                }
                else if(action.equals("pleaCompleted"))
                {
                    pleaCallBack.onPleaComplected();
                    return true;
                }
            }

            if(url.contains("webview"))
            {
                String decodeUrl = Utils.decode(url, "UTF-8");
                String urlParam = Utils.queryToMap(decodeUrl).get("url");
                String titleParam = Utils.queryToMap(decodeUrl).get("title");
                String targetParam = Utils.queryToMap(decodeUrl).get("target");

                urlParam = (urlParam == null) ? "" : urlParam;
                titleParam = (titleParam == null) ? "" : titleParam;
                targetParam = (targetParam == null) ? "" : targetParam;

                if(targetParam.equals("popup"))     //리스트 터치시 상세화면 popup 현재 적용 안됨
                {
                    IntentData indata = new IntentData();
                    indata.link = String.format(urlParam);
                    indata.title = titleParam;
                    indata.aniType = Constants.VIEW_ANIMATION.ANI_END_ENTER;
                    Intent intent = new Intent(base, InAppWebView.class);
                    intent.putExtra(Constants.INTENT_DATA_KEY, indata);
                    base.startActivity(intent);
                    return true;
                }
                else                                //쇼핑몰 터치시 활성화
                {
                    IntentData indata = new IntentData();
                    indata.link = String.format(urlParam);
                    indata.aniType = Constants.VIEW_ANIMATION.ANI_END_ENTER;
                    indata.screenType = Constants.SCREEN_TYPE.SHOP;
                    Intent intent = new Intent(base, InAppWebView.class);
                    intent.putExtra(Constants.INTENT_DATA_KEY, indata);
                    base.startActivity(intent);
                    return true;
                }
            }

            curUrl = url;
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if(mBtnPlea != null)
            {
                mBtnPlea.setVisibility(View.GONE);
                if(url.indexOf("/memberView/") > -1 || url.indexOf("/main") > -1 || url.indexOf("/search/") > -1) {
                    mBtnPlea.setVisibility(View.VISIBLE);
                }
                if(url.indexOf("/search/user/") > -1) {
                    mBtnPlea.setVisibility(View.GONE);
                }

            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CookieSyncManager.getInstance().sync();

            if(titleArr.get(url) != null && !titleArr.get(url).equals(""))
            {
                setWebViewTitle(titleArr.get(url));
            }

            if(url.toLowerCase().contains("notice"))
            {
                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("type", "updateNotice");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                setUpdateUserJson(jsonObject);
            }

            super.onPageFinished(view, url);
        }
    }

    private void setWebViewTitle(String title) {
        if(callbackTitle != null) callbackTitle.onReceive(title);
    }

    public void setWebTitleCallback(InAppWebView.titleCallback listener)
    {
        callbackTitle = listener;
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

    private void setWebViewHeaderJson(JSONObject jsonObject) {
        if(callback != null) callback.onReceive(jsonObject);
    }

    private void setUpdateUserJson(JSONObject jsonObject) {
        if(userUpdateCallBack != null) userUpdateCallBack.onUserUpdate(jsonObject);
    }

    public void setWebHeaderCallback(MainPleaListActivity.headerJsonCallback listener)
    {
        callback = listener;
    }

    public void setUserUpdateCallback(MainPleaListActivity.userUpdateCallBack listener)
    {
        userUpdateCallBack = listener;
    }

    public void setPleaCallback(PleaInsertActivity.pleaCallBack listener)
    {
        pleaCallBack = listener;
    }

    public void initContentView(String link) {
//		String targetUrl = WebViewHelper.getTargetUrlWithParam(base, link);
        curUrl = link;
        mView.loadUrl(link);
    }

    public void goBack()
    {
        if(mView.canGoBack())
            mView.goBack();
    }

    public void goNext()
    {
        if(mView.canGoForward())
            mView.goForward();
    }

    public boolean canBack()
    {
        return mView.canGoBack();
    }

    public void refresh()
    {
        mView.reload();
    }
}
