package com.zack.persistent.entity;

public class DBEntry
{
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	private String name;
	private String path;
	public DBEntry(String fileName, String filePath)
	{
		this.name = fileName;
		this.path = filePath;
	}
	
	
}