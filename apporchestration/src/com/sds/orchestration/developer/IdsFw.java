package com.sds.orchestration.developer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sds.orchestration.handler.JobHandler;

public class IdsFw implements JobHandler{
	/*
	 * {
	 * 	"type":"Possible TCP DoS",
	 *  "detail":
	 * }
	 * 
	 */
	@Override
	public String processFunction(String appOutput) {
		// TODO Auto-generated method stub
		Map<String,String> result = new HashMap<String,String>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode json = mapper.readTree(appOutput);
			System.out.println("IDS alert message...."+json.get("src_ip").toString());
			System.out.println("IDS alert message...."+json.get("dst_ip").toString());
			result.put("src_ip", json.get("src_ip").toString());
			result.put("dst_ip", json.get("dst_ip").toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{\"src_ip\":"+result.get("src_ip")+","+"\"dst_ip\":"+result.get("dst_ip")+"}";
	}

}
