package com.zack.financemgr.activity;


import java.util.List;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.zack.data.adapter.MemberArrayAdapter;
import com.zack.persistent.dao.MemberDAO;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Utilities;

public class MemberListActivity extends BaseActivity {
	
	private List<Member> members;
	private ListView memberListView;
	
	private Button newMemberBtn;
	private Button backBtn;
	
	private void initMemberList()
	{
		MemberDAO dao = new MemberDAO(this);
		members = dao.queryAllEntities();
	}
	
	private void initComponents()
	{
		newMemberBtn = (Button)this.findViewById(R.id.newMemberBtn);
		backBtn = (Button)this.findViewById(R.id.backBtn);
		
		this.addListenerOnButton(newMemberBtn, this, MemberDetailActivity.class);
		this.addListenerOnButton(backBtn, this, MainActivity.class);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        
        initMemberList();
        initComponents();
        
        if(Utilities.isNullOrEmptyList(members))
        {
        	return;
        }

        MemberArrayAdapter adapter = new MemberArrayAdapter(this, R.layout.member_entry, members);

        memberListView = (ListView)findViewById(R.id.memberListView);       
        memberListView.setSelector(R.drawable.list_selector);
        memberListView.setAdapter(adapter);
        
    }    
}
