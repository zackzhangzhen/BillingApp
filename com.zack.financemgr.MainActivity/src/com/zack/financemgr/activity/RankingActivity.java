package com.zack.financemgr.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import weibo4j.Oauth;
import weibo4j.examples.timeline.Upload;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

import com.zack.persistent.dao.MemberDAO;
import com.zack.persistent.entity.Member;
import com.zack.utilities.Constants;
import com.zack.utilities.Utilities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class RankingActivity extends BaseActivity {

	public WebView webview;
	
	List<Member> membersCurPt;
	List<Member> membersLastPt;
	List<String> rankingStrList = new ArrayList<String>();
	TextView rankingTv;
	
	Button okBtn;
	Button backBtn;
	String msg = "N/A";
	
	private void init()
	{
		initDAL();
		initMsg();		
		initAuthWebView();
		initComponents();
	}
	
	private void initDAL()
	{
    	MemberDAO dao = new MemberDAO(this);
    	membersCurPt = dao.queryAllByCurPtDesc();	
    	membersLastPt = dao.queryAllByLastPtDesc();
		
	}
	
	private void initMsg()
	{		
		if(!Utilities.isNullOrEmptyList(membersCurPt))
		{		
			msg = "";
			String message = Utilities.getHeaderWithDate("最新屌丝度排行榜");			
			this.rankingStrList.add(message);
			
			for(int i = 0; i < membersCurPt.size(); i++)
			{
				if(!Utilities.isNullOrEmptyList(membersLastPt))
				{
					for(int j = 0; j < membersLastPt.size(); j++)
					{
						if(membersCurPt.get(i).equals(membersLastPt.get(j)))
						{
							int up = i - j;
							
							membersCurPt.get(i).setUp(up);
							
							break;
						}
					}
				}
			}
			
			Member maxUpMember = null;
			for(int i = 0; i < membersCurPt.size(); i++)
			{
				Member m = membersCurPt.get(i);
				
				if(maxUpMember == null || m.getUp() < maxUpMember.getUp())
				{
					maxUpMember = m;
				}
				
				
				String up = "";
				if(m.getUp() < 0)
				{
					up += Constants.UP + Math.abs(m.getUp());
				}
				else if(m.getUp() > 0)
				{
					up += Constants.DOWN + m.getUp();
				}
				else
				{
					up = "N/A";
				}
				String ranking = i+1 + " " + m.getName() + " " + m.getCurPt() + " " + up;
				this.rankingStrList.add(ranking);
			}
			
			if(membersCurPt.get(0).equals(maxUpMember))
			{
				Member m = membersCurPt.get(0);
				String congrats1 = "恭喜" + m.getName() + "获得给自己独点一个菜" ;
				this.rankingStrList.add(congrats1);
				
				String congrats2 = "和给任意一位屌丝点一碗饭的机会！";
				this.rankingStrList.add(congrats2);
			}
			else
			{
				Member m = membersCurPt.get(0);
				String congrats1 = "恭喜" + m.getName() + "获得给自己独点一个菜的机会！";
				this.rankingStrList.add(congrats1);
				
				String congrats2 = "恭喜" + maxUpMember.getName() + "获得给任意一位屌丝点一碗饭的机会！";
				this.rankingStrList.add(congrats2);
			}
		}
		
		for(String str : rankingStrList)
		{
			msg += str + "\r\n";
		}
	}


	private void updateMemberPoint()
	{	
		if(!Utilities.isNullOrEmptyList(membersCurPt))
		{
			MemberDAO dao = new MemberDAO(this);
			for(Member m : membersCurPt)
			{
				m.setLastPt(m.getCurPt());
				
				dao.updateEntity(m, m.getId());
			}
		}
	}
	
    private void initAuthWebView()
    {
    	webview = (WebView) this.findViewById(R.id.authWebView);
    	webview.setWebViewClient(new WebViewClient() {
			

			
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				
				if (url.startsWith(AuthActivity.CALLBACK_URL)) {
				
					//rankingTv.setText("1");

					if(!saveRankingWeiboPic())
					{
						Utilities.showToast("存屌丝微博图片失败！将不会发送微博！", _self);
						return;
					}
					
					//rankingTv.setText("2");
					
					Uri uri = Uri.parse(url);
					String code = uri.getQueryParameter(Constants.CODE);

					//rankingTv.setText("3");
					
					Oauth oauth = new Oauth();
					AccessToken accessToken;
					
					//rankingTv.setText("4");
					try {
						accessToken = oauth.getAccessTokenByCode(code);
						//rankingTv.setText("4.1");
						Upload.updateMsgWithFile(accessToken.getAccessToken(), Utilities.getWeiboMsg("屌丝度最新排行榜 " + new Date().toLocaleString() + " " , membersCurPt), Constants.RANKING_WEIBO_PIC_PATH, rankingTv);
						
						Utilities.showToast("屌丝排行榜微博发送成功哦，亲！",_self);
					} catch (WeiboException e) {
						//rankingTv.setText("4.2");
						Utilities.showToast(e.getMessage(), _self);
					}

				}
				super.onPageStarted(view, url, favicon);
			}
		});
    }
	
	private boolean saveRankingWeiboPic() {
		if (Utilities.isNullOrEmptyList(this.rankingStrList)) {
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

			for (String str : rankingStrList) {
				y += txtHeight;
				canvas.drawText(str, 0, y, paint);
			}

			String path = Environment.getExternalStorageDirectory().toString();
			OutputStream fOut = null;
			File file = new File(path, Constants.RANKING_WEIBO_PIC_PATH);

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
    
	private void initComponents() {    	
		
		rankingTv = (TextView)this.findViewById(R.id.rankingTv);
		rankingTv.setText(msg);
		
		okBtn = (Button)this.findViewById(R.id.okBtn);	
		okBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				updateMemberPoint();
				
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
		
		backBtn = (Button)this.findViewById(R.id.backBtn);		
		this.addListenerOnQuitButton(backBtn);
	}
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ranking, menu);
        return true;
    }

    
}
