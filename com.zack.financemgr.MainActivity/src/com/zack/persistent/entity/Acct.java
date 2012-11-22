package com.zack.persistent.entity;

public class Acct {
	private long id;
	private float deposit;
	
	public float getDeposit() {
		return deposit;
	}

	public void setDeposit(float deposit) {
		this.deposit = deposit;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof Acct)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		Acct m = (Acct) obj;
		return this.id == m.getId();
	}
}
