package com.sds.orchestration.entity;

import java.io.Serializable;


/**
 * TriggerAppEntity:触发APP
 * EventAppEntity：事件APP
 * @author xpn
 *
 */
public class OrchestrationNode implements Serializable{
	
	private static final long serialVersionUID = 8147419497697842072L;
	private TriggerAppEntity triggerApp;
	private EventAppEntity eventApp;
	private TimerEntity timer;
	
	public TriggerAppEntity getTriggerApp() {
		return triggerApp;
	}
	public void setTriggerApp(TriggerAppEntity triggerApp) {
		this.triggerApp = triggerApp;
	}
	public EventAppEntity getEventApp() {
		return eventApp;
	}
	public void setEventApp(EventAppEntity eventApp) {
		this.eventApp = eventApp;
	}
	public TimerEntity getTimerEntity(){
		return timer;
	}
	public void setTimer(TimerEntity timer){
		this.timer = timer;
	}
	
}
