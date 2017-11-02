package com.sds.orchestration.entity;

public class EventAppEntity {

	private String app_name;
	private String app_event_url;
	private String url_method;
	
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_event_url() {
		return app_event_url;
	}
	public void setApp_event_url(String app_event_url) {
		this.app_event_url = app_event_url;
	}
	public void setUrl_method(String url_method){
		this.url_method = url_method;
	}
	public String getUrl_method(){
		return this.url_method;
	}
	@Override
	public String toString() {
		return app_event_url;
	}
	
}
