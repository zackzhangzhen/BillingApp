package com.zack.utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import com.zack.persistent.entity.Member;

public class Utilities {

	public static List<Member> presentMembers;
	
	private Utilities()
	{}
	
	public static Intent newIntent(Context context, Class<?> cls)
	{
		Intent intent = new Intent();
		intent.setClass(context, cls);
		return intent;
	}
	
	public static void showToast(String value, Activity activity)
	{
		Toast toast = Toast.makeText(activity, value, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.show();
	}	
	
	public static void log(String msg)
	{
		System.out.println(msg);		
	}
	
	public static void logFormat(String format, Object... args)
	{
		String msg = String.format(format, args);
		
		System.out.println(msg);
	}
	
	public static boolean isNullOrEmptyList(List list)
	{
		return list == null || list.isEmpty();
	}
	
	public static boolean isNullOrEmptySet(Set set)
	{
		return set == null || set.isEmpty();
	}
	
	public static boolean isNullOrEmptyMap(Map map)
	{
		return map == null || map.isEmpty();
	}
	
	public static <T> Iterator  getMapEntriesIterator(Map<String, T> m)
	{
		Set<Entry<String,T>> entries = m.entrySet();
		
		Iterator<Entry<String,T>> iter = entries.iterator();
		
		return iter;
	}
	
	public static <T> Map<String, T> getMap(String key, T value)
	{
		Map<String, T> map = new HashMap<String, T>();
		map.put(key, value);
		
		return map;
	}
	
	public static void putExtraString(Intent intent, Map<String, String> nvPairs)
	{
		if(!Utilities.isNullOrEmptyMap(nvPairs))
		{
			Iterator<Entry<String,String>> iter = Utilities.getMapEntriesIterator(nvPairs);
			
			while(iter.hasNext())
			{
				Entry<String,String> entry = iter.next();
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public static void putExtraLong(Intent intent, Map<String, Long> nvPairs)
	{
		if(!Utilities.isNullOrEmptyMap(nvPairs))
		{
			Iterator<Entry<String,Float>> iter = Utilities.getMapEntriesIterator(nvPairs);
			
			while(iter.hasNext())
			{
				Entry<String,Float> entry = iter.next();
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public static void putExtraFloat(Intent intent, Map<String, Float> nvPairs)
	{
		if(!Utilities.isNullOrEmptyMap(nvPairs))
		{
			Iterator<Entry<String,Float>> iter = Utilities.getMapEntriesIterator(nvPairs);
			
			while(iter.hasNext())
			{
				Entry<String,Float> entry = iter.next();
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public static void putExtraBoolean(Intent intent, Map<String, Boolean> nvPairs)
	{
		if(!Utilities.isNullOrEmptyMap(nvPairs))
		{
			Iterator<Entry<String,Boolean>> iter = Utilities.getMapEntriesIterator(nvPairs);
			
			while(iter.hasNext())
			{
				Entry<String,Boolean> entry = iter.next();
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public static long getLong(Bundle extras, String key)
	{
		if (extras == null) {
			return Constants.INVALID_LONG;
		}

		long val = extras.getLong(key);
		
		return val;
	}
	
	public static float getFloat(Bundle extras, String key)
	{
		if (extras == null) {
			return Constants.INVALID_FLOAT;
		}

		float val = extras.getFloat(key);
		
		return val;
	}
	
	public static String getString(Bundle extras, String key)
	{
		if (extras == null) {
			return null;
		}

		String val = extras.getString(key);
		
		return val;
	}
	
	public static boolean getBoolean(Bundle extras, String key)
	{
		if (extras == null) {
			return false;
		}

		boolean val = extras.getBoolean(key);
		
		return val;
	}
	
	public static boolean isStringNullOrEmpty(String str)
	{
		return str == null || str.trim().length() == 0;
	}

	public static int getCourtesy(float avgNonMemberTotal) {
		
		if(avgNonMemberTotal % 5 == 0)
		{
			return (int)avgNonMemberTotal;
		}
		
		int dough = (int) Math.floor(avgNonMemberTotal);
		dough++;
		while(true)
		{
			if(dough % 5 == 0)
			{
				return dough;
			}
			
			dough++;
		}
	}
	
	public static String getIdStr(List<Member> members) {
		if(Utilities.isNullOrEmptyList(members))
		{
			return "";
		}
		
		String str = "";
		for(int i = 0; i < members.size(); i++)
		{
			Member m = members.get(i);
			
			str += m.getId();
			
			if(i != members.size() - 1 )
			{
				str += ";";
			}
		}
		
		return str;
	}
	
	public static String getIdStr(Set<Member> members) {
		if(Utilities.isNullOrEmptySet(members))
		{
			return "";
		}
		
		String str = "";
		Iterator<Member> iter = members.iterator();
		while(iter.hasNext())
		{
			Member m = iter.next();
			str += m.getId();
			
			if(iter.hasNext())
			{
				str += Constants.STRING_DELIMITER;
			}
		}
		
		return str;
	}
	
	public static List setToList(Set set)
	{
		if(Utilities.isNullOrEmptySet(set))
		{
			return null;
		}
		
		List list = new ArrayList();
		Iterator iter = set.iterator();
		while(iter.hasNext())
		{
			Object m = iter.next();
			list.add(m);
		}
		
		return list;
	}

	public static List<Member> getMembersFromIdStr(List<Member> members,
			String presentMemberIdStr) {

		if(Utilities.isNullOrEmptyList(members) || Utilities.isStringNullOrEmpty(presentMemberIdStr))
		{
			return null;
		}
		
		StringTokenizer tokenizer = new StringTokenizer(presentMemberIdStr,Constants.STRING_DELIMITER);
		Set<Long> set = new HashSet<Long>();
		while(tokenizer.hasMoreElements())
		{
			String id =  (String)tokenizer.nextElement();
			set.add(Long.parseLong(id));			
		}
		
		List<Member> resultMembers = new ArrayList<Member>();
		
		for(Member m : members)
		{
			if(set.contains(m.getId()))
			{
				resultMembers.add(m);
			}
		}
		
		return resultMembers;
	}

	public static String getNewLineString(String str) {
		if(Utilities.isStringNullOrEmpty(str))
		{
			return "";
		}
		
		return str + "\r\n";
	}
	
	public static String getWeiboMsg(String msg, List<Member> members) {
		
		String str = msg;
		if(!Utilities.isNullOrEmptyList(members))
		{
			for(Member m : members)
			
				str += "@" + m.getWeibo() + " ";
		}
		
		return str;
	}
	
	public static String getHeaderWithDate(String header) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateAsString = simpleDateFormat.format(date);	
		String msg = header +  "（" + dateAsString +"）";
		
		return msg;
	}
	
	public static File getDBFile(Context ctx)
	{
		return ctx.getDatabasePath(Constants.DB_FILE_NAME);
	}
	
	public static File getDBBackupDir()
	{
		String path = Environment.getExternalStorageDirectory().toString();
		
		path += "/" + Constants.DB_BACKUP_FOLDER;
		
		File db_back_dir = new File(path);
		if(!db_back_dir.exists())
		{
			db_back_dir.mkdir();
		}
		
		return db_back_dir;
	
	}
	
	public static void backupDB(Context ctx, boolean promptSuccess) {
		
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		String dateAsString = simpleDateFormat.format(date);	
		
		File dbFile = Utilities.getDBFile(ctx);
		File backupDir = Utilities.getDBBackupDir();
		String backupFileName = backupDir.getAbsolutePath() + "/" + dateAsString + ".db";
		File backupFile = new File(backupFileName);
		
		try
		{
	        if (dbFile.exists()) {
	            FileChannel src = new FileInputStream(dbFile).getChannel();
	            FileChannel dst = new FileOutputStream(backupFile).getChannel();
	            dst.transferFrom(src, 0, src.size());
	            src.close();
	            dst.close();
	        }
	        
	        if(promptSuccess)
	        {
	        	Utilities.showToast("备份数据成功！", (Activity)ctx);
	        }
	        
	        
		}
		catch(Exception ex)
		{
			Utilities.showToast("备份数据失败！", (Activity)ctx);
		}        
	}
}
