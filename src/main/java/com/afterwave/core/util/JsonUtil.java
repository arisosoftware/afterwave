package com.afterwave.core.util;

import com.google.gson.Gson;
 
public class JsonUtil {

	public final static Gson gson = new Gson();

	public static <T> T jsonToObject(String json, Class<T> object) {
		return gson.fromJson(json, object);
	}

	public static String objectToJson(Object object) {
		return gson.toJson(object);
	}

}
