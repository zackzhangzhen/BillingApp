package com.zack.financemgr.activity;


import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.zack.data.adapter.TurnoutArrayAdapter;
import com.zack.persistent.dao.MemberDAO;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Utilities;

public class TurnoutActivity extends BaseActivity {
	
	private List<Member> members;
	private ListView turnoutListView;
	private TurnoutArrayAdapter adapter;
	private Button computeBtn;
	private void initMemberList()
	{
		MemberDAO dao = new MemberDAO(this);
		members = dao.queryAllEntities();
	}
	
	private void initComponents()
	{
		computeBtn = (Button)this.findViewById(R.id.computeTurnout);
		
		computeBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				adapter.onComputeClick();
				
				_self.finish();
			}
		});
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnout);
        
        initMemberList();
        
        adapter = new TurnoutArrayAdapter(this, R.layout.turnout_entry, members);
        
        initComponents();
        
        if(Utilities.isNullOrEmptyList(members))
        {
        	return;
        }

        

        turnoutListView = (ListView)findViewById(R.id.turnoutListView);       
        turnoutListView.setSelector(R.drawable.list_selector);
        turnoutListView.setAdapter(adapter);
        
    }    
}
