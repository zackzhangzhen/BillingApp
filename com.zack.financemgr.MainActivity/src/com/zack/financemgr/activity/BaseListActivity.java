package com.zack.financemgr.activity;


import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zack.utilities.Utilities;

public abstract class BaseListActivity extends ListActivity {

	protected Activity _self;
	
	public void addListenerOnButton(Button btn, final Context context, final Class<?> cls, 
			final Map<String, String> strNvPairs,
			final Map<String, Boolean> boolNvPairs,
			final Map<String, Long> longNvPairs,
			final Map<String, Float> floatNvPairs)
	{
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = Utilities.newIntent(context, cls);
				
				Utilities.putExtraString(intent, strNvPairs);
				Utilities.putExtraString(intent, strNvPairs);
				Utilities.putExtraLong(intent, longNvPairs);
				Utilities.putExtraFloat(intent, floatNvPairs);
				
				startActivityForResult(intent, 1);
			}
		});
	}
	
	public void addListenerOnButton(Button btn, final Context context, final Class<?> cls)
	{
		addListenerOnButton(btn, context, cls, null, null, null, null);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_self = this;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void addListenerOnQuitButton(Button btn) {		

		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				_self.finish();

			}

		});
	}
}