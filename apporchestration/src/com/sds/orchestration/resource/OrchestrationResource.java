package com.sds.orchestration.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sds.orchestration.entity.EventAppEntity;
import com.sds.orchestration.entity.OrchestrationEntity;
import com.sds.orchestration.entity.OrchestrationNode;
import com.sds.orchestration.entity.TriggerAppEntity;
import com.sds.orchestration.handler.OrchestrationHandler;

@Path("/orchestration")
public class OrchestrationResource {

	private static OrchestrationHandler handler=OrchestrationHandler.getOrchestrationHandler();
	protected static Logger log = LoggerFactory.getLogger(OrchestrationResource.class);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addOrcestration(OrchestrationEntity orchestrationEntity){
		log.info("POST Request received,start to handle ...");
		handler.addOrchestrationNode(orchestrationEntity);
		return "{\"operation\":\"ok\"}";
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public OrchestrationEntity getEntity(){
		log.info("GET Request received,start to handle ...");
		OrchestrationEntity res= new OrchestrationEntity();
		res.setOrchestration_name("orchestration_name");
		List<OrchestrationNode> orchestration_nodes=new ArrayList<OrchestrationNode>();
		
		OrchestrationNode e=new OrchestrationNode();
		TriggerAppEntity triggerApp = new TriggerAppEntity();
		triggerApp.setApp_name("trigger_app_name");
		triggerApp.setApp_trigger("NOT_SAFE");
		triggerApp.setApp_trigger_url("http://localhost/triggerApp/getTrigger");
		e.setTriggerApp(triggerApp);
		EventAppEntity eventApp=new EventAppEntity();
		eventApp.setApp_name("event_app_name");
		eventApp.setApp_event_url("http://localhost/eventApp/event");
		e.setEventApp(eventApp);
		orchestration_nodes.add(e);
		
		res.setOrchestration_nodes(orchestration_nodes);
		return res;
	}
}
