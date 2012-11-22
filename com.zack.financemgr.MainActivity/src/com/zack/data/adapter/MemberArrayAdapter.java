package com.zack.data.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zack.financemgr.activity.BaseActivity;
import com.zack.financemgr.activity.MemberDetailActivity;
import com.zack.financemgr.activity.MemberListActivity;
import com.zack.financemgr.activity.R;
import com.zack.persistent.dao.MemberDAO;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Constants;
import com.zack.utilities.Utilities;

public class MemberArrayAdapter extends ArrayAdapter<Member> {

	private final Context context;
	
	public MemberArrayAdapter(Context theContext, int resource, List<Member> objects) {
		super(theContext, resource, objects);

		this.context = theContext;
	}
	
	private String getMemberDetailInfo(Member member)
	{
		String str = "余额 ：" + String.format("%.2f", member.getDeposit()) + " | 屌丝度 ：" + member.getCurPt();
		return str;
	}
	
	
	private void fillSingleEntry(View rowView, BaseActivity activity, final Member member)
	{
		 //========================================================================
		 TextView memberName = (TextView) rowView.findViewById(R.id.txtMemberName);
		 memberName.setText(member.getName());
		 
		 //========================================================================
		 TextView memberDetail = (TextView) rowView.findViewById(R.id.txtMemberDetail);
		 memberDetail.setText(getMemberDetailInfo(member));		 
		 
		 //========================================================================
		 Button editBtn = (Button) rowView.findViewById(R.id.editSingleMemberBtn);
		 activity.addListenerOnButton(editBtn, activity, MemberDetailActivity.class, 
				 null,null,
				 Utilities.getMap(Constants.ID, member.getId()),
				 null);
		 
		//========================================================================
		 Button deleteBtn = (Button) rowView.findViewById(R.id.deleteSingleMemberBtn);

		 DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
			    
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			        {
			            if(deleteMember(member.getId()))	
			            {
			            	Utilities.showToast("好吧...踢了...。", (Activity)context);
			            	BaseActivity ba = (BaseActivity)context;
			            	ba.startNewActivity(ba, MemberListActivity.class);
			            }
			            else
			            {
			            	Utilities.showToast("貌似木有踢成功...", (Activity)context);
			            }
			        		
			            break;	
			        }


			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			            break;
			        }
			    }
			};
		 
		 activity.addListenerOnButtonForDiag(deleteBtn, "真要踢人？", "当然", "再想想", deleteListener);
	}
	
	private boolean deleteMember(long id)
	{
		try
		{
			MemberDAO dao = new MemberDAO(context);
			dao.deleteEntity(id);
			
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		 Member member = this.getItem(position);
		 BaseActivity activity = (BaseActivity)context;
		 
		 View rowView = convertView;
		 
		 if(rowView != null)
		 {			 
			 fillSingleEntry(rowView, activity, member);
			 
			 return rowView;
		 }
		 
		 LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 rowView = inflater.inflate(R.layout.member_entry, parent, false);
		 
		 fillSingleEntry(rowView, activity, member);
		 
		 return rowView;
	}

}
