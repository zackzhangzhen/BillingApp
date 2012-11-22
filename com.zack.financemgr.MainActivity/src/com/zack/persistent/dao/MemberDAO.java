package com.zack.persistent.dao;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zack.persistent.entity.Member;
import com.zack.persistent.sql.MySQLiteHelper;

public class MemberDAO extends BaseDAO<Member> {

		/*private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
				MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_EMAIL};*/		
	
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_WEIBO = "weibo";
		public static final String COLUMN_CELL = "cell";
		public static final String COLUMN_DEPOSIT = "deposit";
		public static final String COLUMN_LAST_PT = "last_pt";
		public static final String COLUMN_CUR_PT = "cur_pt";
		public static final String COLUMN_COME = "come";		
		public static final String COLUMN_PLUS_ONE = "plus_one";
		public static final String COLUMN_LEADER = "leader";
		
		public static final String DB_CREATE_SQL_SUFFIX = COLUMN_NAME + " text not null, "
				+ COLUMN_WEIBO + " text, "
				+ COLUMN_CELL + " text not null, "
				+ COLUMN_DEPOSIT + " float not null default 0,"
				+ COLUMN_LAST_PT + " integer not null default 0,"
				+ COLUMN_CUR_PT + " integer not null default 0,"
				+ COLUMN_COME + " integer default 0,"
				+ COLUMN_PLUS_ONE + " integer default 0,"
				+ COLUMN_LEADER + " integer default 0);";
		public static final String TABLE_NAME = "member"; 
		
		public MemberDAO(Context context) {
			super(context, DB_CREATE_SQL_SUFFIX, TABLE_NAME);		
			
			dbHelper = new MySQLiteHelper(context, DB_CREATE_SQL_SUFFIX  , table_name);
		}

		@Override
		public Member cursorToEntity(Cursor cursor) {
			Member member = new Member();
			member.setId(cursor.getLong(0));
			member.setName(cursor.getString(1));
			member.setWeibo(cursor.getString(2));
			member.setCell(cursor.getString(3));
			member.setDeposit(cursor.getFloat(4));
			member.setLastPt(cursor.getInt(5));
			member.setCurPt(cursor.getInt(6));
			member.setCome(cursor.getInt(7));
			member.setPlusOne(cursor.getInt(8));
			member.setLeader(cursor.getInt(9));
			return member;
		}

		@Override
		protected ContentValues getContentValues(Member entity) {
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();
			//values.put(COLUMN_ID, entity.getId());
			values.put(COLUMN_NAME, entity.getName());
			values.put(COLUMN_WEIBO, entity.getWeibo());
			values.put(COLUMN_CELL, entity.getCell());
			values.put(COLUMN_DEPOSIT, entity.getDeposit());
			values.put(COLUMN_LAST_PT, entity.getLastPt());
			values.put(COLUMN_CUR_PT, entity.getCurPt());
			values.put(COLUMN_COME, entity.getCome());
			values.put(COLUMN_PLUS_ONE, entity.getPlusOne());
			values.put(COLUMN_LEADER, entity.getLeader());
			return values;
		}
		
		public List<Member> queryAllByCurPtDesc()
		{
			return this.queryAllEntities(COLUMN_CUR_PT + " DESC");
		}
		
		public List<Member> queryAllByLastPtDesc()
		{
			return this.queryAllEntities(COLUMN_LAST_PT + " DESC");
		}
		
		public List<Member> queryLeaders()
		{
			this.open();
			Cursor cursor = database.query(table_name,
					null, COLUMN_LEADER + " = 1", null,
					null, null, null);

			List<Member> leaders = new ArrayList<Member>();
			
			
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Member entity = cursorToEntity(cursor);
				leaders.add(entity);
				cursor.moveToNext();
			}
			
			cursor.close();
			this.close();
			return leaders;			
		}
}
