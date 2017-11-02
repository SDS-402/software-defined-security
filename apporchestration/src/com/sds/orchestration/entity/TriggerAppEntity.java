package com.sds.orchestration.entity;

public class TriggerAppEntity {

	private String app_name;
	private String app_trigger;
	private DataType data_type;
	private CompareType compare_type;
	private String app_trigger_url;
	// Add url access method,like "GET" "POST"
	private String url_method;
	
	
	public DataType getData_type() {
		return data_type;
	}
	public void setData_type(DataType data_type) {
		this.data_type = data_type;
	}
	public CompareType getCompare_type() {
		return compare_type;
	}
	public void setCompare_type(CompareType compare_type) {
		this.compare_type = compare_type;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_trigger() {
		return app_trigger;
	}
	public void setApp_trigger(String app_trigger) {
		this.app_trigger = app_trigger;
	}
	public String getApp_trigger_url() {
		return app_trigger_url;
	}
	public void setApp_trigger_url(String app_trigger_url) {
		this.app_trigger_url = app_trigger_url;
	}
	public String getUrl_method(){
		return this.url_method;
	}
	public void setUrl_method(String url_method){
		this.url_method = url_method;
	}
	@Override
	public String toString() {
		return app_trigger+app_trigger_url;
	}
	
}
