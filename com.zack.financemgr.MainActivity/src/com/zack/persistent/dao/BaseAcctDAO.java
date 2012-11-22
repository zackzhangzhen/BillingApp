package com.zack.persistent.dao;



import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zack.persistent.entity.Acct;
import com.zack.persistent.sql.MySQLiteHelper;
import com.zack.utilities.Utilities;

public class BaseAcctDAO extends BaseDAO<Acct> {

	
		public static final String COLUMN_DEPOSIT = "deposit";
		
		public static final String DB_CREATE_SQL_SUFFIX = COLUMN_DEPOSIT + " float not null default 0); ";
				
		public final String table_name; 
		
		public BaseAcctDAO(Context context, String tableName) {
			
			super(context, DB_CREATE_SQL_SUFFIX, tableName);		
			
			table_name = tableName;
			
			dbHelper = new MySQLiteHelper(context, DB_CREATE_SQL_SUFFIX  , table_name);
		}

		public float getDeposit()
		{	
			
			Acct acct = getSingleAcct();
			return acct.getDeposit();
		}
		
		public void updateDeposit(float val)
		{
			Acct acct = getSingleAcct();
			acct.setDeposit(val);
			
			this.updateEntity(acct, acct.getId());
		}
		
		public Acct getSingleAcct()
		{
			List<Acct> list = this.queryAllEntities();
			if(Utilities.isNullOrEmptyList(list))
			{
				Acct acct = new Acct();
				acct.setDeposit(0f);
				this.createEntity(acct);
			}
			
			list = this.queryAllEntities();
			return list.get(0);
		}
		
		@Override
		public Acct cursorToEntity(Cursor cursor) {
			Acct acct = new Acct();
			acct.setId(cursor.getLong(0));
			acct.setDeposit(cursor.getFloat(1));
			return acct;
		}

		@Override
		protected ContentValues getContentValues(Acct entity) {
			// TODO Auto-generated method stub
			ContentValues values = new ContentValues();
			values.put(COLUMN_DEPOSIT, entity.getDeposit());
			return values;
		}		
}
