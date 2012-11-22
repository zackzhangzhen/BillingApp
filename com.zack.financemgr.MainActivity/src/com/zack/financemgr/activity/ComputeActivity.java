package com.zack.financemgr.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zack.data.adapter.RosterArrayAdapter;
import com.zack.utilities.Utilities;

public class ComputeActivity extends BaseActivity {

	private Date date;
	private float ball = -1;
	private float court = -1;
	private float water = -1;
	private long nonMemberCount = -1;
	private String membersStr;
	private long memberCount = -1;
	private float avgBall = -1;
	private float avgCourt = -1;
	private float avgMemberWater = -1;
	private float avgMemberTotal = -1;
	private float avgNonMemberTotal = -1;
	private float avgNonMemberCourtesy = -1;
	private float avgNonMemberActualTotal = -1;
	
	private String presentMemberIdStr;
	private String p1MemberIdStr;
	private String absentMemberIdStr;
	private String p1AbsentMemberIdStr;
	
	public static final String BALL = "ball";
	public static final String COURT = "court";
	public static final String WATER = "water";
	public static final String NON_MEMBER_COUNT = "nonMemberCount";
	public static final String MEMBERS_STR = "membersStr";
	public static final String MEMBER_COUNT = "memberCount";
	public static final String AVG_BALL = "avgBall";
	public static final String AVG_COURT = "avgCourt";
	public static final String AVG_MEMBER_WATER = "avgMemberWater";
	public static final String AVG_MEMBER_TOTAL = "avgMemberTotal";
	public static final String AVG_NON_MEMBER_TOTAL = "avgNonMemberTotal";
	public static final String AVG_NON_MEMBER_COURTESY = "avgNonMemberCourtesy";
	public static final String AVG_NON_MEMBER_ACTUAL_TOTAL = "avgNonMemberActualTotal";
	
	private TextView dateTv;
	private TextView membersTv;
	private TextView memberCountTv;
	private EditText ballEt;
	private EditText courtEt;
	private EditText waterEt;
	private EditText nonMemberCountEt;
	private TextView avgBallTv;
	private TextView avgCourtTv;
	private TextView avgMemberWaterTv;
	private TextView avgMemberTotalTv;	
	private TextView avgNonMemberTotalTv;
	private TextView avgNonMemberCourtesyTv;
	private TextView avgNonMemberActualTotalTv;
	
	private Button editPresentMembersBtn;
	private Button computeBtn;
	private Button okBtn;
	private Button clearBtn;
	private Button backBtn;	
	
	private boolean populateFieldsWithBundleInfo(Bundle extras)
	{
		date = new Date();
		
		if(extras == null || extras.isEmpty())
		{
			return false;
		}
		
		try
		{			
			ball = extras.getFloat(BALL);
			court = extras.getFloat(COURT);		
			water = extras.getFloat(WATER);		
			nonMemberCount = extras.getLong(NON_MEMBER_COUNT);			
			avgBall = extras.getFloat(AVG_BALL);
			avgCourt = extras.getFloat(AVG_COURT);
			avgMemberWater = extras.getFloat(AVG_MEMBER_WATER);
			avgMemberTotal = extras.getFloat(AVG_MEMBER_TOTAL);
			avgNonMemberTotal = extras.getFloat(AVG_NON_MEMBER_TOTAL);
			avgNonMemberCourtesy = extras.getFloat(AVG_NON_MEMBER_COURTESY);
			avgNonMemberActualTotal = extras.getFloat(AVG_NON_MEMBER_ACTUAL_TOTAL);		
			
			memberCount = extras.getInt(MEMBER_COUNT);
			membersStr = extras.getString(MEMBERS_STR);
			presentMemberIdStr = extras.getString(RosterArrayAdapter.PRESENT_MEMBER_ID_STR);
			p1MemberIdStr = extras.getString(RosterArrayAdapter.PLUS_ONE_MEMBER_ID_STR);
			p1AbsentMemberIdStr = extras.getString(RosterArrayAdapter.PLUS_ONE_ABSENT_MEMBER_ID_STR);
			absentMemberIdStr = extras.getString(RosterArrayAdapter.ABSENT_MEMBER_ID_STR);
			
			return true;
		}
		catch(Exception ex)
		{
			Utilities.showToast(ex.getMessage(), this);
			return false;
		}

	}
	
	private Intent createIntent(Class<?> cls)
	{
		try
		{
			populateBasicFieldsFromUI();
			
			Intent intent = Utilities.newIntent(this, cls);
			
			intent.putExtra(BALL, ball);
			intent.putExtra(COURT, court);
			intent.putExtra(WATER, water);
			intent.putExtra(NON_MEMBER_COUNT, nonMemberCount);
			intent.putExtra(AVG_BALL, avgBall);
			intent.putExtra(AVG_COURT, avgCourt);
			intent.putExtra(AVG_MEMBER_WATER, avgMemberWater);
			intent.putExtra(AVG_MEMBER_TOTAL, avgMemberTotal);
			intent.putExtra(AVG_NON_MEMBER_TOTAL, avgNonMemberTotal);
			intent.putExtra(AVG_NON_MEMBER_COURTESY, avgNonMemberCourtesy);
			intent.putExtra(AVG_NON_MEMBER_ACTUAL_TOTAL, avgNonMemberActualTotal);	

			intent.putExtra(MEMBERS_STR, membersStr);
			intent.putExtra(MEMBER_COUNT, memberCount);
			intent.putExtra(RosterArrayAdapter.PRESENT_MEMBER_ID_STR, presentMemberIdStr);
			intent.putExtra(RosterArrayAdapter.PLUS_ONE_MEMBER_ID_STR, p1MemberIdStr);
			intent.putExtra(RosterArrayAdapter.PLUS_ONE_ABSENT_MEMBER_ID_STR, p1AbsentMemberIdStr);
			intent.putExtra(RosterArrayAdapter.ABSENT_MEMBER_ID_STR, absentMemberIdStr);
			return intent;
		}

		catch(Exception ex)
		{
			Utilities.showToast(ex.getMessage(), this);
			return null;
		}		
	}
	
	private void populateDateUI()
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateAsString = simpleDateFormat.format(date);		
		this.dateTv.setText(dateAsString);
	}
	
	private void setText(TextView tv, int val)
	{
		if( val != -1)
		{
			tv.setText(String.valueOf(val));
		}		
	}
	
	private void setText(TextView tv, float val)
	{
		if( val != -1f)
		{			
			tv.setText(String.format("%.2f", val));
		}		
	}
	
	private void setText(EditText et, int val)
	{
		if( val != -1)
		{
			et.setText(String.valueOf(val));
		}		
	}
	
	private void setText(EditText et, float val)
	{
		if( val != -1f)
		{						
			et.setText(String.format("%.2f", val));
		}		
	}
	
	private boolean populateUIWithFieldInfo()
	{		
		try
		{				
			membersTv.setText(membersStr);
			setText(memberCountTv, (int)memberCount);
			setText(ballEt, ball);
			setText(courtEt, court);
			setText(waterEt, water);
			setText(nonMemberCountEt, (int)nonMemberCount);			
			setText(avgBallTv, avgBall);
			setText(avgCourtTv, avgCourt);
			setText(avgMemberWaterTv, avgMemberWater);
			setText(avgMemberTotalTv, avgMemberTotal);
			setText(avgNonMemberTotalTv, avgNonMemberTotal);
			setText(avgNonMemberCourtesyTv, avgNonMemberCourtesy);
			setText(avgNonMemberActualTotalTv, avgNonMemberActualTotal);			

			return true;
		}
		catch(Exception ex)
		{
			Utilities.showToast(ex.getMessage(), this);
			return false;
		}

	}
	
	private void populateBasicFieldsFromUI()
	{
		ball = tryPopulateFieldFloat(ballEt.getText().toString());
		court = tryPopulateFieldFloat(courtEt.getText().toString());
		water = tryPopulateFieldFloat(waterEt.getText().toString());
		memberCount = tryPopulateFieldLong(memberCountTv.getText().toString());
		nonMemberCount = tryPopulateFieldLong(nonMemberCountEt.getText().toString());
	}
	
	private float tryPopulateFieldFloat(String str)
	{
		try
		{
			return Float.parseFloat(str);
		}
		catch (Exception ex)
		{
			return -1;
		}
	}
	
	private long tryPopulateFieldLong(String str)
	{
		try
		{
			return Long.parseLong(str);
		}
		catch (Exception ex)
		{
			return -1;
		}
	}
	
	private boolean compute()
	{			
		try
		{			
			if(!validateInput())
			{
				Utilities.showToast("该填的都填好再算拜托！", this);
				return false;
			}
			
			populateBasicFieldsFromUI();
			
			long totalTurnout = memberCount + nonMemberCount;			
			
			avgBall = ball/(float)totalTurnout;
			setText(avgBallTv,avgBall);
			
			avgCourt = court/(float)totalTurnout;
			setText(avgCourtTv, avgCourt);
			
			avgMemberWater = water/(float)memberCount;
			setText(avgMemberWaterTv, avgMemberWater);
			
			avgMemberTotal = avgBall + avgCourt + avgMemberWater;
			setText(avgMemberTotalTv, avgMemberTotal);
			
			avgNonMemberTotal = avgBall + avgCourt;
			setText(avgNonMemberTotalTv, avgNonMemberTotal);
			
			avgNonMemberActualTotal = Utilities.getCourtesy(avgNonMemberTotal);
			setText(avgNonMemberActualTotalTv, avgNonMemberActualTotal);
			
			avgNonMemberCourtesy = avgNonMemberActualTotal - avgNonMemberTotal;
			setText(avgNonMemberCourtesyTv, avgNonMemberCourtesy);

			return true;
		}
		catch(Exception ex)
		{
			Utilities.showToast(ex.getMessage(), this);
			return false;
		}

	}
	
	private boolean validateInput() {
		if(Utilities.isStringNullOrEmpty(ballEt.getText().toString()))
		{
			return false;
		}
		
		if(Utilities.isStringNullOrEmpty(courtEt.getText().toString()))
		{
			return false;
		}
		
		if(Utilities.isStringNullOrEmpty(waterEt.getText().toString()))
		{
			return false;
		}
		
		if(Utilities.isStringNullOrEmpty(memberCountTv.getText().toString()))
		{
			return false;
		}
		
		if(Utilities.isStringNullOrEmpty(nonMemberCountEt.getText().toString()))
		{
			return false;
		}
		
		return true;
	}

	private void assignResources()
	{
		dateTv = (TextView)this.findViewById(R.id.dateValTextView);
		membersTv = (TextView)this.findViewById(R.id.membersValTextView);
		memberCountTv = (TextView)this.findViewById(R.id.memberCountValTextView);
		ballEt = (EditText)this.findViewById(R.id.ballEditText);
		courtEt = (EditText)this.findViewById(R.id.courtEditText);
		waterEt = (EditText)this.findViewById(R.id.waterEditText);
		nonMemberCountEt = (EditText)this.findViewById(R.id.nonMemberCountEditText);
		avgBallTv = (TextView)this.findViewById(R.id.avgBallValTextView);
		avgCourtTv = (TextView)this.findViewById(R.id.avgCourtValTextView);
		avgMemberWaterTv = (TextView)this.findViewById(R.id.avgMemberWaterValTextView);
		avgMemberTotalTv = (TextView)this.findViewById(R.id.avgMemberTotalValTextView);
		avgNonMemberTotalTv = (TextView)this.findViewById(R.id.avgNonMemberTotalValTextView);
		avgNonMemberCourtesyTv = (TextView)this.findViewById(R.id.avgNonMemberCourtesyValTextView);
		avgNonMemberActualTotalTv = (TextView)this.findViewById(R.id.avgNonMemberActualTotalValTextView);		
		
		editPresentMembersBtn = (Button)this.findViewById(R.id.editActualComeBtn);
		computeBtn = (Button)this.findViewById(R.id.computeBtn);
		okBtn = (Button)this.findViewById(R.id.okBtn);
		clearBtn = (Button)this.findViewById(R.id.clearBtn);
		backBtn = (Button)this.findViewById(R.id.backBtn);
	}
	
	private void initComponents()
	{
		assignResources();
		
		Bundle extras = getIntent().getExtras();
		populateFieldsWithBundleInfo(extras);
		populateDateUI();
		populateUIWithFieldInfo();		
		

		editPresentMembersBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {				
				Intent intent = ((ComputeActivity)_self).createIntent(MemberRosterActivity.class);
				
				startActivity(intent);
			}
		});
		
		computeBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {				
				if(compute())
				{
					okBtn.setEnabled(true);
				}
			}
		});
				
		okBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				Intent intent = ((ComputeActivity)_self).createIntent(ComputeSummaryActivity.class);
				
				startActivity(intent);
			}
		});
		
		this.addListenerOnButton(backBtn, this, MainActivity.class);
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compute);
		
		initComponents();
	}
		

}
