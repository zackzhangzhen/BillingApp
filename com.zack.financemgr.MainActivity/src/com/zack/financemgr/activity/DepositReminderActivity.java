package com.zack.financemgr.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.zack.persistent.dao.MemberDAO;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Utilities;

public class DepositReminderActivity extends BaseActivity {

	List<Member> members;
	TextView depositReminderTv;
	Button backBtn;
	String msg = "N/A";
	
	private void initDAL()
	{
    	MemberDAO dao = new MemberDAO(this);
		members = dao.queryAllEntities();	
		
		
	}
	
	private void initMsg()
	{
		List<Member> membersToRemind = new ArrayList<Member>();
		if(!Utilities.isNullOrEmptyList(members))
		{
			msg = "";
			for(Member m : members)
			{
				if(m.getDeposit() <=  20)
				{
					String str = m.getName() + "(" + String.format("%.2f", m.getDeposit()) +")\r\n"; 
					msg+=str;
				}
			}
		}
	}
	
	private void initComponents() {    	
		
		depositReminderTv = (TextView)this.findViewById(R.id.depositReminderTv);
		depositReminderTv.setText(msg);
		
		backBtn = (Button)this.findViewById(R.id.backBtn);		
		this.addListenerOnQuitButton(backBtn);
	}
	
	private void init()
	{
		initDAL();
		initMsg();
		initComponents();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_reminder);
        
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_deposit_reminder, menu);
        return true;
    }

    
}
