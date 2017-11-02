package com.sds.orchestration.core;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.PropertyConfigurator;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.owlike.genson.ext.jaxrs.GensonJsonConverter;
import com.sds.orchestration.resource.ClientResource;
import com.sds.orchestration.resource.OrchestrationResource;


public class Main {
	
	protected static Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws IllegalArgumentException, IOException {
		//read log configuration
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/conf/log4j.properties");
		
		//read global Configuration
		GlobalConfig config = GlobalConfig.getInstance();
        config.loadConfig("conf/global.config");
		String ip = config.local;
		int listenPort = Integer.parseInt(config.listenPort);
		
		String serverUrl="http://"+ip+":"+listenPort+"/";
		URI baseUri = UriBuilder.fromUri("http://"+ip+"/").port(listenPort).build();
		ResourceConfig configuration=new ResourceConfig(
				OrchestrationResource.class,
				ClientResource.class
				)
				.register(GensonJsonConverter.class);
		//HttpServer server=JdkHttpServerFactory.createHttpServer(baseUri, configuration);
		JdkHttpServerFactory.createHttpServer(baseUri, configuration);
		log.info("appOrchestration service start at: "+serverUrl);
	}
}