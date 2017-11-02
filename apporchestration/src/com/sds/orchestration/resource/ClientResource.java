package com.sds.orchestration.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @description : return query result about orchestration
 * @author Sherry
 *
 */

@Path("/client")
public class ClientResource {

	@Path("/triggerSafe")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getTrigger(){
		return "SAFE";
	}
	
	@Path("/triggerUnSafe")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getUnTrigger(){
		return "UN_SAFE";
	}
	
	@Path("/EventUnSafe")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String sendEvent(){
		System.out.println("send event ok");
		return "Event";
	}
	
}
