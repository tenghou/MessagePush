package com.example.jpushdemo;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import  com.scu.messagepush.R;

public class TestActivity extends Activity {
	
	private TextView msgText;
	private Button mButton;
	DatabaseHelper UDBHelper = new DatabaseHelper(this);
	Cursor UCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.single_message);
		initView();
		
		String timeString = getTime();
		
		Intent intent = getIntent();
        if (null != intent) {
	        Bundle bundle = getIntent().getExtras();
	        //String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
	        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
	        String showcontentString = timeString + "\n"+"     "+content+"\n";
	        	        
	        msgText.setText("\n"+showcontentString);

	        //将本条消息存入数据库
	        ContentValues cv = new ContentValues();
	        cv.put("content", showcontentString);
			UDBHelper.insert(cv);
			
	        
        }
        
        //点击返回主页
        mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
			}
		});
        
	}
	
	private void initView(){
		msgText = (TextView)findViewById(R.id.msg_text);
		mButton = (Button)findViewById(R.id.back);
		
	}
	
	//获取用户点击查看通知时的日期
	private String getTime(){
		
		Time t=new Time();  
		t.setToNow();
		int year = t.year;  
		int month = t.month+1;//月份默认从0开始  
		int date = t.monthDay;
		String timeString = "【"+year+"年"+month+"月"+date+"日"+"】";
		return timeString;
	}
	
	
	
	
	
	

}


