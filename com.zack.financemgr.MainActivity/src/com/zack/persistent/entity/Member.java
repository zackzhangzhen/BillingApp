package com.zack.persistent.entity;

public class Member {
	private long id;
	private String name;
	private String weibo;
	private String cell;
	private float deposit;
	private int lastPt;
	private int curPt;
	private int come;
	private int plusOne;
	private int leader;
	private int up;

	public int getUp() {
		return up;
	}
	
	public void setUp(int up) {
		this.up = up;
	}
	
	public int getLeader() {
		return leader;
	}
	
	public void setLeader(int leader) {
		this.leader = leader;
	}
	
	public int getCome() {
		return come;
	}

	public void setCome(int come) {
		this.come = come;
	}

	public int getPlusOne() {
		return plusOne;
	}

	public void setPlusOne(int plusOne) {
		this.plusOne = plusOne;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public float getDeposit() {
		return deposit;
	}

	public void setDeposit(float deposit) {
		this.deposit = deposit;
	}

	public int getLastPt() {
		return lastPt;
	}

	public void setLastPt(int lastPt) {
		this.lastPt = lastPt;
	}

	public int getCurPt() {
		return curPt;
	}

	public void setCurPt(int curPt) {
		this.curPt = curPt;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof Member)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		Member m = (Member) obj;
		return this.id == m.getId();
	}
}
