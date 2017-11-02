package com.sds.orchestration.entity;

public class TimerEntity {
	private String quantum;
	private String interval;
	private int counter;
	
	public void setQuantum(String quantum){
		this.quantum = quantum;
	}
	
	public void setInterval(String interval){
		this.interval = interval;
	}
	
	public void setCounter(int counter){
		this.counter = counter;
	}
	
	public String getQuantum(){
		return this.quantum;
	}
	
	public int getCounter(){
		return this.counter;
	}
	
	public String getInterval(){
		return this.interval;
	}
}
