package com.zack.financemgr.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;


import weibo4j.Weibo;

import weibo4j.http.AccessToken;

import android.content.Intent;
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
import android.widget.Button;

import com.zack.persistent.entity.Member;
import com.zack.utilities.Constants;
import com.zack.utilities.Utilities;

public class AuthActivity extends BaseActivity {

	private static final String OAUTH_VERIFIER = "oauth_verifier";
	//public static final String CALLBACK_URL = "myapp://AuthActivity";
	public static final String CALLBACK_URL = "http://weibo.com/zaczhang";

	private Button backBtn;

	private AccessToken accessToken;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

//		authBtn = (Button) this.findViewById(R.id.authBtn);
//		addListenerOnAuthButton();

		backBtn = (Button) this.findViewById(R.id.backBtn);
		
		Intent intent = this.getIntent();
		Uri uri = intent.getData();
		if (uri == null)
			return;

		String verifier = uri.getQueryParameter(OAUTH_VERIFIER);
		/*reqToken = com.zack.utilities.Utilities.reqToken;
		accessToken = WebOAuth.requstAccessToken(reqToken, verifier);*/

		this.addListenerOnButton(backBtn, _self, MainActivity.class);
		
		this.updateWeibo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_auth, menu);
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	private void updateWeibo() {
		/*Weibo weibo = new Weibo();
		weibo.setToken(accessToken.getToken(), accessToken.getTokenSecret());
		try {
			
			String path = Environment.getExternalStorageDirectory()
					.toString();
			File file = new File(path, Constants.ACTIVITY_WEIBO_PIC_PATH);
			Status status = weibo.updateStatus("s");			
			//Status status = weibo.uploadStatus("Weirdo Rankings : ", file);
			
			List<Member> presentMembers = Utilities.presentMembers;
			if(Utilities.isNullOrEmptyList(presentMembers))
			{
				return;
			}
			
			String repostStatus = "";
			
			for(Member m : presentMembers)
			{
				if(!Utilities.isStringNullOrEmpty(m.getWeibo()))
				{
					repostStatus = "@" + m.getWeibo() + " ";
				}									
			}
			
			weibo.repost(String.valueOf(status.getId()), repostStatus);
			
			Utilities.showToast("微博发送成功哦~", this);
		} catch (WeiboException e) {
			Utilities.showToast(e.getMessage(), this);
		}*/
	}

	public void addListenerOnAuthButton() {

		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				/*Bitmap bitmap = Bitmap.createBitmap(480, 800,
						Bitmap.Config.ARGB_8888);
				Paint paint = new Paint();
				paint.setTextSize(20);
				paint.setColor(Color.CYAN);
				Canvas canvas = new Canvas(bitmap);
				canvas.drawText("Lingo屌丝团屌丝指数排行榜", 0, 20, paint);
				canvas.drawText("1 Alpha    98", 50, 50, paint);
				canvas.drawText("2 Jacky    90", 50, 80, paint);
				canvas.drawText("3 Zack     86", 50, 110, paint);
				canvas.drawText("4 Ailsa    82", 50, 140, paint);
				canvas.drawText("5 Gillian  80", 50, 170, paint);
				canvas.drawText("恭喜所有上榜屌丝!", 0, 200, paint);	
				canvas.drawText("你有幸成为Zack的安卓APP的测试数据:)", 0, 230, paint);
				//canvas.drawText("TEXT", 0, 50, paint);

				String path = Environment.getExternalStorageDirectory()
						.toString();
				OutputStream fOut = null;
				File file = new File(path, "FitnessGirl.jpg");
				try {
					if(!file.exists()){
						file.createNewFile();
					}
					fOut = new FileOutputStream(file);

					bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
					fOut.flush();
					fOut.close();

					MediaStore.Images.Media.insertImage(getContentResolver(),
							file.getAbsolutePath(), file.getName(),
							file.getName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				reqToken = WebOAuth.request(CALLBACK_URL);
				com.zack.utilities.Utilities.reqToken = reqToken;
				String authUrl = reqToken.getAuthorizationURL();

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(authUrl));
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);*/
			}
		});
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		int i = 0;
	}

}
