package com.zack.data.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zack.financemgr.activity.BaseActivity;
import com.zack.financemgr.activity.R;
import com.zack.persistent.dao.MemberDAO;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Utilities;

public class TurnoutArrayAdapter extends ArrayAdapter<Member> {

	private class Holder
	{
		public CheckBox cb;
		public int pos;
		public Holder(CheckBox cb, int pos)
		{
			this.cb = cb;
			this.pos = pos;
		}
	}
	
	private final Context context;
	
	private List<Member> members;
	
	public TurnoutArrayAdapter(Context theContext, int resource, List<Member> objects) {
		super(theContext, resource, objects);

		this.context = theContext;
		members = objects;
	}	
	
	public boolean onComputeClick()
	{
		try
		{
			if(Utilities.isNullOrEmptyList(members))
			{
				Utilities.showToast("会员都没有，统计啥？！", (Activity)context);
				return false;
			}			
			
			List<Member> comeMembers = new ArrayList<Member>();
			List<Member> nonComeMembers = new ArrayList<Member>();
			List<Member> p1Members = new ArrayList<Member>();
			List<Member> nonP1Members = new ArrayList<Member>();
			for(Member m : members)				
			{
				if(m.getCome() == 0)
				{
					nonComeMembers.add(m);
				}
				else
				{
					comeMembers.add(m);
				}
				
				if(m.getPlusOne() == 0)
				{
					nonP1Members.add(m);
				}
				else
				{
					p1Members.add(m);
				}
			}
			
			updateTurnoutStatus(MemberDAO.COLUMN_COME, nonComeMembers, "0");
			updateTurnoutStatus(MemberDAO.COLUMN_COME, comeMembers, "1");
			updateTurnoutStatus(MemberDAO.COLUMN_PLUS_ONE, nonP1Members, "0");
			updateTurnoutStatus(MemberDAO.COLUMN_PLUS_ONE, p1Members, "1");
			
			return true;
		}
		catch(Exception ex)
		{
			Utilities.showToast(ex.getMessage(), (Activity)context);
			
			return false;
		}
	}
	
	private void updateTurnoutStatus(String colName,
			List<Member> members, String val) {

			if(Utilities.isNullOrEmptyList(members))
			{
				return;
			}
			
			MemberDAO dao = new MemberDAO(context);
			
			String sql = "update " + dao.table_name + " set " + colName + " = " +  val + " where (" + 
					MemberDAO.COLUMN_ID + " = ";	
			
			int size = members.size();
			for(int i = 0; i < size; i ++)
			{
				Member m = members.get(i);
				sql += m.getId();
				
				if(i == size - 1)
				{
					sql += ")";
				}
				else
				{
					sql += " OR " + MemberDAO.COLUMN_ID + " = ";
				}
			}
			
			dao.execSQL(sql);
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
		 comeCb.setTag(new Holder(plusOneCb,position));
		 
		 if(member.getCome() == 1)
		 {
			 comeCb.setChecked(true);
			 plusOneCb.setVisibility(View.VISIBLE);
		 }
		 else
		 {
			 comeCb.setChecked(false);
			 plusOneCb.setVisibility(View.GONE);
		 } 		 
		 
		 
		comeCb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;


				Holder holder = (Holder) cb.getTag();
				Member member = members.get(holder.pos);

				CheckBox p1Cb = holder.cb;
				
				if (cb.isChecked()) {
					
					member.setCome(1);
					p1Cb.setVisibility(View.VISIBLE);
					
				} else {
					member.setCome(0);
					member.setPlusOne(0);
					p1Cb.setVisibility(View.GONE);
				}
			}
		});

		if(member.getPlusOne() == 1)
		{
			plusOneCb.setChecked(true);
		}
		else
		{
			plusOneCb.setChecked(false);
		}
		
		plusOneCb.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;

				int pos = (Integer) cb.getTag();
				Member member = members.get(pos);

				if (cb.isChecked()) {

					member.setPlusOne(1);
				} else {
					member.setPlusOne(0);
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
		 rowView = inflater.inflate(R.layout.turnout_entry, parent, false);
		 
		 fillSingleEntry(rowView, activity, member, position);
		 
		 return rowView;
	}

}
