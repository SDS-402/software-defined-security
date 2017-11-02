package com.sds.orchestration.utils.http;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtils {

	//Convert {a:b} style string to java map
    public static Map<String, String> decodeJsonToMap(String fmJson) throws IOException {
        Map<String, String> data = new HashMap<String, String>();

    	if(fmJson == null)
    		return data; 
    				
        MappingJsonFactory f = new MappingJsonFactory();
        JsonParser jp;
        
        try {
            jp = f.createParser(fmJson);
        } catch (JsonParseException e) {
            throw new IOException(e);
        }
        
        jp.nextToken();
        if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IOException("Expected START_OBJECT");
        }
        
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            if (jp.getCurrentToken() != JsonToken.FIELD_NAME) {
                throw new IOException("Expected FIELD_NAME");
            }
            
            String key = jp.getCurrentName();
            jp.getCurrentToken();
            jp.nextToken();
            if (jp.getText().equals("")) 
                continue;
            
            data.put(key, jp.getText());
            jp.getCurrentToken();
        }
        
        return data;
    }
    
    public static List<String> decodeJsonToStringList(String fmJson) throws IOException{
        if (fmJson == null || fmJson.equals(""))  
            return new ArrayList<>();  
        ObjectMapper mapper = new ObjectMapper(); 
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, String.class); 
        @SuppressWarnings("unchecked")
		List<String> list =  (List<String>)mapper.readValue(fmJson, javaType); 
        return list; 
    }
    

	public static List<Map<String, String>> decodeJsonToStringMapList(String fmJson) throws IOException{
    	 if (fmJson == null || fmJson.equals(""))  
             return new ArrayList<>();  
         ObjectMapper mapper = new ObjectMapper(); 
         JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Map.class); 
  
		@SuppressWarnings("unchecked")
		List<Map<String, String>> list =  (List<Map<String, String>>)mapper.readValue(fmJson, javaType); 
         return list; 
    }
    
    
    
    @SuppressWarnings("rawtypes")
	public static String encodeMapToJson(Map map){
    	try {

    		ObjectMapper mapper = new ObjectMapper();
    		String json = "";
    			
    		//convert map to JSON string
    		json = mapper.writeValueAsString(map);
        	return json;
    	}catch(Exception e){
    		return null;
    	}
    	
    }
     
}
 