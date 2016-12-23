package com.example.jpushdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import  com.scu.messagepush.R;

public class MessageListActivity extends Activity {
	
	private TextView msgText;
	private Button mBackbButton;
	DatabaseHelper UDBHelper = new DatabaseHelper(this);
	Cursor UCursor1,UCursor2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_list);
		initView();
		
		//将数据库中的通知内容以横线分割方式拼接起来并输出
		//UCursor2 = UDBHelper.querySQL("select group_concat(content ,'-----------\n') from " + DatabaseHelper.TABLE_NAME , null);
		
		//将数据库中的通知内容按id号倒序输出，即发送时间倒序输出
		UCursor2 = UDBHelper.querySQL("select group_concat(ev.content ,'-----------\n') from (select content from " + DatabaseHelper.TABLE_NAME +" order by _id desc) AS ev", null);
		
		if (UCursor2.moveToFirst()) {
			String contentString = UCursor2.getString(0);
			msgText.setText("\n"+contentString);
			//设置内容可上下滑动
			msgText.setMovementMethod(ScrollingMovementMethod.getInstance());
		}
		
		//点击历史通知按钮即返回主页
		mBackbButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MessageListActivity.this, MainActivity.class);
                startActivity(intent);
			}
		});

	}
	
	private void initView(){
		msgText = (TextView)findViewById(R.id.msg_text);
		mBackbButton = (Button)findViewById(R.id.goback);
		
		deleteTheRedundantData();
		
	}
	
	//数据库中只保留十条消息
	private void deleteTheRedundantData(){
		UCursor1 = UDBHelper.querySQL("select count(_id),min(_id) from " + DatabaseHelper.TABLE_NAME , null);
		if (UCursor1.moveToFirst()) {
			if (UCursor1.getInt(0) > 10) {
				UDBHelper.delete(UCursor1.getInt(1));
			}
		}
	}
	
	@Override  
	protected void onDestroy() {  
	    super.onDestroy();  
	    if (UDBHelper  != null) {  
	    	UDBHelper.close();
	    	UCursor1.close();
	    	UCursor2.close();
	    }  
	} 
	
	

}


