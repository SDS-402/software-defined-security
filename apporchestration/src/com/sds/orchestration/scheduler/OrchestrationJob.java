package com.sds.orchestration.scheduler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sds.orchestration.entity.CompareType;
import com.sds.orchestration.entity.DataType;
import com.sds.orchestration.entity.EventAppEntity;
import com.sds.orchestration.entity.ResponseEntity;
import com.sds.orchestration.entity.TriggerAppEntity;
import com.sds.orchestration.handler.JobHandler;
import com.sds.orchestration.utils.http.HTTPHelper;
import com.sds.orchestration.utils.http.HTTPHelperResult;
import com.sds.orchestration.utils.http.JsonUtils;

public class OrchestrationJob implements Job {

	protected static Logger log = LoggerFactory
			.getLogger(OrchestrationJob.class);
	
	private TriggerAppEntity triggerApp;
	private EventAppEntity eventApp;

	public OrchestrationJob() {
		super();
	}

	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap datamap = context.getJobDetail().getJobDataMap();
		triggerApp=(TriggerAppEntity) datamap.get("triggerApp");
		eventApp=(EventAppEntity) datamap.get("eventApp");
		if (eventApp == null || triggerApp == null)
			return;
		String reqUrl = triggerApp.getApp_trigger_url();
		log.info("App_trigger_url:"+reqUrl);
		Map<String, String> otherHeaders = new HashMap<String, String>();
		HTTPHelperResult result = HTTPHelper.httpGet(reqUrl, otherHeaders);
		log.info("www1234"+result.getCode()+" "+result.getMsg());
		String status = triggerApp.getApp_trigger();
		// 获取TriggerApp状态
		if (200 != result.getCode()) {
			log.error("receive status from trigger app error! Check net connection...");
			return;
		}
		log.info("receive status from trigger app ok!");
		// TriggerApp状态是否符合要求
		CompareType compare_type = triggerApp.getCompare_type();
		DataType data_type = triggerApp.getData_type();
		ResponseEntity responseEntity = null;
		try {
			 responseEntity = new ResponseEntity(result.getMsg());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("reponseStatus : "+ responseEntity.getStatus());
		
		if(!isCheck(status,responseEntity.getStatus(),compare_type,data_type)){
			log.info("event app will not be executed");
			return;
		}
		/*if (!status.equals(result.getMsg())) {
			log.error("event app error");
			return;
		}*/
		// 触发关联的EventApp
		String eventUrl = eventApp.getApp_event_url();
		String method = eventApp.getUrl_method();
		//TODO : get function name
		//String function = "com.sds.orchestration.developer."+"IdsFw";
		String function = "com.sds.orchestration.developer."+"ScanWaf";
		String appInput = null;
		
		//TODO : this function will be realized by developer
		try {
			Object obj;
			try {
				obj = Class.forName(function).newInstance();
				JobHandler jobHandler = (JobHandler)obj;
				appInput = jobHandler.processFunction(responseEntity.getMsg());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HTTPHelperResult resultEvent = null;
		//TODO : this function will be realized by developer
		Map<String,String> content = new HashMap<String,String>();
		if((triggerApp.getApp_name().equals("SCAN")) && (eventApp.getApp_name().equals("WAF"))){
			content.put("policy", responseEntity.getMsg());
			content.put("protected_host", getHost(reqUrl));
			resultEvent= HTTPHelper.httpRequest(eventUrl,method,JsonUtils.encodeMapToJson(content),
					otherHeaders);
		}
		else
		{
			log.info("firewall appInput:"+appInput);
			resultEvent = HTTPHelper.httpRequest(eventUrl,method,appInput,
					otherHeaders);
		}
		if (200 == resultEvent.getCode()) {
			log.info("event app ok");
			log.info("eventApp Result: "+ resultEvent.getMsg());
		}
	}

	//TODO : get host from scanner url
	private String getHost(String trigger_url){
		String[] url = trigger_url.split("\\?");
		return url[1];
		//return "192.168.19.12:8000";
	}
	
	
	private boolean isCheck(String status, String msg,
			CompareType compare_type, DataType data_type) {
		if(data_type==null||compare_type==null){
			log.error("the datatype or the compareType can not be null !");
			return false;
		}
		if(!DataType.contains(data_type)||!CompareType.contains(compare_type)){
			log.error("the datatype or the compareType is not supported !");
			return false;
		}
		
		switch (data_type) {
		case STRING:
			return isCompare(status,msg,compare_type);
		case INT:
			try {
				Integer.parseInt(status);
			} catch (NumberFormatException e) {
				log.error("NumberFormatException error: string can not covert integer" );
				return false;
			}
			return isCompare(Integer.parseInt(status),Integer.parseInt(msg),compare_type);
		default:
			break;
		}
		return false;
	}


	private boolean isCompare(int status,int msg, CompareType compare_type) {
		switch (compare_type) {
		case EQ:
			return status==msg;
		case NEQ:
			return status!=msg;
		case GT:
			return msg>status;
		case LT:
			return msg<status;
		default:
			break;
		}
		return false;
	}


	private boolean isCompare(String status, String msg,CompareType compare_type) {
		switch (compare_type) {
		case EQ:
			return status.equals(msg);
		case NEQ:
			return !status.equals(msg);
		default:
			log.error("the compare_type is not supported !");
			break;
		}
		return false;
	}


	public String getJobId(){
		return triggerApp.toString()+eventApp.toString();
	}
}
