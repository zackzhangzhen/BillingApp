package com.zack.financemgr.activity;


import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zack.utilities.Utilities;

public abstract class BaseActivity extends Activity {

	protected Activity _self;
	
	public void addListenerOnButtonForResult(Button btn, final Context context, final Class<?> cls, 
			final Map<String, String> strNvPairs,
			final Map<String, Boolean> boolNvPairs,
			final Map<String, Long> longNvPairs,
			final Map<String, Float> floatNvPairs)
	{
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				startNewActivityForResult(context, cls, strNvPairs, boolNvPairs, longNvPairs, floatNvPairs );
			}
		});
	}
	
	public void addListenerOnButtonForResult(Button btn, final Context context, final Class<?> cls)
	{
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				startNewActivityForResult(context, cls);
			}
		});
	}
	
	public void addListenerOnButton(Button btn, final Context context, final Class<?> cls, 
			final Map<String, String> strNvPairs,
			final Map<String, Boolean> boolNvPairs,
			final Map<String, Long> longNvPairs,
			final Map<String, Float> floatNvPairs)
	{
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				startNewActivity(context, cls, strNvPairs, boolNvPairs, longNvPairs, floatNvPairs );
			}
		});
	}

	
	protected void startNewActivityForResult(Context context, Class<?> cls, 
			final Map<String, String> strNvPairs,
			final Map<String, Boolean> boolNvPairs,
			final Map<String, Long> longNvPairs,
			final Map<String, Float> floatNvPairs)
	{
		Intent intent = GetFilledIntent(context, cls, strNvPairs, boolNvPairs, longNvPairs, floatNvPairs);
		
		startActivityForResult(intent, 1);
	}
	
	protected void startNewActivityForResult(Context context, Class<?> cls)
	{
		startNewActivityForResult(context, cls, null, null, null, null);
	}
	
	public void startNewActivity(Context context, Class<?> cls, 
			final Map<String, String> strNvPairs,
			final Map<String, Boolean> boolNvPairs,
			final Map<String, Long> longNvPairs,
			final Map<String, Float> floatNvPairs)
	{
		Intent intent = GetFilledIntent(context, cls, strNvPairs, boolNvPairs, longNvPairs, floatNvPairs);
		
		startActivity(intent);
	}
	
	public void startNewActivity(Context context, Class<?> cls)
	{
		startNewActivity(context, cls, null, null, null, null);
	}
	
	private Intent GetFilledIntent(Context context, Class<?> cls, 
			final Map<String, String> strNvPairs,
			final Map<String, Boolean> boolNvPairs,
			final Map<String, Long> longNvPairs,
			final Map<String, Float> floatNvPairs)
	{
		Intent intent = Utilities.newIntent(context, cls);
		
		Utilities.putExtraString(intent, strNvPairs);
		Utilities.putExtraString(intent, strNvPairs);
		Utilities.putExtraLong(intent, longNvPairs);
		Utilities.putExtraFloat(intent, floatNvPairs);
		
		return intent;
	}
	

	
	public void addListenerOnButton(Button btn, final Context context, final Class<?> cls)
	{
		addListenerOnButtonForResult(btn, context, cls, null, null, null, null);
	}
	
	public void addListenerOnButtonForDiag(Button btn, final String stmt, final String yesStr, final String noStr, final DialogInterface.OnClickListener okListener )
	{
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(_self);
				builder.setMessage(stmt).setPositiveButton(yesStr, okListener)
				    .setNegativeButton(noStr, okListener).show();
			}
		});
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
