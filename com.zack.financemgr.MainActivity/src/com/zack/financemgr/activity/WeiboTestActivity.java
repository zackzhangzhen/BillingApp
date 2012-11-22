package com.zack.financemgr.activity;

import weibo4j.Oauth;
import weibo4j.examples.timeline.Upload;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

import com.zack.utilities.Utilities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WeiboTestActivity extends BaseActivity {

	Button okBtn;
	Button backBtn;
	EditText codeEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_test);
        
        codeEditText = (EditText)this.findViewById(R.id.codeEditText);
        
        okBtn = (Button)this.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Oauth oauth = new Oauth();
				AccessToken accessToken;
				try {
					
					String code = codeEditText.getText().toString();
					accessToken = oauth.getAccessTokenByCode(code);
					
					//Upload.main(new String[]{accessToken.getAccessToken(), "ŒÅË¿¶È¸üÐÂ"});
				} catch (WeiboException e) {
					Utilities.showToast(e.getMessage(), _self);
				}

			}

		});
        
        backBtn = (Button)this.findViewById(R.id.backBtn);
        this.addListenerOnQuitButton(backBtn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_weibo_test, menu);
        return true;
    }

    
}
