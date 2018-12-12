package com.ebrightmoon.dclient.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebrightmoon.dclient.util.Constant;
import com.ebrightmoon.dclient.view.ProgressWebView;
import com.ebrightmoon.dclient.R;
import com.ebrightmoon.dclient.util.CookiesTools;
import com.ebrightmoon.dclient.util.PrefUtils;

import static android.view.KeyEvent.KEYCODE_BACK;

public class RepairPushActivity extends AppCompatActivity {

    private ProgressWebView mWebView;
    private String title;
    private String rePairUrl;
    private FrameLayout mWebViewContent;
    private Context mContext;

    /**
     * 初始化标题
     */
    private void initTitleBar(String title) {
        RelativeLayout bar_rl_left = (RelativeLayout) findViewById(R.id.bar_rl_left);
        bar_rl_left.setVisibility(View.VISIBLE);
        bar_rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
//                    mWebView.goBack();
                    mWebView.loadUrl("javascript:goBack()");
                } else {
                    finish();
                }
            }
        });
        TextView bar_tv_title = (TextView) findViewById(R.id.bar_tv_title);
        bar_tv_title.setText(title);

    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initData();
    }

    @SuppressLint("JavascriptInterface")
    public void initView() {
        title = getIntent().getStringExtra(Constant.PUSH_REPAIR_TITLE);
        rePairUrl = getIntent().getStringExtra(Constant.URL_REPAIR_URL);
        initTitleBar(this.title);
        mWebViewContent = (FrameLayout) findViewById(R.id.fl_content);
        mWebView = new ProgressWebView(this, null);
        mWebViewContent.addView(mWebView);
        // 设置webview属性
        initWebViewSettings(mWebView.getSettings());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mWebView.progressbar.setVisibility(View.GONE);

                } else {
                    mWebView.progressbar.setVisibility(View.VISIBLE);
                    mWebView.progressbar.setProgress(newProgress);
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            private String startUrl;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                startUrl = url;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (startUrl != null && startUrl.equals(url)) {
                    view.loadUrl(url);
                } else {
                    //交给系统处理
                    return super.shouldOverrideUrlLoading(view, url);
                }
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              String url) {
                WebResourceResponse response = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    response = super.shouldInterceptRequest(view, url);
                }
                return response;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });


        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        CookiesTools.setCookies(mContext, rePairUrl, "agentAccount_" + PrefUtils.getInt(mContext, PrefUtils.KEY.TOP_AGENT_ID, 0) + "=" + PrefUtils.getString(mContext, PrefUtils.KEY.USER_NAME, ""));
        CookiesTools.setCookies(mContext, rePairUrl, "fromagent=" + PrefUtils.getInt(mContext, PrefUtils.KEY.TOP_AGENT_ID, 0));
        CookiesTools.setCookies(mContext, rePairUrl, "s_LoginStatus_" + PrefUtils.getInt(mContext, PrefUtils.KEY.TOP_AGENT_ID, 0) + "=" + PrefUtils.getInt(mContext, PrefUtils.KEY.LOGIN_STATUS, 0));
        CookiesTools.setCookies(mContext, rePairUrl, "agent_" + PrefUtils.getInt(mContext, PrefUtils.KEY.TOP_AGENT_ID, 0) + "=" + PrefUtils.getInt(mContext, PrefUtils.KEY.AGENT_ID, 0));
        CookiesTools.setCookies(mContext, rePairUrl, "isHavaLicenseno_" + PrefUtils.getInt(mContext, PrefUtils.KEY.AGENT_ID, 0) + "=" + PrefUtils.getString(mContext, PrefUtils.KEY.REPEAT_QUOTE, ""));
        CookiesTools.setCookies(mContext, rePairUrl, "tx_login_token_" + PrefUtils.getInt(mContext, PrefUtils.KEY.TOP_AGENT_ID, 0) + "=" + PrefUtils.getString(mContext, PrefUtils.KEY.TOKEN, ""));
        CookiesTools.setCookies(mContext, rePairUrl, "tx_login_agentname_" + PrefUtils.getInt(mContext, PrefUtils.KEY.TOP_AGENT_ID, 0) + "=" + PrefUtils.getString(mContext, PrefUtils.KEY.AGENT_NAME, ""));
        CookiesTools.setCookies(mContext, rePairUrl, "s_token_" + PrefUtils.getInt(mContext, PrefUtils.KEY.TOP_AGENT_ID, 0) + "=" + PrefUtils.getString(mContext, PrefUtils.KEY.TOKEN, ""));
        mWebView.addJavascriptInterface(new JavaScriptInterface(mContext), "AndroidAPP");
        mWebView.loadUrl(rePairUrl);
    }


    public void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null)
            mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null)
            mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        CookiesTools.removeCookie(mContext);
        if (mWebView != null) {
            mWebView.setWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            if (mWebViewContent != null)
                mWebViewContent.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK && mWebView.canGoBack()) {
//            mWebView.goBack();
            mWebView.loadUrl("javascript:goBack()");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void initWebViewSettings(WebSettings wSet) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wSet.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 是否显示缩放按钮
        wSet.setBuiltInZoomControls(false);
        // 支持缩放
        wSet.setSupportZoom(false);
        wSet.setTextZoom(100);
        // 默认字体大小
        wSet.setDefaultFontSize(12);
        wSet.setAllowFileAccess(false);
        // 设置可以访问文件
        wSet.setAllowFileAccess(true);
        // 设置支持webView JavaScript
        wSet.setJavaScriptEnabled(true);
        // 设置缓冲的模式
        wSet.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置字符编码
        wSet.setDefaultTextEncodingName("utf-8");
        //优先使用缓存
//        wSet.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wSet.setAppCacheEnabled(false);
        wSet.setDomStorageEnabled(true);
        wSet.setDatabaseEnabled(true);

    }


    //    private void startPer
    public class JavaScriptInterface {

        private Context context;

        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        /**
         * 返回首页方法
         */
        @JavascriptInterface
        public void goHome() {
            //替换成第三方产品主页
            startActivity(new Intent(mContext, HomeActivity.class));
            finish();
        }

        /**
         * 返回登录页方法
         */
        @JavascriptInterface
        public void goLogin() {
            //替换成第三方产品登录页
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }

        //更改标题 暂时无用
        @JavascriptInterface
        public void changeTitle(String title) {

        }

    }
}
