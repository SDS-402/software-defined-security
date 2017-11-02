package com.sds.orchestration.entity;

public class TimeEntity {
	private int value;
	private TimeType type;
	
	public TimeEntity(int value,TimeType type){
		this.value = value;
		this.type = type;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public void setType(TimeType type){
		this.type = type;
	}

	public int getValue(){
		return this.value;
	}
	
	public TimeType getTimeType(){
		return this.type;
	}
}
