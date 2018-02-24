package com.zouhx.crawler.util;

import java.io.IOException;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

	public JsonUtil() {
		// TODO Auto-generated constructor stub
	}
	public static String toJson(Object obj) {
		String ret = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			ret = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
