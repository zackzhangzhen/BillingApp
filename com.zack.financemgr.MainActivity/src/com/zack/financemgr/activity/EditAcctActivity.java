package com.zack.financemgr.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;

import com.zack.persistent.dao.ActivityAcctDAO;
import com.zack.persistent.dao.BallAcctDAO;
import com.zack.persistent.dao.OpAcctDAO;
import com.zack.utilities.Utilities;

public class EditAcctActivity extends BaseActivity {

	private EditText opAcctEt;
	private EditText ballAcctEt;
	private EditText activityAcctEt;
	private EditText opAcctAddEt;
	private EditText ballAcctAddEt;
	private EditText activityAcctAddEt; 
	private Button okBtn;
	private Button backBtn;
	
	float opDeposit;
	float ballDeposit;
	float activityDeposit;
	
	DialogInterface.OnClickListener okListener;
	
	private void initComponents()
	{
		opAcctEt = (EditText)this.findViewById(R.id.opAcctDepositEditText);
		ballAcctEt = (EditText)this.findViewById(R.id.ballAcctDepositEditText);
		activityAcctEt = (EditText)this.findViewById(R.id.activityAcctDepositEditText);
		opAcctAddEt = (EditText)this.findViewById(R.id.opAcctDepositAddEditText);
		ballAcctAddEt = (EditText)this.findViewById(R.id.ballAcctDepositAddEditText);
		activityAcctAddEt = (EditText)this.findViewById(R.id.activityAcctDepositAddEditText);
		
		opAcctEt.setText(String.format("%.2f", opDeposit));
		ballAcctEt.setText(String.format("%.2f", ballDeposit));
		activityAcctEt.setText(String.format("%.2f", activityDeposit));
		
		opAcctAddEt.setOnFocusChangeListener(new OnFocusChangeListener()
        {
                public void onFocusChange(View v, boolean hasFocus)
                {
                	if(!hasFocus)
                	{
                		try
                    	{
                    		String addition = opAcctAddEt.getText().toString();
                            
                        	if(Utilities.isStringNullOrEmpty(addition))
                        	{
                        		return;
                        	}
                        	
                        	float additionVal = Float.valueOf(addition);
                        	
                        	String orig = opAcctEt.getText().toString();
                        	float origVal = 0f;
                        	if(!Utilities.isStringNullOrEmpty(orig))
                        	{
                        		origVal = Float.valueOf(orig);
                        	}
                        	
                        	origVal = origVal + additionVal;
                        	opAcctEt.setText(String.format("%.2f", origVal));
                        	opAcctAddEt.setText(String.format("%.2f", 0f));
                    	}
                    	catch (Exception ex)
                    	{
                    		Utilities.showToast(ex.getMessage(), _self);
                    	}
                	}
                }
          });
		
		ballAcctAddEt.setOnFocusChangeListener(new OnFocusChangeListener()
        {
                public void onFocusChange(View v, boolean hasFocus)
                {
                	if(!hasFocus)
                	{
                		try
                    	{
                    		String addition = ballAcctAddEt.getText().toString();
                            
                        	if(Utilities.isStringNullOrEmpty(addition))
                        	{
                        		return;
                        	}
                        	
                        	float additionVal = Float.valueOf(addition);
                        	
                        	String orig = ballAcctEt.getText().toString();
                        	float origVal = 0f;
                        	if(!Utilities.isStringNullOrEmpty(orig))
                        	{
                        		origVal = Float.valueOf(orig);
                        	}
                        	
                        	origVal = origVal + additionVal;
                        	ballAcctEt.setText(String.format("%.2f", origVal));
                        	ballAcctAddEt.setText(String.format("%.2f", 0f));
                    	}
                    	catch (Exception ex)
                    	{
                    		Utilities.showToast(ex.getMessage(), _self);
                    	}
                	}
                }
          });
		
		activityAcctAddEt.setOnFocusChangeListener(new OnFocusChangeListener()
        {
                public void onFocusChange(View v, boolean hasFocus)
                {
                	if(!hasFocus)
                	{
                		try
                    	{
                    		String addition = activityAcctAddEt.getText().toString();
                            
                        	if(Utilities.isStringNullOrEmpty(addition))
                        	{
                        		return;
                        	}
                        	
                        	float additionVal = Float.valueOf(addition);
                        	
                        	String orig = activityAcctEt.getText().toString();
                        	float origVal = 0f;
                        	if(!Utilities.isStringNullOrEmpty(orig))
                        	{
                        		origVal = Float.valueOf(orig);
                        	}
                        	
                        	origVal = origVal + additionVal;
                        	activityAcctEt.setText(String.format("%.2f", origVal));
                        	activityAcctAddEt.setText(String.format("%.2f", 0f));
                    	}
                    	catch (Exception ex)
                    	{
                    		Utilities.showToast(ex.getMessage(), _self);
                    	}
                	}
                }
          });
		
		okBtn = (Button)this.findViewById(R.id.okBtn);
		backBtn = (Button)this.findViewById(R.id.backBtn);
		
		
		okListener = new DialogInterface.OnClickListener() {
		    
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        {
		            if(editAccts())	
		            {
		            	Utilities.showToast("亲，操作成功哦！", _self);
		            }
		            else
		            {
		            	Utilities.showToast("给我检查一下输入正确性先...", _self);
		            }
		        		
		            break;	
		        }


		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};
		
		addListenerOnButtonForDiag(okBtn, "就这样私自修改账户余额啦？", "当然", "再想想", okListener);
		addListenerOnQuitButton(backBtn);
	}
	
	private void initDAL()
	{
		OpAcctDAO opDao = new OpAcctDAO(this);
		opDeposit = opDao.getDeposit();
		
		BallAcctDAO ballDao = new BallAcctDAO(this);
		ballDeposit = ballDao.getDeposit();
		
		ActivityAcctDAO activityDao = new ActivityAcctDAO(this);
		activityDeposit = activityDao.getDeposit();
	}
	
	private boolean validateInput()
	{
		if(Utilities.isStringNullOrEmpty(opAcctEt.getText().toString())) 
		{
			return false;
		}
		
		if(Utilities.isStringNullOrEmpty(ballAcctEt.getText().toString())) 
		{
			return false;
		}
		
		if(Utilities.isStringNullOrEmpty(activityAcctEt.getText().toString())) 
		{
			return false;
		}	
		
		return true;
	}
	
	private boolean editAccts()
	{
		if(!validateInput())
		{
			Utilities.showToast("账户的框框里面要填东西哦", this);
			return false;
		}
			
		try
		{
			OpAcctDAO opDao = new OpAcctDAO(this);				
			opDao.updateDeposit(Float.valueOf(opAcctEt.getText().toString()));
			
			BallAcctDAO ballDao = new BallAcctDAO(this);				
			ballDao.updateDeposit(Float.valueOf(ballAcctEt.getText().toString()));
			
			ActivityAcctDAO activityDao = new ActivityAcctDAO(this);				
			activityDao.updateDeposit(Float.valueOf(activityAcctEt.getText().toString()));
			
			return true;
		}
		catch(Exception ex)
		{
			Utilities.showToast(ex.toString(), this);
			return false;
		}

	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acct);
        
        this.initDAL();
        this.initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_acct, menu);
        return true;
    }

    
}
