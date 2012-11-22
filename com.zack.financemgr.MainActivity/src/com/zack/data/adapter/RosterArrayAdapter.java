package com.zack.data.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zack.financemgr.activity.BaseActivity;
import com.zack.financemgr.activity.ComputeActivity;
import com.zack.financemgr.activity.R;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Utilities;

public class RosterArrayAdapter extends ArrayAdapter<Member> {

	public static final String PRESENT_MEMBER_ID_STR = "PRESENT_MEMBER_ID_STR";
	public static final String PLUS_ONE_MEMBER_ID_STR = "PLUS_ONE_MEMBER_ID_STR";
	public static final String ABSENT_MEMBER_ID_STR = "ABSENT_MEMBER_ID_STR";
	public static final String PLUS_ONE_ABSENT_MEMBER_ID_STR = "PLUS_ONE_ABSENT_MEMBER_ID_STR";
	
	private final Context context;
	
	private List<Member> members;
	private Set<Member> presentMembers = new HashSet<Member>();
	private Set<Member> p1AbsentMembers = new HashSet<Member>();
	
	
	private Intent intent;
	
	public RosterArrayAdapter(Context theContext, int resource, List<Member> objects, Intent incomingIntent) {
		super(theContext, resource, objects);

		this.context = theContext;
		members = objects;
		intent = incomingIntent;
	}	
	
	public boolean onComputeClick()
	{
		try
		{
			if(Utilities.isNullOrEmptyList(members))
			{
				Utilities.showToast("会员都没有，哪来的到场会员？", (Activity)context);
				return false;
			}		
			
			List<Member> appointedMembers = getAppointedMembers(members);
			
			String presentMemberIdStr = Utilities.getIdStr(presentMembers);
			
			List<Member> p1Members = getP1Members(members);
			
			String p1MemberIdStr = Utilities.getIdStr(p1Members);
			
			List<Member> absentMembers = getAbsentMembers(appointedMembers, presentMembers);
			
			String absentMemberIdStr = Utilities.getIdStr(absentMembers);
			
			String p1AbsentMemberIdStr = Utilities.getIdStr(p1AbsentMembers);			
			
			String membersStr = getMembersStr(Utilities.setToList(presentMembers), absentMembers);

			intent.putExtra(ComputeActivity.MEMBERS_STR,membersStr);
			intent.putExtra(PRESENT_MEMBER_ID_STR, presentMemberIdStr);
			intent.putExtra(PLUS_ONE_MEMBER_ID_STR, p1MemberIdStr);
			intent.putExtra(PLUS_ONE_ABSENT_MEMBER_ID_STR, p1AbsentMemberIdStr);
			intent.putExtra(ABSENT_MEMBER_ID_STR, absentMemberIdStr);
			intent.putExtra(ComputeActivity.MEMBER_COUNT, Utilities.isNullOrEmptySet(presentMembers)?-1 : presentMembers.size());
			
	
			
			intent.setClass(context, ComputeActivity.class);
			context.startActivity(intent);		
			
			return true;
		}
		catch(Exception ex)
		{
			Utilities.showToast(ex.getMessage(), (Activity)context);
			
			return false;
		}
	}
	
	
	private String getMembersStr(List<Member> presentMembers,
			List<Member> absentMembers) {
		String str=  "";
		if(!Utilities.isNullOrEmptyList(presentMembers))
		{
			str+= "到场： ";
			
			for(Member m : presentMembers)
			{
				str += m.getName() + "  ";
			}
			
			str+="\r\n";
		}
		
		if(!Utilities.isNullOrEmptyList(absentMembers))
		{
			str+= "缺席： ";
			
			for(Member m : absentMembers)
			{
				str += m.getName() + "  ";
			}
		}
		
		return str;
	}

	private List<Member> getAbsentMembers(List<Member> appointedMembers,
			Set<Member> presentMembers) {
		if(Utilities.isNullOrEmptyList(appointedMembers))
		{
			return null;
		}
		
		if(Utilities.isNullOrEmptySet(presentMembers))
		{
			return appointedMembers;
		}
		
		List<Member> absentMembers = new ArrayList<Member>();
		
		for(Member m : appointedMembers)
		{
			if(!presentMembers.contains(m))
			{
				absentMembers.add(m);
			}
		}
		
		return absentMembers;
	}

	private List<Member> getP1Members(List<Member> members) {
		if(Utilities.isNullOrEmptyList(members))
		{
			return null;
		}
		
		List<Member> p1Members = new ArrayList<Member>();
		
		for(Member m : members)
		{
			if(m.getPlusOne() == 1)
			{
				p1Members.add(m);
			}
		}
		
		return p1Members;
	}

	private List<Member> getAppointedMembers(List<Member> members) {
		if(Utilities.isNullOrEmptyList(members))
		{
			return null;
		}
		
		List<Member> appointedMembers = new ArrayList<Member>();
		
		for(Member m : members)
		{
			if(m.getCome() == 1)
			{
				appointedMembers.add(m);
			}
		}
		
		return appointedMembers;
	}

	private void fillSingleEntry(View rowView, BaseActivity activity, final Member member, int position)
	{
		 //========================================================================
		 TextView memberName = (TextView) rowView.findViewById(R.id.txtMemberName);
		 memberName.setText(member.getName());
		 
		 //========================================================================
		 
		 CheckBox plusOneCb = (CheckBox) rowView.findViewById(R.id.plusOneCb);
		 plusOneCb.setTag(position);
		 
		 CheckBox comeCb = (CheckBox) rowView.findViewById(R.id.comeCb);
		 comeCb.setTag(position);
		 
		 CheckBox absentCb = (CheckBox) rowView.findViewById(R.id.p1AbsentCb);
		 absentCb.setTag(position);
		 
		//==============================================================
		 if(member.getCome() == 1)
		 {
			 comeCb.setChecked(true);
			 presentMembers.add(member);
			
		 }
		 else
		 {
			 comeCb.setChecked(false);
			 presentMembers.remove(member);
		 } 		 
		 
		//==============================================================
		 if(member.getPlusOne() == 1)
		 {
			 plusOneCb.setChecked(true);
			 
			 absentCb.setVisibility(View.VISIBLE);
		 }
		 else
		 {
			 plusOneCb.setChecked(false);
			 absentCb.setVisibility(View.GONE);
		 }
		 
		comeCb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				
				int pos = (Integer)cb.getTag();
				
				if (cb.isChecked()) {
					presentMembers.add(members.get(pos));
					
				} else {
					presentMembers.remove(members.get(pos));
				}
			}
		});

		//==============================================================
		
		if(member.getPlusOne() == 1)
		{
			plusOneCb.setChecked(true);
		}
		else
		{
			plusOneCb.setChecked(false);
		}

		absentCb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				
				int pos = (Integer)cb.getTag();
				
				if (cb.isChecked()) {
					p1AbsentMembers.add(members.get(pos));
					
				} else {
					p1AbsentMembers.remove(members.get(pos));
				}
			}
		});
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		 Member member = this.getItem(position);
		 BaseActivity activity = (BaseActivity)context;
		 
		 View rowView = convertView;
		 
		 if(rowView != null)
		 {			 
			 fillSingleEntry(rowView, activity, member, position);
			 
			 return rowView;
		 }
		 
		 LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 rowView = inflater.inflate(R.layout.roster_entry, parent, false);
		 
		 fillSingleEntry(rowView, activity, member, position);
		 
		 return rowView;
	}

}
