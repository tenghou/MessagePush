package com.example.jpushdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public final static int VERSION = 1;
	public final static String DBNAME="table.db";
	public static final String TABLE_NAME = "contenttable"; //表名称常量
	

	public DatabaseHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE "+TABLE_NAME+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT(144));");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	//增加操作 
	public void insert(ContentValues cv) { 
		SQLiteDatabase db = this.getWritableDatabase(); 
		db.insert(TABLE_NAME, null, cv); 
		db.close();
	} 
		
	//删除操作 
	public void delete(int id) { 
		SQLiteDatabase db = this.getWritableDatabase(); 
		String where = "_id" + " = ?"; 
		String[] whereValue ={ Integer.toString(id) }; 
		db.delete(TABLE_NAME, where, whereValue); 
		db.close();
	} 
	
	//查询操作
	public Cursor querySQL(String sql,String[] args){
		SQLiteDatabase db = this.getWritableDatabase(); 
		Cursor cursor=db.rawQuery(sql,args);
		return cursor;
	}
	
	//删除表
	public void drop() {
		SQLiteDatabase db = this.getWritableDatabase(); 
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME; 
		db.execSQL(sql); 
		onCreate(db); 
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
