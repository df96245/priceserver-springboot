package com.ywcx.price.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ywcx.price.entity.json.BaiduResponse;
import com.ywcx.price.entity.json.BaiduResponseBase;
import com.ywcx.price.entity.json.Result;
public class JsonUtil {
	private static Logger logger=LoggerFactory.getLogger(JsonUtil.class);

	//两点间距离
	public static BaiduResponseBase parseJsonFromGetDistance(String json) {
		return JSONObject.parseObject(json, BaiduResponseBase.class);
	}
	
	public static BaiduResponse<Result> parseJsonFromDriving(String json) {
		return (BaiduResponse<Result>) JSON.parseObject(json, new TypeReference<BaiduResponse<Result>>(){});
	}
	
	public static String buildSimpleJson(int code , String msg) {
		String str =    "{\"status\":"
				+ code+",\"message\":"
				+ msg +
				"} ";
		return  str;
	}

}
