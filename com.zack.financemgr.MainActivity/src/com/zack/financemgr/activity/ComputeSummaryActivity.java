package com.zack.financemgr.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import weibo4j.Oauth;
import weibo4j.examples.timeline.Upload;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.zack.data.adapter.RosterArrayAdapter;
import com.zack.persistent.dao.ActivityAcctDAO;
import com.zack.persistent.dao.BallAcctDAO;
import com.zack.persistent.dao.BaseAcctDAO;
import com.zack.persistent.dao.MemberDAO;
import com.zack.persistent.dao.OpAcctDAO;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Constants;
import com.zack.utilities.Utilities;

public class ComputeSummaryActivity extends BaseActivity {

	float ball;
	float court;
	float water;
	long memberCount;
	long nonMemberCount;
	float avgCourt;
	float avgBall;
	float avgMemberTotal;
	float avgNonMemberTotal;
	float avgNonMemberCourtesy;
	String membersStr;
	
	String presentMemberIdStr;
	String p1MemberIdStr;
	String p1AbsentMemberIdStr;
	String absentMemberIdStr;
	
	float op_acct;
	float ball_acct;
	float activity_acct;
	
	List<Member> members;
	
	List<Member> presentMembers;
    List<Member> p1Members;
    List<Member> p1AbsentMembers;
    List<Member> absentMembers;
    
    String presentReport;
    String absentReport;
    String p1Report;
    String p1AbsentReport;
    
    
    float opAcctVal;
    String opAccttStr;
    float ballAcctVal;
    String ballAcctStr;
    float activityAcctVal;        
    String activityAcctStr; 
    
    String activityStatsStr;
    
    String msg;
    
	
    TextView reportSummaryTv;
    Button okBtn;
    Button weiboBtn;
    Button backBtn;
    Button backToMainBtn;
    
    List<String> activityWeiboMsgList = new ArrayList<String>();
    
    DialogInterface.OnClickListener okListener;
    
    public WebView webview;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute_summary);
        
        initFromDAL();
        initFromIncomingIntent();
        initRankingReports();
        initActivityStatsReport();
        initAcctReports(); 
        initComponents();
        this.initMemberPointAndDeposit();
        initMsg();
    }

    private void initMsg() {
		msg = this.getSMSMsg();
		activityWeiboMsgList = getActivityWeiboMsg();
        reportSummaryTv.setText(msg);
	}

    private void initAuthWebView()
    {
    	webview = (WebView) this.findViewById(R.id.authWebView);
    	webview.setWebViewClient(new WebViewClient() {
			
			/*public void onPageFinished(WebView view, String url) {
				
				super.onPageFinished(view, url);
			}*/

			
			/*public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				webview.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}*/

			
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				
				if (url.startsWith(AuthActivity.CALLBACK_URL)) {
					

//					view.cancelLongPress();
//					view.stopLoading();

					if(!saveActivityWeiboPic())
					{
						Utilities.showToast("���˿΢��ͼƬʧ�ܣ������ᷢ��΢����", _self);
						return;
					}
					
					
					Uri uri = Uri.parse(url);
					String code = uri.getQueryParameter(Constants.CODE);

					Oauth oauth = new Oauth();
					AccessToken accessToken;
					try {
						accessToken = oauth.getAccessTokenByCode(code);
						
						Upload.updateMsgWithFile(accessToken.getAccessToken(), Utilities.getWeiboMsg("��˿�ȸ��� " + new Date().toLocaleString() + " " , presentMembers), Constants.ACTIVITY_WEIBO_PIC_PATH,null);
						
					} catch (WeiboException e) {
						Utilities.showToast(e.getMessage(), _self);
					}

				}
				super.onPageStarted(view, url, favicon);
			}
		});
    }

	private void initComponents() {    	
		
		initAuthWebView();
		
		reportSummaryTv = (TextView)this.findViewById(R.id.reportSummaryTextView);
		okBtn = (Button)this.findViewById(R.id.okBtn);
		
		weiboBtn = (Button)this.findViewById(R.id.weiboBtn);
		weiboBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				Oauth oauth = new Oauth();
				String authUrl;
				try {
					authUrl = oauth.authorize(Constants.CODE);	
					webview.getSettings().setJavaScriptEnabled(true);
					webview.loadUrl(authUrl);
				} catch (WeiboException e) {
					Utilities.showToast(e.getMessage(), _self);
				}
			}
		});
		
		backToMainBtn = (Button)this.findViewById(R.id.backToMainBtn);
		
		okListener = new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE: {
					sendSMS();
					compute();
					Utilities.backupDB(_self, false);
					weiboBtn.setEnabled(true);
					webview.setVisibility(View.VISIBLE);
					//((BaseActivity)_self).startNewActivity(_self, MainActivity.class);
					break;
					}

				case DialogInterface.BUTTON_NEGATIVE: {
					compute();
					Utilities.backupDB(_self, false);
					weiboBtn.setEnabled(true);
					webview.setVisibility(View.VISIBLE);
					//((BaseActivity)_self).startNewActivity(_self, MainActivity.class);
					break;
					}
				}
			}
		};

		String stmt = "Ҫ�����Ÿ���˿���쵼�㷥������ͷ�쵼̫�࣬��m�㲻�ٶ��ŷ�Ӵ~";
		addListenerOnButtonForDiag(okBtn, stmt, "��Ȼ", "����", okListener);
		
		
		backBtn = (Button)this.findViewById(R.id.backBtn);	
		this.addListenerOnQuitButton(backBtn);
		
		this.addListenerOnButtonForResult(backToMainBtn, this, MainActivity.class);
	}



	protected void compute() {
		saveMemberPointAndDeposit();
		computeAccount();
	}

	/*protected void sendActivityWeibo() {
		
			if(!saveActivityWeiboPic())
			{
				Utilities.showToast("���˿΢��ͼƬʧ�ܣ������ᷢ��΢����", this);
			}
			
			reqToken = WebOAuth.request(AuthActivity.CALLBACK_URL);
			com.zack.utilities.Utilities.reqToken = reqToken;
			com.zack.utilities.Utilities.presentMembers = presentMembers;
			String authUrl = reqToken.getAuthorizationURL();

			Intent intent = new Intent(Intent.ACTION_VIEW, Uri
					.parse(authUrl));
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
	}*/
	
	private boolean saveActivityWeiboPic() {
		if (Utilities.isNullOrEmptyList(this.activityWeiboMsgList)) {
			return false;
		}
		try {
			Bitmap bitmap = Bitmap.createBitmap(480, 800,
					Bitmap.Config.ARGB_8888);
			Paint paint = new Paint();
			int txtHeight = 30;
			paint.setTextSize(txtHeight - 10);
			paint.setColor(Color.CYAN);
			Canvas canvas = new Canvas(bitmap);

			int y = 0;

			for (String str : activityWeiboMsgList) {
				y += txtHeight;
				canvas.drawText(str, 0, y, paint);
			}

			String path = Environment.getExternalStorageDirectory().toString();
			OutputStream fOut = null;
			File file = new File(path, Constants.ACTIVITY_WEIBO_PIC_PATH);

			if (!file.exists()) {
				file.createNewFile();
			}
			fOut = new FileOutputStream(file);

			bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
			fOut.flush();
			fOut.close();

			MediaStore.Images.Media.insertImage(getContentResolver(),
					file.getAbsolutePath(), file.getName(), file.getName());

			return true;

		} catch (Exception e) {
			Utilities.showToast(e.getMessage(), this);
			return false;
		}

	}

	private void initMemberPointAndDeposit() {
		for(Member m : members)
		{
			boolean scoreChanged = false;
			boolean memberPresent = false;
			int score = 0;
			if(!Utilities.isNullOrEmptyList(presentMembers) && presentMembers.contains(m))
			{
				score += 1;
				scoreChanged = true;
				memberPresent = true;
			}
			
			if(!Utilities.isNullOrEmptyList(absentMembers) && absentMembers.contains(m))
			{
				score -= 2;
				scoreChanged = true;
			}
			
			if(!Utilities.isNullOrEmptyList(p1Members)  && p1Members.contains(m))
			{
				score += 1;
				scoreChanged = true;
			}
			
			if(!Utilities.isNullOrEmptyList(p1AbsentMembers)  && p1AbsentMembers.contains(m))
			{
				score -= 2;
				scoreChanged = true;
			}
			
			/*if(!scoreChanged)
			{
				return;
			}*/
			
			m.setCurPt(m.getCurPt() + score);
			
			if(memberPresent)
			{
				m.setDeposit(m.getDeposit() - avgMemberTotal);
			}			
		}		
	}
	
	private void saveMemberPointAndDeposit() {
		for(Member m : members)
		{
			MemberDAO dao = new MemberDAO(this);
			dao.updateEntity(m, m.getId());
		}		
	}

	private void computeAccount() {
		updateAcctDeposit(new OpAcctDAO(this), opAcctVal);
		updateAcctDeposit(new BallAcctDAO(this), ballAcctVal);
		updateAcctDeposit(new ActivityAcctDAO(this), activityAcctVal);		
	}

	private void updateAcctDeposit(BaseAcctDAO dao, float val)
	{
		dao.updateDeposit(val);
	}
	
	private void initFromDAL() {
    	MemberDAO dao = new MemberDAO(this);
		members = dao.queryAllEntities();	
		
		
		OpAcctDAO opDao = new OpAcctDAO(this);
		op_acct = opDao.getDeposit();
		
		BallAcctDAO ballDao = new BallAcctDAO(this);
		ball_acct = ballDao.getDeposit();
		
		ActivityAcctDAO activityDao = new ActivityAcctDAO(this);
		activity_acct = activityDao.getDeposit();
		
		//MOCK
//		op_acct = 523f;
//		ball_acct = 210f;
//		activity_acct = 122f;
	}

	private void initActivityStatsReport() {
		
		float avgWater = water/(float)memberCount;
    	
		activityStatsStr = "��Ա����/����Ǯ/��Ǯ/ˮǮ/�ܶ�:" + memberCount + "/" + String.format("%.2f", avgCourt) + "/" + String.format("%.2f", avgBall) + "/" + String.format("%.2f", avgWater) + "/" + String.format("%.2f", avgMemberTotal) + "\r\n";
        
    	activityStatsStr+= "�ǻ�Ա����/����Ǯ/��Ǯ/����/�ܶ�:" + nonMemberCount + "/" + String.format("%.2f", avgCourt) + "/" + String.format("%.2f", avgBall) + "/" + String.format("%.2f", avgNonMemberCourtesy) + "/" + String.format("%.2f", avgNonMemberTotal) + "\r\n";
    	
	}
	
	private void initAcctReports() {
		
    	opAcctVal = op_acct + nonMemberCount*avgCourt - court - water - memberCount*avgBall;        
    	opAccttStr = "��Ӫ�˻���\r\n���ǻ�Ա����x�˾����ط�-���ط�-ˮǮ-��Ա����*�˾���Ǯ\r\n" + String.format("%.2f", op_acct) + "+" + nonMemberCount + "*" + String.format("%.2f", avgCourt) +  "-" + String.format("%.2f", court) + "-" + String.format("%.2f", water) +  "-" + memberCount + "*" + String.format("%.2f", avgBall) + "=" + String.format("%.2f", opAcctVal);
        
        ballAcctVal = ball_acct + ball;
        ballAcctStr = "��Ǯ�˻���\r\n���+��Ǯ\r\n" + String.format("%.2f", ball_acct) + "+" + String.format("%.2f", ball) + "=" + String.format("%.2f", ballAcctVal);
        
        activityAcctVal = activity_acct + (avgNonMemberCourtesy * nonMemberCount);        
        activityAcctStr = "��˻���\r\n���+���ǻ�Ա�˾�����x�ǻ�Ա������\r\n" +  String.format("%.2f", activity_acct) + "+(" + String.format("%.2f", avgNonMemberCourtesy) + "*" +  nonMemberCount + ") = "+ String.format("%.2f", activityAcctVal);
	}

	private void initRankingReports() {
    	presentReport = initReport(presentMembers, "���� ��+1����" );
    	absentReport = initReport(absentMembers, "ȱϯ ��-2����" );
    	p1Report = initReport(p1Members, "���� ��+1����" );
    	p1AbsentReport = initReport(p1AbsentMembers, "������ȱϯ ��-2����" );
	}

	private String initReport(List<Member> members, String header) {
	
		if(Utilities.isNullOrEmptyList(members))
		{
			return "";
		}
		
		String str = header;
		for(Member m : members)
		{
			str += m.getName() + "  ";
		}
		
		return str;
	}

	private void initFromIncomingIntent() {
		
    	Bundle extras = getIntent().getExtras();
        ball = extras.getFloat(ComputeActivity.BALL);
        court = extras.getFloat(ComputeActivity.COURT);
        water = extras.getFloat(ComputeActivity.WATER);
        memberCount = extras.getLong(ComputeActivity.MEMBER_COUNT);
        nonMemberCount = extras.getLong(ComputeActivity.NON_MEMBER_COUNT);
        avgCourt = extras.getFloat(ComputeActivity.AVG_COURT);
        avgBall = extras.getFloat(ComputeActivity.AVG_BALL);
        avgMemberTotal = extras.getFloat(ComputeActivity.AVG_MEMBER_TOTAL);
        avgNonMemberCourtesy = extras.getFloat(ComputeActivity.AVG_NON_MEMBER_COURTESY);
        membersStr = extras.getString(ComputeActivity.MEMBERS_STR);
        
        presentMemberIdStr = extras.getString(RosterArrayAdapter.PRESENT_MEMBER_ID_STR);
        p1MemberIdStr = extras.getString(RosterArrayAdapter.PLUS_ONE_MEMBER_ID_STR);
        p1AbsentMemberIdStr = extras.getString(RosterArrayAdapter.PLUS_ONE_ABSENT_MEMBER_ID_STR);
        absentMemberIdStr = extras.getString(RosterArrayAdapter.ABSENT_MEMBER_ID_STR);
        
        presentMembers = Utilities.getMembersFromIdStr(members, presentMemberIdStr);
        p1Members = Utilities.getMembersFromIdStr(members, p1MemberIdStr);
        p1AbsentMembers = Utilities.getMembersFromIdStr(members, p1AbsentMemberIdStr);
        absentMembers = Utilities.getMembersFromIdStr(members, absentMemberIdStr);
	}
	
	private void sendSMS() {
//		PendingIntent pi = PendingIntent.getActivity(this, 0,
//	            new Intent(this, Object.class), 0);   
		try
		{
	        SmsManager sms = SmsManager.getDefault();
	      
	        MemberDAO dao = new MemberDAO(this);
	        List<Member> leaders = dao.queryLeaders();
	        
	        if(Utilities.isNullOrEmptyList(leaders))
	        {
	        	Utilities.showToast("�쵼�����ģ�û���쵼�㷢���˱�����", this);
	        	return;
	        }
	                
	        ArrayList<String> messages = sms.divideMessage(msg);
	        
	        for(Member m : leaders)
	        {
	        	sms.sendMultipartTextMessage(m.getCell(), null, messages, null, null);
	        }
	        
	        /*sms.sendMultipartTextMessage("18801606064", null, messages, null, null);//Alpha
	        sms.sendMultipartTextMessage("13817669446", null, messages, null, null);//Ailsa
	        sms.sendMultipartTextMessage("15800323896", null, messages, null, null);//Jacky
	        sms.sendMultipartTextMessage("13816219852", null, messages, null, null);//Gillian
	        sms.sendMultipartTextMessage("13681936432", null, messages, null, null);//Zachary*/

	        Utilities.showToast("���ŷ��ͳɹ���\r\n\r\n" + msg, this);
		}
		catch(Exception ex)
		{
			Utilities.showToast("ʧ�ܣ�"+ ex.getMessage(), this);
		}

	}

	private List<String> getActivityWeiboMsg()
	{
		List<String> activityWeiboMsgList = new ArrayList<String>();		
		
		String msg = Utilities.getHeaderWithDate("��˿�Ż����");
		activityWeiboMsgList.add(msg);
		activityWeiboMsgList.add("��˿�ȸ��£�");
		
		if(!Utilities.isStringNullOrEmpty(presentReport))
		{
			activityWeiboMsgList.add(presentReport);
		}
		
		if(!Utilities.isStringNullOrEmpty(absentReport))
		{
			activityWeiboMsgList.add(absentReport);
		}
		
		if(!Utilities.isStringNullOrEmpty(p1Report))
		{
			activityWeiboMsgList.add(p1Report);
		}
		
		if(!Utilities.isStringNullOrEmpty(p1AbsentReport))
		{
			activityWeiboMsgList.add(p1AbsentReport);
		}	
		
		return activityWeiboMsgList;
	}
	
	private String getSMSMsg() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		
		String dateAsString = simpleDateFormat.format(date);	
		String msg = "��˿�Ż������" + dateAsString +"��\r\n\r\n";
		if(!(Utilities.isStringNullOrEmpty(presentReport) &&
				Utilities.isStringNullOrEmpty(absentReport) &&
				Utilities.isStringNullOrEmpty(p1Report) &&
				Utilities.isStringNullOrEmpty(p1AbsentReport)))
		{
			msg += "��˿�ȸ��£�\r\n\r\n" +  Utilities.getNewLineString(presentReport) + Utilities.getNewLineString(absentReport) + Utilities.getNewLineString(p1Report) + Utilities.getNewLineString(p1AbsentReport) + "\r\n";
		}		
		
		msg += "��ſ���\r\n\r\n" + Utilities.getNewLineString(activityStatsStr) + "\r\n";
		
		msg += "���񱨱���\r\n\r\n" + Utilities.getNewLineString(opAccttStr) + "\r\n" + Utilities.getNewLineString(ballAcctStr) + "\r\n" + Utilities.getNewLineString(activityAcctStr) + "\r\n";
		
		msg += getMemberDepositMsg();
		return msg;
	}
	
	private String getMemberDepositMsg()
	{
		if(!Utilities.isNullOrEmptyList(members))
		{
			msg = "��Ա��\r\n\r\n";
			for(Member m : members)
			{
				String str = m.getName() + "(" + String.format("%.2f", m.getDeposit()) +") "; 
				msg+=str;
			}
		}
		
		return msg;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_compute_summary, menu);
        return true;
    }

    
}