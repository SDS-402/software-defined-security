package com.sds.orchestration.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class GlobalConfig {
	private static GlobalConfig config = new GlobalConfig();

	public static GlobalConfig getInstance(){
		return config;
	}
	public Integer Interval=3;//minutes
    public String local = "localhost";
    public String listenPort="9000";
    
	public void loadConfig(String configName){
		Field[] fs = this.getClass().getDeclaredFields();
    	BufferedReader br;
    	String       line;

    	try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(configName), Charset.forName("UTF-8")));
	    	while ((line = br.readLine()) != null) {
	    		if(line.startsWith("#"))
	    			continue;
	    		String[] s = line.split("=", 2);
	    		if(s == null || s.length!=2)
	    			continue;
	    		
	    		String key = s[0].trim();
	    		String value = s[1].trim();
	    		
	    		for(Field f : fs){
	    			if(!key.equals(f.getName()))
	    				continue;
	    			Type type = f.getType();
	    			if(type == String.class)
	    				f.set(this, value);
	    			else if(type == Integer.class){
	    				f.set(this, Integer.parseInt(value));
	    				break;
	    			}
	    			else if(type == Long.class){
	    				f.set(this, Long.parseLong(value));
	    				break;
	    			}
	    			else if(type == Float.class){
	    				f.set(this, Float.parseFloat(value));
	    				break;
	    			}
	    			else{
	    				System.err.println("Unresolved type :"+ type);
	    				break;
	    			}
	    		}
	    		
	    	}
	
	    	br.close();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	br = null;
    }
}
