package com.zack.financemgr.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class WebViewTestActivity extends BaseActivity {

	private WebView webview;
	Button backBtn;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_test);
        
        backBtn = (Button)this.findViewById(R.id.backBtn);
        this.addListenerOnQuitButton(backBtn);
        
        webview = (WebView) this.findViewById(R.id.authWebView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("http://tech.sina.com.cn/mobile/n/2012-07-31/08067448128.shtml");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web_view_test, menu);
        return true;
    }

    
}
