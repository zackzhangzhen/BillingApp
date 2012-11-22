package com.zack.financemgr.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class BitmapActivity extends Activity {

	private ImageView imgView;
	
	private BitmapActivity _self;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        
        _self = this;
        addListenerOnBackButton();
        
        imgView = (ImageView) findViewById(R.id.imageView1);
        
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bufferfly);

        
        Bitmap bmOverlay = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), 
        	    Bitmap.Config.ARGB_8888);
        // create a canvas on which to draw
        Canvas canvas = new Canvas(bmOverlay);

        //canvas.drawBitmap(bm, 0, 0, null);
        
        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setTextSize(50);
        //paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        
        //canvas.drawPoint(0, 0, paint);
        canvas.drawText("You are here", 0, 50, paint);
        canvas.drawText("And I'm here", 10, 100, paint);

        // set the bitmap into the ImageView
        imgView.setImageBitmap(bmOverlay);


    }

	public void addListenerOnBackButton() {

		Button backBtn = (Button) findViewById(R.id.backBtn);

		backBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				_self.finish();

			}

		});

	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bitmap, menu);
        return true;
    }

    
}
