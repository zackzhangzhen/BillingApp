package com.zack.test;

import android.os.Bundle;
import android.widget.Button;

import com.zack.financemgr.activity.BaseActivity;
import com.zack.financemgr.activity.R;
import com.zack.financemgr.activity.WebViewTestActivity;
import com.zack.financemgr.activity.WeiboTestActivity;

public class MainTestActivity extends BaseActivity {

	Button daoTestBtn;
	Button webViewTestBtn;
	Button weiboTestBtn;
	Button backBtn;
	private void initButtons()
	{
		daoTestBtn = (Button) this.findViewById(R.id.button1);
		webViewTestBtn = (Button) this.findViewById(R.id.webViewTestBtn);
		weiboTestBtn = (Button) this.findViewById(R.id.weiboTestBtn);
		backBtn = (Button) this.findViewById(R.id.backBtn);
		
		addListenerOnButton(daoTestBtn, MainTestActivity.this,DAOTestActivity.class);
		addListenerOnButton(webViewTestBtn, MainTestActivity.this,WebViewTestActivity.class);
		addListenerOnButton(weiboTestBtn, MainTestActivity.this,WeiboTestActivity.class);
		this.addListenerOnQuitButton(backBtn);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        
        initButtons();
        
        
        
		/*int i = Utilities.getCourtesy(24.7f);
		System.out.println("Utilities.getCourtesy(24.7f) = " + i);

		i = Utilities.getCourtesy(25.0f);
		System.out.println("Utilities.getCourtesy(25.0f) = " + i);

		i = Utilities.getCourtesy(25.1f);
		System.out.println("Utilities.getCourtesy(25.1f) = " + i);*/
    }
}
