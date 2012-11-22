package com.zack.persistent.sql;

import com.zack.persistent.dao.BaseDAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "weirdo_sns.db"; 
	private String table_name;
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private String db_create_prefix; 
		
	private String db_create_full_sql;

	
	private static final String db_create_member_sql = "create table member (id integer primary key autoincrement, name text not null, weibo  text, cell text not null, deposit float not null default 0, last_pt integer not null default 0, cur_pt integer not null default 0, come  integer default 0, plus_one  integer default 0, leader  integer default 0);";
	private static final String db_create_op_acct_sql = "create table op_acct (id integer primary key autoincrement, deposit float not null default 0);" ;
	private static final String db_create_ball_acct_sql = "create table ball_acct (id integer primary key autoincrement, deposit float not null default 0);" ;
	private static final String db_create_activity_acct_sql = "create table activity_acct (id integer primary key autoincrement, deposit float not null default 0);" ;
	
	public MySQLiteHelper(Context context, String db_create_sql, String tableName) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
		
		table_name = tableName;
		
		db_create_prefix = "create table "
				+ table_name + "(" + BaseDAO.COLUMN_ID
				+ " integer primary key autoincrement, ";
		
		db_create_full_sql = db_create_prefix + db_create_sql;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(db_create_member_sql);
		database.execSQL(db_create_op_acct_sql);
		database.execSQL(db_create_ball_acct_sql);
		database.execSQL(db_create_activity_acct_sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + table_name);
		onCreate(db);
	}

} 
