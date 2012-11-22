package com.zack.financemgr.activity;


import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.zack.data.adapter.RosterArrayAdapter;
import com.zack.persistent.dao.MemberDAO;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Utilities;

public class MemberRosterActivity extends BaseActivity {
	
	private List<Member> members;
	private ListView rosterListView;
	private RosterArrayAdapter adapter;
	private Button computeBtn;
	private Intent intent;
	
	private void initMemberList()
	{
		MemberDAO dao = new MemberDAO(this);
		members = dao.queryAllEntities();
	}
	
	private void initComponents()
	{
		computeBtn = (Button)this.findViewById(R.id.computeRoster);
		
		computeBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				adapter.onComputeClick();
			}
		});
	}
	
	private void storeIncomingIntent()
	{
		intent = getIntent();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_roster);
        
        initMemberList();
        storeIncomingIntent();
        
        adapter = new RosterArrayAdapter(this, R.layout.roster_entry, members, intent);        
        
        initComponents();
        
        
        if(Utilities.isNullOrEmptyList(members))
        {
        	return;
        }        

        rosterListView = (ListView)findViewById(R.id.rosterListView);       
        rosterListView.setSelector(R.drawable.list_selector);
        rosterListView.setAdapter(adapter);
        
    }    
}
