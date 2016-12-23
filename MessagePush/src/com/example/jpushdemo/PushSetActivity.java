package com.example.jpushdemo;


import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

import  com.scu.messagepush.R;

import cn.jpush.android.api.TagAliasCallback;


public class PushSetActivity extends InstrumentedActivity implements OnClickListener {
    private static final String TAG = "JPush";
    
	Button mSetTag;
	Button mSetAlias;
	TextView mTag;
	TextView mAlias;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		//去掉标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.push_set_dialog);
		init();
		initListener();
	}
	
	private void init(){
		mSetTag = (Button)findViewById(R.id.bt_tag);
	    mSetAlias = (Button)findViewById(R.id.bt_alias);
	    mTag = (TextView)findViewById(R.id.et_tag);
	    mAlias = (TextView)findViewById(R.id.et_alias);
	}
	
	private void initListener(){
		mSetTag.setOnClickListener(this);
		mSetAlias.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_tag:
			setTag();
			break;
		case R.id.bt_alias:
			setAlias();
			break;
		}
	}
	
	private void setTag(){
		EditText tagEdit = (EditText) findViewById(R.id.et_tag);
		String tag = tagEdit.getText().toString().trim();
		
        // 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			Toast.makeText(PushSetActivity.this,R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		
		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
				Toast.makeText(PushSetActivity.this,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
				return;
			}
			tagSet.add(sTagItme);
		}
		
		//调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	} 
	
	private void setAlias(){
		EditText aliasEdit = (EditText) findViewById(R.id.et_alias);
		String alias = aliasEdit.getText().toString().trim();
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(PushSetActivity.this,R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(PushSetActivity.this,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		
		//调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "设置成功！";
                Log.i(TAG, logs);
                mAlias.setFocusable(false);
                //隐藏输入法
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PushSetActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
                
            case 6002:
                logs = "时间超时，设置失败. 请一分钟后再试！";
                Log.i(TAG, logs);
                if (ExampleUtil.isConnected(getApplicationContext())) {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                } else {
                	Log.i(TAG, "无网络！");
                }
                break;
            
            default:
                logs = "设置失败，错误码： " + code;
                Log.e(TAG, logs);
            }
            
            ExampleUtil.showToast(logs, getApplicationContext());
        }
	    
	};
	
    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
            case 0:
                logs = "设置成功！";
                Log.i(TAG, logs);
                mTag.setFocusable(false);//输入框失去焦点
                //隐藏输入法
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PushSetActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
                
            case 6002:
                logs = "时间超时，设置失败. 请一分钟后再试！";
                Log.i(TAG, logs);
                if (ExampleUtil.isConnected(getApplicationContext())) {
                	mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                } else {
                	Log.i(TAG, "无网络！");
                }
                break;
            
            default:
                logs = "设置失败，错误码： " + code;
                Log.e(TAG, logs);
            }
            
            ExampleUtil.showToast(logs, getApplicationContext());
        }
        
    };
    
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	
	

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_SET_ALIAS:
                Log.d(TAG, "Set alias in handler.");
                JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                break;
                
            case MSG_SET_TAGS:
                Log.d(TAG, "Set tags in handler.");
                JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                break;
                
            default:
                Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
	
}