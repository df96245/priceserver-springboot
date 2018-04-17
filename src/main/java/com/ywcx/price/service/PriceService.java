package com.ywcx.price.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ywcx.price.common.Constants;
import com.ywcx.price.common.ResponseCode;
import com.ywcx.price.common.ServerResponse;
import com.ywcx.price.factory.PriceCounterFactory;
import com.ywcx.price.util.JsonUtil;

@Service
public class PriceService {
	private Logger logger = LoggerFactory.getLogger(PriceService.class);
	
	private ThreadLocal<String> ori = new ThreadLocal<String>();
	private ThreadLocal<String> dest = new ThreadLocal<String>();
	
	@Autowired
	private TrackService tkService;

	public ServerResponse getRealTimePrice(String type, String city, String entityName, String start_time, String end_time)
			throws Exception {
		String json = tkService.getDistance(entityName, start_time, end_time);
		Double distance  = JsonUtil.parseDistanceByJson(json);
		if (distance<0) {
			if (distance==Constants.INCORRECT_STATUS) {
				String message= "获取距离失败。";
				return ServerResponse.createByErrorCodeMessage(ResponseCode.CALL_BAIDU_FAILD.getCode(), message);
			}else {
				String message= "起始和结束时间必须在24小时之内";
				return ServerResponse.createByErrorCodeMessage(ResponseCode.CALL_BAIDU_FAILD.getCode(), message);
			}
		}
		if (distance==0) {
			String message= "最近时间没位置没有变化.";
			return ServerResponse.createByErrorCodeMessage(ResponseCode.CALL_BAIDU_FAILD.getCode(), message);
		}
		//TODO for calculate mins for speed below 12km/h
		Double lowSpeedMin=0.0;
		Double realTimePrice =PriceCounterFactory.getPriceCounter(type).callPrice(distance, city,lowSpeedMin);
		return ServerResponse.createBySuccess("获取实时价钱成功",realTimePrice);
	}
	
	public ServerResponse getEstPrice(String oriLatLng, String destLatLng, String city) throws Exception {
		ori.set(oriLatLng);
		dest.set(destLatLng);
		String json = tkService.routematrix(oriLatLng, destLatLng, city);
		Double distance = JsonUtil.parseDistanceByJson(json);
		if (distance<0) {
			if (distance==Constants.INCORRECT_STATUS) {
				String message= "获取距离失败。";
				return ServerResponse.createByErrorCodeMessage(ResponseCode.CALL_BAIDU_FAILD.getCode(), message);
			}else {
				String message= "起始和结束时间必须在24小时之内";
				return ServerResponse.createByErrorCodeMessage(ResponseCode.CALL_BAIDU_FAILD.getCode(), message);
			}
		}
		if (distance==0) {
			String message= "起点和终点不能相同。";
			return ServerResponse.createByErrorCodeMessage(ResponseCode.CALL_BAIDU_FAILD.getCode(),message);
		}
		Map<String, Double> priceMap = new HashMap<>();
		//估价的时候先不考虑路况
		Double lowSpeedMin=0.0;
		priceMap.put("SELF", PriceCounterFactory.getPriceCounter("SELF").callPrice(distance, city,lowSpeedMin));
		priceMap.put("MANY", PriceCounterFactory.getPriceCounter("MANY").callPrice(distance, city,lowSpeedMin));

		return ServerResponse.createBySuccess("获取估算价格成功",priceMap);
	}


}
