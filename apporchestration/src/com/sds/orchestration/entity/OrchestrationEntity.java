package com.sds.orchestration.entity;

import java.util.List;




public class OrchestrationEntity {

	private String orchestration_name;
	private List<OrchestrationNode> orchestration_nodes;
	
	public String getOrchestration_name() {
		return orchestration_name;
	}
	public void setOrchestration_name(String orchestration_name) {
		this.orchestration_name = orchestration_name;
	}
	public List<OrchestrationNode> getOrchestration_nodes() {
		return orchestration_nodes;
	}
	public void setOrchestration_nodes(List<OrchestrationNode> orchestration_nodes) {
		this.orchestration_nodes = orchestration_nodes;
	}
	
	
	
	
}
