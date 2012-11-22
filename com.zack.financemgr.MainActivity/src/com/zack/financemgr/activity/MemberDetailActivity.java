package com.zack.financemgr.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zack.persistent.dao.MemberDAO;
import com.zack.persistent.dao.OpAcctDAO;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Constants;
import com.zack.utilities.Utilities;

public class MemberDetailActivity extends BaseActivity {

	TextView idValueTextView;
	EditText nameValueEt;
	EditText weiboValueEt;
	EditText cellValueEt;
	EditText depositValueEt;
	EditText depositValueEditEt;
	EditText ptValueEt;
	CheckBox leaderCb;
	
	long id = Constants.INVALID_LONG;
	boolean isCreatingNewMember = false;
	
	float oldDeposit;
	
	Button okBtn;
	Button backBtn;
	
	DialogInterface.OnClickListener okListener;
	
	public void initComponents()
	{
		idValueTextView = (TextView) findViewById(R.id.idValueTextView);
		nameValueEt = (EditText) findViewById(R.id.nameEditText);
		weiboValueEt = (EditText) findViewById(R.id.weiboEditText);
		cellValueEt = (EditText) findViewById(R.id.cellEditText);
		depositValueEt = (EditText) findViewById(R.id.depositEditText);
		depositValueEditEt = (EditText) findViewById(R.id.depositAddEditText);
		ptValueEt = (EditText) findViewById(R.id.ptEditText);
		leaderCb = (CheckBox) findViewById(R.id.leaderCb);
		
		okBtn = (Button) findViewById(R.id.memberDetailOkBtn);
		backBtn = (Button) findViewById(R.id.memberDetailBackBtn);		

		depositValueEditEt.setOnFocusChangeListener(new OnFocusChangeListener()
        {
                public void onFocusChange(View v, boolean hasFocus)
                {
                	if(!hasFocus)
                	{
                		try
                    	{
                    		String addition = depositValueEditEt.getText().toString();
                            
                        	if(Utilities.isStringNullOrEmpty(addition))
                        	{
                        		return;
                        	}
                        	
                        	float additionVal = Float.valueOf(addition);
                        	
                        	String orig = depositValueEt.getText().toString();
                        	float origVal = 0f;
                        	if(!Utilities.isStringNullOrEmpty(orig))
                        	{
                        		origVal = Float.valueOf(orig);
                        	}
                        	
                        	origVal = origVal + additionVal;
                        	depositValueEt.setText(String.format("%.2f", origVal));
                        	depositValueEditEt.setText(String.format("%.2f", 0f));
                    	}
                    	catch (Exception ex)
                    	{
                    		Utilities.showToast(ex.getMessage(), _self);
                    	}
                	}
                }
          });
		
		depositValueEditEt.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
            	
            	
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){
                
            }

        }); 
		
		
		
		this.addListenerOnQuitButton(backBtn);
		
		okListener = new DialogInterface.OnClickListener() {
		    
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        {
		            if(addOrEditMember())	
		            {
		            	Utilities.showToast("亲，操作成功哦！", _self);
		            	BaseActivity ba = (BaseActivity)_self;
		            	ba.startNewActivity(_self, MemberListActivity.class);
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
		
		id =  getMemberId();
		isCreatingNewMember = id == Constants.INVALID_LONG;
		
		String stmt = isCreatingNewMember?"你真要招其入会？" : "你真要修改？";
		addListenerOnButtonForDiag(okBtn, stmt, "当然", "再想想", okListener);
		
		
	}
	
	private boolean addOrEditMember()
	{
		if(!validateInput())
		{
			return false;
		}
			
		try
		{
			MemberDAO dao = new MemberDAO(this);
			
			if(isCreatingNewMember)
			{
				Member newMember = new Member();
				fillMember(newMember);
				dao.createEntity(newMember);
				
				return true;
			}
			
			Member m = dao.queryEntityById(id);
			fillMember(m);	
			dao.updateEntity(m, id);
			
			return true;
		}
		catch(Exception ex)
		{
			Utilities.showToast(ex.toString(), this);
			return false;
		}

	}
	
	private void fillMember(Member m)
	{
		m.setName(nameValueEt.getText().toString());
		m.setWeibo(weiboValueEt.getText().toString());
		m.setCell(cellValueEt.getText().toString());
		m.setDeposit(Float.parseFloat(depositValueEt.getText().toString()));
		
		updateOpAcct(oldDeposit, m.getDeposit());
		
		m.setCurPt(Integer.parseInt(ptValueEt.getText().toString()));
		m.setLeader(leaderCb.isChecked()?1:0);
	}
	
	private void updateOpAcct(float oldDeposit, float deposit) {
		float delta = deposit - oldDeposit;
		if(delta == 0)
		{
			return;
		}
		
		OpAcctDAO dao = new OpAcctDAO(this);
		
		float curDeposit = dao.getDeposit();
		dao.updateDeposit(curDeposit + delta);		
	}

	private boolean validateInput()
	{
		if(Utilities.isStringNullOrEmpty(nameValueEt.getText().toString())) 
		{
			return false;
		}
		
		if(Utilities.isStringNullOrEmpty(cellValueEt.getText().toString())) 
		{
			return false;
		}
		
		if(Utilities.isStringNullOrEmpty(depositValueEt.getText().toString())) 
		{
			return false;
		}
		
		if(Utilities.isStringNullOrEmpty(ptValueEt.getText().toString())) 
		{
			return false;
		}
		
		return true;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
        
        initComponents();        
       
        
        fillMemberDetailFields();
    }
    
    private void fillMemberDetailFields() {
		   	          
         if(isCreatingNewMember)
         {
        	idValueTextView.setText(Constants.NOT_APPLICABLE);
         	return;
         }
         
         MemberDAO dao = new MemberDAO(this);
         Member m = dao.queryEntityById(id);
         
         String idStr = String.valueOf(id);        
         
         idValueTextView.setText(idStr);
         nameValueEt.setText(m.getName());
         weiboValueEt.setText(m.getWeibo());
         cellValueEt.setText(m.getCell());
         depositValueEt.setText(String.format("%.2f", m.getDeposit()));
         oldDeposit = m.getDeposit();
         ptValueEt.setText(String.valueOf(m.getCurPt()));
         leaderCb.setChecked(m.getLeader()==1?true:false);
	}

	private long getMemberId()
    {
    	try
    	{
            Bundle extras = getIntent().getExtras();
    		
            return Utilities.getLong(extras, Constants.ID);
    	}

		catch(Exception ex)
		{
			Utilities.showToast(ex.toString(), this);
			
			return -1;
		}
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_member_detail, menu);
        return true;
    }

    
}
