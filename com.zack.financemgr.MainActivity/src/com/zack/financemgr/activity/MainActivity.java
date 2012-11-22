package com.zack.financemgr.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zack.test.MainTestActivity;
import com.zack.utilities.Utilities;

public class MainActivity extends BaseActivity {

	Button reckonBtn;
	Button editTurnoutBtn;
	Button editMemberBtn;
	Button editAcctBtn;
	Button depositReminderBtn;
	Button rankingBtn;
	Button testBtn;
	Button backupBtn;
	Button restoreBtn;
	

	private void initButtons()
	{
		reckonBtn = (Button) this.findViewById(R.id.reckonBtn);
		editTurnoutBtn = (Button) this.findViewById(R.id.editTurnout);
		editAcctBtn = (Button) findViewById(R.id.editAcct);
		depositReminderBtn = (Button) findViewById(R.id.depositReminderBtn);
		editMemberBtn = (Button) this.findViewById(R.id.editMember);
		testBtn = (Button) this.findViewById(R.id.button6);
		rankingBtn = (Button) this.findViewById(R.id.rankingBtn);
		backupBtn = (Button) this.findViewById(R.id.backupBtn);
		restoreBtn = (Button) this.findViewById(R.id.restoreBtn);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initButtons();
		
		addListenerOnButton(editTurnoutBtn, MainActivity.this, TurnoutActivity.class);
		addListenerOnButton(reckonBtn, MainActivity.this, ComputeActivity.class);
		addListenerOnButton(editMemberBtn, MainActivity.this, MemberListActivity.class);
		addListenerOnButton(testBtn, MainActivity.this, MainTestActivity.class);
		addListenerOnButton(depositReminderBtn, MainActivity.this,DepositReminderActivity.class);
		addListenerOnButton(editAcctBtn, MainActivity.this,EditAcctActivity.class);
		addListenerOnButton(rankingBtn, MainActivity.this,RankingActivity.class);
		addListenerOnButton(restoreBtn, MainActivity.this,RestoreDBActivity.class);
		
		backupBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				Utilities.backupDB(_self, true);
			}
		});
	}
	


}
