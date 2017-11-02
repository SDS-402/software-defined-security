package com.sds.orchestration.device;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.sds.orchestration.device.utils.Topology;
import com.sds.orchestration.device.utils.WafTemplate;
import com.sds.orchestration.utils.http.JsonUtils;


public class Waf {
	
	private String traget; 											//防护目标
	private Topology topology;										//WAF部署模式
	private Set<WafTemplate> tempalte = new HashSet<WafTemplate>(); //WAF防护模板
	
	public void setTarget(String target){
		this.traget = target;
	}
	
	public void setTopology(Topology topology){
		this.topology = topology;
	}
	
	public Topology getTopology(){
		return this.topology;
	}
	
	public void addTemplate(WafTemplate vul){
		this.tempalte.add(vul);
	}
	
	//获取WAF防护策略
	public String getWafPolicy(){
		Map<String,String> policy = new HashMap<String,String>();
		Map<String,String> temp = new HashMap<String,String>();
		policy.put("target",traget);
		policy.put("topology", topology.toString().toLowerCase());
		Iterator iter = tempalte.iterator();
		while(iter.hasNext()){
			WafTemplate vul = (WafTemplate)iter.next();
			temp.put(vul.toString().toLowerCase(), "enabled");
		}
		policy.put("template", temp.toString());
		return JsonUtils.encodeMapToJson(policy);
	}
}
