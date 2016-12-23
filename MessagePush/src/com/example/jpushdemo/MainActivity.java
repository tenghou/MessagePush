package com.example.jpushdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import cn.jpush.android.api.InstrumentedActivity;

import  com.scu.messagepush.R;

public class MainActivity extends InstrumentedActivity implements OnClickListener{

	private Button mSetting;
	private EditText msgText;
	private Button mHistory;
	
	public static boolean isForeground = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		initView();   
		registerMessageReceiver();  // used for receive msg
	}
	
	private void initView(){
		
		mSetting = (Button)findViewById(R.id.setting);
		mSetting.setOnClickListener(this);
		msgText = (EditText)findViewById(R.id.msg_rec);
		mHistory = (Button)findViewById(R.id.history);
		mHistory.setOnClickListener(this);
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			Intent intent = new Intent(MainActivity.this, PushSetActivity.class);
			startActivity(intent);
			break;
		case R.id.history:
			Intent intent2 = new Intent(MainActivity.this,MessageListActivity.class);
			startActivity(intent2);
			break;
		}
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "最新消息";
	public static final String KEY_EXTRAS = "extras";
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
              if (!ExampleUtil.isEmpty(extras)) {
            	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
              }
              setCostomMsg(showMsg.toString());
			}
		}
	}
	
	private void setCostomMsg(String msg){
		 if (null != msgText) {
			 msgText.setText(msg);
			 msgText.setVisibility(android.view.View.VISIBLE);
         }
	}

}