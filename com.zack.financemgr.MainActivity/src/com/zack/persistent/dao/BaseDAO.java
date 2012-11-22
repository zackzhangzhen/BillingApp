package com.zack.persistent.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.zack.utilities.Constants;

import com.zack.persistent.sql.MySQLiteHelper;

public abstract class BaseDAO<T> {

	protected SQLiteDatabase database;
	protected MySQLiteHelper dbHelper;
	
	public String table_name;	
	
	public static final String COLUMN_ID = "id";
	
	public BaseDAO(Context context, String db_create_sql_suffix, String table_name) {
		this.table_name = table_name;
		dbHelper = new MySQLiteHelper(context, db_create_sql_suffix, table_name);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();		
	}
	
	public void close() {
		dbHelper.close();
	}
	
	protected abstract ContentValues getContentValues(T entity);

	public void execSQL(String sql)
	{
		this.open();
		database.execSQL(sql);
		this.close();
	}
	
	public T createEntity(T entity) {
		
		ContentValues values = getContentValues(entity);
		
		if(values == null)
		{
			return null;
		}
		
		this.open();
		long insertId = database.insert(table_name, null,
				values);
		
		T result = this.queryEntityById(insertId);
		this.close();
		return result;

	}
	
	public void deleteEntity(long id) {
		this.open();
		database.delete(this.table_name, Constants.ID	+ " = " + id, null);
		this.close();
	}
	
	public void updateEntity(T entity, long id) {
		String whereClause = Constants.ID + " = " + id; 

		ContentValues values = getContentValues(entity);
		
		this.open();
		database.update(table_name, values, whereClause, null);
		this.close();
	}
	
	public List<T> queryAllEntities(String orderBy)
	{
		this.open();
		
		Cursor cursor = database.query(table_name,
				null, null, null,
				null, null, orderBy);
		
		List<T> entities = new ArrayList<T>();
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			T entity = cursorToEntity(cursor);
			entities.add(entity);
			cursor.moveToNext();
		}
		
		cursor.close();
		this.close();
		return entities;
	}
	
	public List<T> queryAllEntities()
	{
		return queryAllEntities(null);
	}
	
	public List<T> queryEntities(String whereClause)
	{
		this.open();
		Cursor cursor = database.query(table_name,
				null, whereClause, null,
				null, null, null);
		
		List<T> entities = new ArrayList<T>();
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			T entity = cursorToEntity(cursor);
			entities.add(entity);
			cursor.moveToNext();
		}
		
		cursor.close();
		
		this.close();
		return entities;
	}
	
	public T queryEntity(String whereClause)
	{
		this.open();
		Cursor cursor = database.query(table_name,
				null, whereClause, null,
				null, null, null);

		cursor.moveToFirst();
		
		if(cursor.isAfterLast())
		{
			return null;
		}
		
		T newEntity = cursorToEntity(cursor);
		cursor.close();
		this.close();
		return newEntity;
	}	
	
	public T queryEntityById(long id)
	{
		this.open();
		Cursor cursor = database.query(table_name,
				null, Constants.ID + "=" + id, null,
				null, null, null);

		cursor.moveToFirst();
		
		if(cursor.isAfterLast())
		{
			return null;
		}
		
		T newEntity = cursorToEntity(cursor);
		cursor.close();
		this.close();
		return newEntity;
	}	
	
	public abstract T cursorToEntity(Cursor cursor);
	
	public void dropTable()
	{
		this.open();
		database.execSQL("DROP TABLE IF EXISTS " + table_name);
		this.close();
	}
	
	public void createTable()
	{
		this.open();
		dbHelper.onCreate(database);
		this.close();
	}
}
